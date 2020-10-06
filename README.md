# Important!!

fix license stuff before publishing. awe copied from serverless goat and the api gateway pojos from: https://github.com/willh/lambda-helloworld-config/blob/master/src/main/java/com/willhamill/lambda/apigateway/ApiGatewayRequest.java

# Build the application
`mvn clean install`

**To test the generated Lambda function locally with SAM CLI , you can run the following command.**

Fist install sam: https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-cli-install.html

__MAKE SURE that you have at least sam-cli 1.3.2 installed. Otherwise, you will run into issues with the deployment__

`sam local invoke`

NOTE: The docker image used for local deployment does not have curl installed. The awslinux:1 image used after deploy does!

# deploy

Make sure you have setup your aws cli: https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-getting-started-set-up-credentials.html

User intellij plugin to deploy easily: 
https://aws.amazon.com/de/blogs/developer/aws-toolkit-for-intellij-now-generally-available/

I'll later add how to deploy from console.

__Do not try to run the deployment with docker containers '--use-containers', the java build will get stuck__

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

# Build something for amazon linux


` docker run -v /Users/mbenz/Documents/privat/codeshield/repos/serverless/Serverless-Goat-Java/src/main/resources:/var/task/ -v /tmp/repo:/var/cache/yum/x86_64/2/amzn2-core  --rm -it amazonlinux:2 bash`
`echo "diskspacecheck=0" >> /etc/yum.conf `
`yum update`
`yum groupinstall "Development Tools"`
Do whatever you want to
