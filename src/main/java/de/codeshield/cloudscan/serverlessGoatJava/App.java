package de.codeshield.cloudscan.serverlessGoatJava;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Lambda function entry point. You can change to use other pojo type or implement a different
 * RequestHandler.
 *
 * @see <a href=https://docs.aws.amazon.com/lambda/latest/dg/java-handler.html>Lambda Java
 *     Handler</a> for more information
 */
public class App
    implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
  private final AmazonS3 s3Client;
  private final Path catdocExecutable;

  public App() throws IOException {
    // Initialize the SDK client outside of the handler method so that it can be reused for
    // subsequent invocations.
    // It is initialized when the class is loaded.
    s3Client = AmazonS3ClientBuilder.standard().withRegion(Regions.EU_CENTRAL_1).build();

    // acquire catdoc executable for later use
    catdocExecutable = Paths.get("/var/task/bin/catdoc");
  }

  private void log(Context context, APIGatewayProxyRequestEvent request) {

    AmazonDynamoDB docClient =
        AmazonDynamoDBClientBuilder.standard().withRegion(Regions.EU_CENTRAL_1).build();

    String requestId = context.getAwsRequestId();
    String ip = request.getRequestContext().getIdentity().getSourceIp();
    String documentUrl = request.getQueryStringParameters().get("document_url");

    Map<String, AttributeValue> item = new HashMap<>();
    item.put("id", new AttributeValue(requestId));
    item.put("ip", new AttributeValue(ip));
    item.put("document_url", new AttributeValue(documentUrl));

    context.getLogger().log(item.toString());
    docClient.putItem(System.getenv("TABLE_NAME"), item);
  }

  @Override
  public APIGatewayProxyResponseEvent handleRequest(
      APIGatewayProxyRequestEvent request, final Context context) {
    try {
      log(context, request);

      String documentUrl = request.getQueryStringParameters().get("document_url");
      InputStream txt = getDocumentInputStream(documentUrl);

      // Lambda response max size is 6MB. The workaround is to upload result to S3 and redirect user
      // to the file.
      String key = UUID.randomUUID().toString();
      ObjectMetadata objectMetadata = new ObjectMetadata();
      objectMetadata.setContentType("text/html");

      s3Client.putObject(
          new PutObjectRequest(System.getenv("BUCKET_NAME"), key, txt, objectMetadata)
              .withCannedAcl(CannedAccessControlList.PublicRead));

      String docLocation = String.format("%s/%s", System.getenv("BUCKET_URL"), key);

      return new APIGatewayProxyResponseEvent()
          .withHeaders(Collections.singletonMap("Location", docLocation))
          .withStatusCode(302);

    } catch (Throwable e) {
      StringWriter sw = new StringWriter();
      e.printStackTrace(new PrintWriter(sw));
      String exceptionAsString = sw.toString();
      return new APIGatewayProxyResponseEvent().withBody(exceptionAsString).withStatusCode(500);
    }
  }

  private InputStream getDocumentInputStream(String documentUrl) throws IOException {

    String command =
        String.format(
            "curl --silent -L %s | /lib64/ld-linux-x86-64.so.2 %s -",
            documentUrl, catdocExecutable.toAbsolutePath().toString());

    Process process = new ProcessBuilder("/bin/sh", "-c", command).start();
    return process.getInputStream();
  }
}
