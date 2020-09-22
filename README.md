
# Build the application
`mvn clean install`

**To test the generated Lambda function locally with SAM CLI , you can run the following command.**

`sam local invoke`

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
``
