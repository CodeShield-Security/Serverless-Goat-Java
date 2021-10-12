# Deploying Serverless-Goat-Java to the AWS Serverless Application Repository

The content of this folder helps to easily deploy Serverless-Goat-Java to AWS SAR.

### Content
- `README.md`: This file
- `Makefile`: A Makefile containing the necessary commands to deploy Serverless-Goat-Java to SAR (uses a vulnerable setup by default!)
- `secure.yaml`: A template that creates the necessary S3 Bucket and Policy required by SAR. This template is properly secured against the confused deputy attack described [here](https://codeshield.io/blog/2021/08/26/sar_confused_deputy/).
- `vulnerable.yaml`: A template version that is vulnerable to the depicted confused deputy attack. (This is the default template used for deploying Serverlerless-Goat-Java to SAR.)


### Steps to deploy to SAR
__Care: By default, a vulnerable template configuration will be used for the deployment!__
1. Enter the directory that contains this file
2. Invoke `make create-bucket` to create a bucket with for all necessary artifacts. Configure sam as required. Beware that this bucket will be vulnerable ad `vulnerable.yaml`, as described above, will be used by default.
3. Invoke `make deploy` to deploy the app to the Serverless Application Repository
4. Copy the ARN of your newly created SAR app and replace `<APP-ARN` in the `Makefile` with it.
5. Invoke `make publish` to make the newly created SAR app publicly accessible by everyone.

__Note__: All make commands can be executed with an AWS profile for authentication. E.g., `make deploy PROFILE=testing`