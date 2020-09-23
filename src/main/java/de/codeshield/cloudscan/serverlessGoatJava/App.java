package de.codeshield.cloudscan.serverlessGoatJava;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import de.codeshield.cloudscan.serverlessGoatJava.apiGateway.ApiGatewayRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * Lambda function entry point. You can change to use other pojo type or implement
 * a different RequestHandler.
 *
 * @see <a href=https://docs.aws.amazon.com/lambda/latest/dg/java-handler.html>Lambda Java Handler</a> for more information
 */
public class App implements RequestHandler<ApiGatewayRequest, Object> {
  private final S3Client s3Client;
  private final Path catdocExecutable;

  public App() throws IOException {
    // Initialize the SDK client outside of the handler method so that it can be reused for subsequent invocations.
    // It is initialized when the class is loaded.
    s3Client = DependencyFactory.s3Client();

    // acquire catdoc executable for later use
    Path outDir = Files.createTempDirectory(System.currentTimeMillis() + "");
    catdocExecutable = IOUtils.copyResourceToFile(getClass(), "/catdoc", outDir);
  }

  private void log(Context context, ApiGatewayRequest request) {

    AmazonDynamoDB docClient = AmazonDynamoDBClientBuilder.standard()
            .withRegion(Regions.EU_CENTRAL_1)
            .build();

    String requestId = context.getAwsRequestId();
    String ip = request.getRequestContext().getIdentity().getSourceIp();
    String documentUrl = request.getQueryStringParameters().get("document_url");

    Map<String, AttributeValue> item = new HashMap<>();
    item.put("id", new AttributeValue(requestId));
    item.put("ip", new AttributeValue(ip));
    item.put("document_url", new AttributeValue(documentUrl));

    docClient.putItem(System.getenv("TABLE_NAME"), item);
  }

  @Override
  public Object handleRequest(ApiGatewayRequest request, final Context context) {
    log(context, request);

    String documentUrl = request.getQueryStringParameters().get("document_url");
    String txt = getDocumentText(documentUrl);

    // TODO: invoking the api call using s3Client.
    return request;
  }

  private String getDocumentText(String documentUrl) {

    String command = String.format("curl --silent -L %s | /lib64/ld-linux-x86-64.so.2 %s -", documentUrl, catdocExecutable.toAbsolutePath().toString());

    try {
      Process process = new ProcessBuilder("/bin/sh", "-c", command).start();
      return com.amazonaws.util.IOUtils.toString(process.getInputStream());
    } catch (IOException e) {
      throw new RuntimeException("Could not execute catdoc");
    }
  }

}
