# s3

## Resources

See the [AWS SAM developer guide](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/what-is-sam.html) 

N [AWS Serverless Application Repository main page](https://aws.amazon.com/serverless/serverlessrepo/)

## Interact with s3

`aws configure --profile sv` configure AWS CLI

`aws configure list --profile sv`


aws --endpoint-url=http://localhost:4566 s3 ls --profile sv

aws --endpoint-url=http://localhost:4566 s3api create-bucket --bucket sample --profile sv


aws s3api put-object --bucket sample --key test2.txt --body test2.txt --endpoint-url=http://localhost:4566 --profile sv


aws s3api list-objects --bucket sample --endpoint-url=http://localhost:4566 --profile sv

## Lambda deployed to localstack

`sam build`

`C:\Users\username\.aws`(windows) or ~/.aws (unix)

update config file adding endpoint_url

```config
[profile sv]
region = us-east-1
output = json
endpoint_url = http://localhost:4566
```

sam local generate-event s3 put --bucket vsvdevsource > events/s3.json

`sam deploy --guided --profile sv`

`aws lambda list-functions --profile sv`


aws s3api list-objects --bucket vsvdevdestination --profile sv

aws s3 rm s3://vsvdevdestination/test2.txt --profile sv


aws s3api put-object --bucket vsvdevsource --key test2.txt --body test2.txt --profile sv



aws s3api list-objects --bucket vsvdevdestination --profile sv

aws s3api presign s3://vsvdevsource/test2.txt --profile sv


sam local invoke Storage --event s3.json


sam remote invoke Storage --stack-name agapp --event events/s3.json


aws lambda invoke --invocation-type Event --function-name app-Storage-1b7a388c --cli-binary-format raw-in-base64-out --payload file://s3e.json --profile sv out.txt

`aws lambda list-functions --profile sv`


aws lambda invoke --invocation-type Event --function-name app-Storage-1b7a388c --cli-binary-format raw-in-base64-out --payload file://s3e.json --profile sv out.txt

bash


aws lambda invoke --function-name app-Storage-1b7a388c --cli-binary-format raw-in-base64-out --payload file://s3e.json --profile sv out --log-type Tail --query 'LogResult' --output text --cli-binary-format raw-in-base64-out | base64 --decode


## Lambda locally tested with localstack

`sam build`

`C:\Users\username\.aws`(windows) or ~/.aws (unix)

update config file adding endpoint_url

```config
[profile sv]
region = us-east-1
output = json
endpoint_url = http://localhost:4566
```

sam local generate-event s3 put --bucket vsvdevsource > events/s3.json

aws s3api create-bucket --bucket vsvdevsource --profile sv

aws s3api create-bucket --bucket vsvdevdestination --profile sv

aws s3api list-objects --bucket vsvdevdestination --profile sv

aws s3api list-objects --bucket vsvdevsource --profile sv

aws s3 rm s3://vsvdevdestination/test2.txt --profile sv


aws s3api put-object --bucket vsvdevsource --key test2.txt --body test2.txt --profile sv



aws s3api list-objects --bucket vsvdevdestination --profile sv


sam local invoke Storage --event s3.json


sam remote invoke Storage --stack-name agapp --event events/s3.json




## Lambda locally tested with s3server

`sam build`


https://github.com/VSVDEv/s3server

docker run -p 8080:7788 ^
  -e AWS_PORT=7788 ^
  -e AWS_ACCESS_KEY=3 ^
  -e AWS_SECRET_ACCESS_KEY=2 ^
  -e AWS_REGION=us-east-1 ^
  -e AWS_FOLDER=/app/s3 ^
  -v %cd%\s3:/app/s3 ^
  vsvdevua/s3server:1.1

using postman collection create buckets

- vsvdevsource

- vsvdevdestination

and put file test2.txt to vsvdevsource

sam local invoke Storage --event s3.json --docker-network host