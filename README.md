# Important!!

fix license stuff before publishing. awe copied from serverless goat and the api gateway pojos from: https://github.com/willh/lambda-helloworld-config/blob/master/src/main/java/com/willhamill/lambda/apigateway/ApiGatewayRequest.java

# Build the application
`mvn clean install`

**To test the generated Lambda function locally with SAM CLI , you can run the following command.**

Fist install sam: https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-cli-install.html

`sam local invoke`

# IntelliJ AWS support for debugging and deployment


https://aws.amazon.com/de/blogs/developer/aws-toolkit-for-intellij-now-generally-available/

# deploy

Make sure you have setup your aws cli: https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-getting-started-set-up-credentials.html

# Command used to create the lambda

```
 mvn org.apache.maven.plugins:maven-archetype-plugin:3.1.0:generate \                                                                                                                                                1 ↵  10265  15:36:44
    -DarchetypeGroupId=software.amazon.awssdk \
    -DarchetypeArtifactId=archetype-lambda \
    -DarchetypeVersion=2.14.22 \
    -DgroupId=de.codeshield.cloudscan \
    -DartifactId=serverless-goat-java \
    -Dservice=s3  \
    -Dregion=eu-central-1 \
    -DinteractiveMode=false
```

# Further hints
- Accessing all sorts of context variables in java: https://willhamill.com/2016/12/12/aws-api-gateway-lambda-proxy-request-and-response-objects
