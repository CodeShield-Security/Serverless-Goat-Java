
default:
	# call sam deploy --guided once to create a proper config file
	sam build
	sam deploy --no-fail-on-empty-changeset  --no-confirm-changeset
	aws s3 cp sensitive.info s3://serverless-goat-packages

	cd ..; sam build; \
	sam package \
	--template-file ./template.yaml \
	--output-template-file .aws-sam/build/package.yaml \
	--s3-bucket serverless-goat-packages
	cd ..; sam publish --template .aws-sam/build/package.yaml


publish:	
	aws serverlessrepo put-application-policy \
	--application-id <APP-ARN> \
	--statements Principals=*,Actions=Deploy \
	--profile testing