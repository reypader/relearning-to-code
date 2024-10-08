# Chapter 4: Switch to DynamoDB

Now that we're done dealing with "traditional", let's mix it up by immediately pivoting to DynamoDB, a very typical transition done by anyone working with AWS.

Our goal for this chapter is to keep most everything as-is except for the `Repository` used. We're largely following the guidance by [Zdenek Papez](https://zdenek-papez.medium.com/spring-boot-integration-tests-using-aws-dynamodb-local-with-maven-7dba6ca2ccb9) and [the offical AWS docs for DynamoDB](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/ProgrammingWithJava.html#AboutProgrammingWithJavaSDK).

Quick note:
- To create the tables for the local DynamoDB: `aws dynamodb create-table --table-name PERSISTED_EVENT --attribute-definitions AttributeName=id,AttributeType=S --key-schema AttributeName=id,KeyType=HASH --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5 --endpoint-url http://localhost:8000`

