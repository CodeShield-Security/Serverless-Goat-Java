PROFILE ?= default

default:
	@echo "Choose one of (create-bucket, deploy, publish)"

deploy:
	cd ..; mvn package && sam build; \
	sam package \
	--profile $(PROFILE) \
	--template-file ./template.yaml \
	--output-template-file .aws-sam/build/package.yaml \
	--s3-bucket serverless-goat-packages
	cd ..; sam publish --profile $(PROFILE) --template .aws-sam/build/package.yaml

create-bucket:
	sam build --template-file ./vulnerable.yaml
	sam deploy --profile $(PROFILE) --guided --no-fail-on-empty-changeset  --no-confirm-changeset
	aws s3 cp sensitive.info s3://serverless-goat-packages

publish:	
	aws serverlessrepo put-application-policy \
	--profile $(PROFILE) \
	--application-id <APP-ARN> \
	--statements Principals=*,Actions=Deploy
