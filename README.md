# AWS Lambda


## Content

[Links](#links)

[Vagrant docker](#vagrant-docker)

[OpenLDAP](#openldap)

[Using OpenLDAP in Vagrant](#using-openldap-in-vagrant)

[Apache DS](#apache-ds)



## Links

[open](https://www.openldap.org/)

[apache](https://directory.apache.org/)

[apache studio](https://directory.apache.org/studio/)

[Content](#content)


## Install CLI

[install](https://docs.aws.amazon.com/cli/latest/userguide/getting-started-install.html)

[use docker with cli](https://docs.aws.amazon.com/cli/latest/userguide/getting-started-docker.html)

We can use docker to run aws cli commands without installing CLI

[use docker with cli](https://docs.aws.amazon.com/cli/latest/userguide/getting-started-docker.html)


Configuration stored in `.aws` in your home directory(variables %UserProfile% in Windows and $HOME or ~ (tilde) in Unix-based systems) 

### profiles

We can have multiple profiles for different environment

`aws ec2 describe-instances --profile user1`

`aws configure --profile sv` - create profile sv

Linux `export AWS_PROFILE=user1`

Windows `setx AWS_PROFILE user1`

`aws configure` 

Config example:

```sh
AWS Access Key ID [None]: AKIAIOSFODNN7EXAMPLE
AWS Secret Access Key [None]: wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY
Default region name [None]: us-west-2
Default output format [None]: json

```
Example for local development:

```sh
AWS Access Key ID [None]: a
AWS Secret Access Key [None]: a
Default region name [None]: us-west-2
Default output format [None]: json

```

`aws configure set`

`aws configure list`

`aws configure list-profiles`

`setx AWS_DEFAULT_PROFILE user2`

`set AWS_DEFAULT_PROFILE=local`

## SAM CLI

[install](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/install-sam-cli.html)

`sam init` use template to create project

`sam build` build project

`sam build --use-container` build project in docker

`sam deploy --guided`

`sam list endpoints --output json`

```sh
curl https://ets1gv8lxi.execute-api.us-west-2.amazonaws.com/Prod/hello/
{"message": "hello world"}
```

`sam remote invoke` command to interact with supported AWS resources in the AWS Cloud

`sam remote invoke HelloWorldFunction --stack-name sam-app` invoke your Lambda function in the cloud

`sam sync --watch` command to sync local changes to the AWS Cloud

`sam delete`  delete your application from the AWS Cloud

`sam local generate-event` generate events for supported AWS services [doc](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/using-sam-cli-local-generate-event.html)

`sam local generate-event apigateway http-api-proxy > events/apigateway_event.json`

`sam local invoke`  invoke your Lambda function locally

`sam local invoke --event events/apigateway_event.json`

`sam local start-api` run your AWS Lambda functions locally and test through a local HTTP server host

`sam local invoke HelloWorldFunction`

`sam local invoke HelloWorldFunction --event events/event.json`

`curl http://127.0.0.1:3000/hello`

`sam local invoke --event events/s3.json S3JsonLoggerFunction`



### Pass environment variables when invoking a Lambda function locally

```yaml
AWSTemplateFormatVersion: 2010-09-09
Transform: AWS::Serverless-2016-10-31
...
Resources:
  getAllItemsFunction:
    Type: AWS::Serverless::Function
    Properties:
      Handler: src/get-all-items.getAllItemsHandler
      Description: get all items
      Policies:
        - DynamoDBReadPolicy:
            TableName: !Ref SampleTable
      Environment:
        Variables:
          SAMPLE_TABLE: !Ref SampleTable
...

```
For local testing we want use different params

locals.json:

```json
{
    "getAllItemsFunction": {
        "SAMPLE_TABLE": "dev-demo-SampleTable-1U991234LD5UM98"
    }
}
```

then use:

`sam local invoke getAllItemsFunction --env-vars locals.json`


### SAM SYNC

`sam sync` command provides options to quickly sync local application changes to the AWS Cloud

after command:

1) Build your application(sam build)

2) Deploy your application 

3) Watch for local changes

4) Sync local changes to the AWS Cloud

samconfig.toml:

```toml
version = 0.1
	
	[default]
	[default.global.parameters]
	stack_name = "lambda-streaming-nodejs-app"
	
	[default.build.parameters]
	cached = true
	parallel = true
	
	[default.validate.parameters]
	lint = true
	
	[default.deploy.parameters]
	capabilities = "CAPABILITY_IAM"
	confirm_changeset = true
	resolve_s3 = true
	s3_prefix = "lambda-streaming-nodejs-app"
	region = "us-west-2"
	image_repositories = []
	
	[default.package.parameters]
	resolve_s3 = true
	
	[default.sync.parameters]
	watch = true
	
	[default.local_start_api.parameters]
	warm_containers = "EAGER"
	
	[default.local_start_lambda.parameters]
	warm_containers = "EAGER"

```

[Content](#content)

## SAM template

closely follows the format of an AWS CloudFormation template file(file that describes your AWS infrastructure)

```yaml
# required with this value
Transform: AWS::Serverless-2016-10-31

# required The stack resources and their properties
Resources:
  set of resources
  
# optional Properties that are common to all your serverless functions, APIs, and simple tables
Globals:
  set of globals


# optional string that describes the template
Description:
  String


# optional additional information about the template
Metadata:
  template metadata

# optional Values to pass to your template at runtime
Parameters:
  set of parameters

# optional a mapping of keys and associated values that you can use to specify conditional parameter values, similar to a lookup table
Mappings:
  set of mappings

# optional control whether certain resources are created or whether certain resource properties are assigned a value during stack creation or update
Conditions:
  set of conditions


# optional The values that are returned whenever you view your stack's properties
Outputs:
  set of outputs

```

[Content](#content)

## Invocation types

### calling the function synchronously:

```sh
aws lambda invoke \
--invocation-type RequestResponse \
--function-name HelloWorldJava \
--payload \"world\" outputfile.txt
```
`aws lambda invoke --invocation-type RequestResponse --function-name HelloWorldJava --payload \"world\" outputfile.txt`

`aws lambda invoke --invocation-type RequestResponse --function-name <FunctionName>  --payload "data" outputfile.txt`

`aws lambda invoke --invocation-type RequestResponse --function-name <FunctionName> outputfile.txt`

`sam local invoke HelloWorldFunction --event events/event.json > outputfile.txt`

RequestResponse (default) – Invoke the function synchronously. Keep the connection open until the function returns a response or times out. The API response includes the function response and additional data.

### called the function asynchronously:

```sh
aws lambda invoke \
--invocation-type Event \
--function-name HelloWorldJava \
--payload \"world\" outputfile.txt
```

`aws lambda invoke --invocation-type Event --function-name HelloWorldJava --payload \"world\" outputfile.txt`

`sam local invoke HelloWorldFunction --event events/s3.json > outputfile.txt`

Event – Invoke the function asynchronously. Send events that fail multiple times to the function's dead-letter queue (if one is configured). The API response only includes a status code.


### validation:

```sh
aws lambda invoke \
--invocation-type DryRun \
--function-name HelloWorldJava \
--payload \"world\" outputfile.txt
```
`aws lambda invoke --invocation-type DryRun --function-name HelloWorldFunction --payload \"sv\" outputfile.txt`

DryRun – Validate parameter values and verify that the user or role has permission to invoke the function.



### Payload

The JSON that you want to provide to your Lambda function as input.

You can enter the JSON directly. For example, `--payload '{ "key": "value" }'`. 

You can also specify a file path. For example, `--payload file://payload.json`.

[Content](#content)


## Logs

`aws lambda invoke --function-name my-function out --log-type Tail` retrieve a log ID from the LogResult field for a function named my-function


how to retrieve base64-encoded logs for my-function:

```sh
aws lambda invoke --function-name my-function out --log-type Tail \
--query 'LogResult' --output text --cli-binary-format raw-in-base64-out | base64 --decode
```



[Content](#content)

## Maven

The plugin creates a JAR file that contains the compiled function code and all of its dependencies.

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>helloworld</groupId>
    <artifactId>HelloWorld</artifactId>
    <version>1.0</version>
    <packaging>jar</packaging>
    <name>A sample Hello World created for SAM CLI.</name>
    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
    </properties>

    <dependencies>
        <dependency>
            <groupId>com.amazonaws</groupId>
            <artifactId>aws-lambda-java-core</artifactId>
            <version>1.2.2</version>
        </dependency>
        <dependency>
          <groupId>com.amazonaws</groupId>
          <artifactId>aws-lambda-java-events</artifactId>
          <version>3.11.0</version>
        </dependency>
		<dependency>
      	  <groupId>com.amazonaws</groupId>
      	  <artifactId>aws-lambda-java-log4j2</artifactId>
      	  <version>1.5.1</version>
       </dependency> 
        <dependency>
          <groupId>junit</groupId>
          <artifactId>junit</artifactId>
          <version>4.13.2</version>
          <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
      <plugins>
       <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-shade-plugin</artifactId>
        <version>3.2.2</version>
        <configuration>
          <createDependencyReducedPom>false</createDependencyReducedPom>
        </configuration>
        <executions>
          <execution>
            <phase>package</phase>
            <goals>
              <goal>shade</goal>
            </goals>
            <configuration>
              <transformers>
                <transformer implementation="com.github.edwgiz.maven_shade_plugin.log4j2_cache_transformer.PluginsCacheFileTransformer">
                </transformer>
              </transformers>
            </configuration>
          </execution>
        </executions>
        <dependencies>
          <dependency>
            <groupId>com.github.edwgiz</groupId>
            <artifactId>maven-shade-plugin.log4j2-cachefile-transformer</artifactId>
            <version>2.13.0</version>
          </dependency>
        </dependencies>
      </plugin>
      </plugins>
    </build>
</project>

```

## Gradle

```gradle
plugins {
    id 'java'
}

repositories {
    mavenCentral()
}

dependencies {
    implementation platform('software.amazon.awssdk:bom:2.15.0')
    implementation platform('com.amazonaws:aws-xray-recorder-sdk-bom:2.11.0')
    implementation 'software.amazon.awssdk:s3'
    implementation 'com.amazonaws:aws-lambda-java-core:1.2.1'
    implementation 'com.amazonaws:aws-lambda-java-events:3.11.0'
    implementation 'org.apache.logging.log4j:log4j-api:[2.17.1,)'
    implementation 'org.apache.logging.log4j:log4j-core:[2.17.1,)'
    implementation 'org.apache.logging.log4j:log4j-slf4j18-impl:[2.17.1,)'
    runtimeOnly 'com.amazonaws:aws-lambda-java-log4j2:1.5.1'
    implementation 'com.amazonaws:aws-xray-recorder-sdk-core'
    implementation 'com.amazonaws:aws-xray-recorder-sdk-aws-sdk'
    implementation 'com.amazonaws:aws-xray-recorder-sdk-aws-sdk-instrumentor'
    testImplementation 'org.junit.jupiter:junit-jupiter-api:5.6.0'
    testRuntimeOnly 'org.junit.jupiter:junit-jupiter-engine:5.6.0'
}

test {
    useJUnitPlatform()
}

task packageJar(type: Zip) {
    into('lib') {
        from(jar)
        from(configurations.runtimeClasspath)
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

build.dependsOn packageJar

```


[Content](#content)


## Environments


You can add a `com.amazonaws.services.lambda.runtime.Context` parameter to the end of any
handler parameter list, and the runtime will pass in an interesting object that you can
use

```java
public void handler (Object input, Context context) {

System.out.println("getMemoryLimitInMB " + context.getMemoryLimitInMB() + "");
}
```
full function list in aws docs

### Variables

```yaml
 Timeout: 2
      Environment:
        Variables:
          DATABASE_URL: my-database-url
          restapiurl: testurl

```

```java

 public void envHandler(Context context) {
		
		System.out.println(System.getenv("restapiurl"));
		String databaseUrl = System.getenv("DATABASE_URL");
        if (databaseUrl == null || databaseUrl.isEmpty())
        System.out.println("DATABASE_URL is not set");
        else
        System.out.println("DATABASE_URL is set to: " + databaseUrl);
  }
```

### NOTE: If yoiu have expensive operations make it global and reuse during invocation like instanceVar and staticVar.


```java

public class App {

    
	private Double instanceVar = 7.33;
	private static Double staticVar = 8.22;


public void handleVariables() {
            int localVar = Math.random();
            System.out.println("Instance: "+instanceVar + " Static Variable: "
            +staticVar + " localVariable " + localVar);
        }
}
```

[Content](#content)




## Timeout

```yaml
.....
     Runtime: java17
     MemorySize: 512
      # max is 900(15 minutes)
      # default is 3 seconds
     Timeout: 2
     Environment:
......

```

```java
public void handlerWithTimeout (Object input, Context context) throws InterruptedException {
        while(true) {
        Thread.sleep(100);
        System.out.println("Context.getRemainingTimeInMillis() : " +
        context.getRemainingTimeInMillis());
        }
        }
```

[Content](#content)


## Cold start

https://docs.aws.amazon.com/lambda/latest/operatorguide/execution-environments.html

https://www.youtube.com/live/Y5b8_KToeDY?si=loKY_HozAiTUXbFp

https://aws.amazon.com/blogs/compute/operating-lambda-performance-optimization-part-1/

https://aws.amazon.com/blogs/compute/operating-lambda-performance-optimization-part-2/

https://aws.amazon.com/blogs/compute/operating-lambda-performance-optimization-part-3/



### cold starts and latency

When the Lambda service receives a request to run a function via the Lambda API, the service first prepares an execution environment. During this step, the service downloads the code for the function. It then creates an environment with the memory, runtime, and configuration specified. Once complete, Lambda runs any initialization code outside of the event handler before finally running the handler code.

first two steps of setting up the environment and the code are frequently referred to as a “cold start”. You are not charged for the time it takes for Lambda to prepare the function but it does add latency to the overall invocation duration.

After the execution completes, the execution environment is frozen. To improve resource management and performance, the Lambda service retains the execution environment for a non-deterministic period of time. During this time, if another request arrives for the same function, the service may reuse the environment. This second request typically finishes more quickly, since the execution environment already exists and it’s not necessary to download the code and run the initialization code. This is called a “warm start”.

anytime you update the code in a Lambda function or change the functional configuration, the next invocation results in a cold start. 

###  functions warmers

serverless community provides open source libraries to “warm” Lambda functions via a pinging mechanism. uses EventBridge rules to schedule invocations of the function every minute. However, this is not a guaranteed way to reduce cold starts. you cannot target a warm environment for an invocation.


### Provisioned Concurrency

is the recommended solution to ensure the lowest possible latency. This feature keeps your functions initialized and warm, ready to respond in double-digit milliseconds at the scale you provision. Unlike on-demand Lambda functions, this means that all setup activities happen ahead of invocation, including running the initialization code.

Functions with Provisioned Concurrency differ from on-demand functions in some important ways:

- Initialization code does not need to be optimized. Since this happens long before the invocation, lengthy initialization does not impact the latency of invocations. 

- Initialization code is run more frequently than the total number of invocations. Since Lambda is highly available, for every one unit of Provisioned Concurrency, there are a minimum of two execution environments prepared in separate Availability Zones. 

- Provisioned Concurrency cannot be used with the $LATEST version. This feature can only be used with published versions and aliases of a function. 

### invocation patterns

Lambda execution environments handle one request at a time. After the invocation has ended, the execution environment is retained for a period of time. If another request arrives, the environment is reused to handle the subsequent request.

If requests arrive simultaneously, the Lambda service scales up the Lambda function to provide multiple execution environments. Each environment has to be set up independently, so each invocation experiences a full cold start.

if API Gateway invokes a Lambda function 6 times sequentially with a delay between each invocation, the existing execution environments are reused if the previous invocation is complete. In this case, only the first two invocations experience a cold start, while invocations 3 through 6 use warm environments

For asynchronous invocations, an internal queue exists between the caller and the Lambda service. Lambda processes messages from this queue as quickly as possible and scales up automatically as needed. If the function uses reserved concurrency, this acts as a maximum capacity

### When happen

- the first invocation

- update

- scalling

- environment’s lifetime expired



### Memory and computing power

You can configure the amount of memory allocated to a Lambda function, between 128 MB and 10,240 MB.  Adding more memory proportionally increases the amount of CPU, increasing the overall computational power available. Since the Lambda service charges for the total amount of gigabyte-seconds consumed by a function, increasing the memory has an impact on overall cost if the total duration stays constant. 

Memory	Duration	Cost
128 MB	11.722 s	$0.024628
1024 MB	1.465 s	$0.024638

### Profiling functions with AWS Lambda Power Tuning

https://github.com/alexcasalboni/aws-lambda-power-tuning


### Optimizing static initialization

Static initialization happens before the handler code starts running in a function. This is the “INIT” code that happens outside of the handler. This code is often used to import libraries and dependencies, set up configuration and initialize connections to other services. In our analyses of Lambda performance across production invocations, data shows that the largest contributor of latency before function execution comes from INIT code. The section that developers can control the most can also have the biggest impact on the duration of a cold start.

Static initialization is often a good place to open database connections to allow a function to reuse connections over multiple invocations to the same execution environment. However, you may have large numbers of objects that are only used in certain execution paths in your function. In this case, you can lazily load variables in the global scope to reduce the static initialization duration.

Global variables should be avoided for context-specific information per invocation. If your function has a global variable that is used only for the lifetime of a single invocation and is reset for the next invocation, use a variable scope that is local to the handler. Not only does this prevent global variable leaks across invocations, it also improves the static initialization performance.


Provisioned Concurrency allows you to prepare execution environments before receiving traffic, making it ideal for functions with complex and lengthy INIT code. In this case, the duration of the INIT code does not impact the overall performance of an invocation.


### Class loading

Use lightweight dependencies:

 - jackson-jr

 - slf4j-simple

### Runtime dependency injection

- Micronaut(Micronaut’s design philosophy is centered around three key principles: compile-time dependency injection, minimal reflection, and efficient resource utilization.)

- Quarkus(Quarkus leverages a combination of compile-time and runtime techniques to enhance dependency injection for efficient injection. By minimizing reflection and optimizing the injection process, Quarkkus achieves two things: a more streamlined and efficient approach to dependency injection and the elimination of unnecessary reflection, leading to smaller memory requirements.
Quarkus applies classpath trimming, including only essential classes and dependencies in the final executable. This helps reduce the size of the application, contributing to faster startup times.
Quarkus emphasizes build-time configuration enhancements, ensuring that certain configuration tasks are performed during the build process rather than at runtime.)

- Dagger (is a fully static, compile-time dependency injection framework for Java, Kotlin, and Android.)

- NO dependency injection

### Just-in-time compilation

- More memory

- Tiered compilation is enabled by default in Java 17 on AWS Lambda(By setting the tiered compilation level to 1, the JVM uses the C1 compiler. This compiler quickly produces optimized native code but it does not generate any profiling data and never uses the C2 compiler.

Tiered compilation is a feature of the Java virtual machine (JVM). It allows the JVM to make best use of the just-in-time (JIT) compilers. The C1 compiler is optimized for fast start-up time. The C2 compiler is optimized for the best overall performance but uses more memory and takes a longer time to achieve it.

There are five different levels of tiered compilation. Level 0 is where Java byte code is interpreted. Level 4 is where the C2 compiler analyses profiling data collected during application startup. It observes code usage over a period of time to find the best optimizations. Choosing the correct level can help you optimize your performance.)

- AOT GraalVM native-image(GraalVM  is a high-performance runtime that is designed to address the limitations of traditional VMs such as initialization overhead and memory consumption. Beyond using GraalVM as just another JVM, you can also create native executables. Native executables includes all necessary dependencies (for example: garbage collector) and therefore does not a require a JVM to run your code.)
https://anywhere.epam.com/en/blog/graalvm-aws-lambda-in-java-for-cold-start-problems


### AWS Lambda SnapStart

Lambda SnapStart for Java can improve startup performance for latency-sensitive applications by up to 10x at no extra cost, typically with no changes to your function code. The largest contributor to startup latency (often referred to as cold start time) is the time that Lambda spends initializing the function, which includes loading the function's code, starting the runtime, and initializing the function code.

With SnapStart, Lambda initializes your function when you publish a function version. Lambda takes a Firecracker microVM snapshot of the memory and disk state of the initialized execution environment, encrypts the snapshot, and caches it for low-latency access. When you invoke the function version for the first time, and as the invocations scale up, Lambda resumes new execution environments from the cached snapshot instead of initializing them from scratch, improving startup latency.

https://catalog.workshops.aws/java-on-aws-lambda/en-US/03-snapstart

template.yaml

```yaml

SnapStart:
        ApplyOn: PublishedVersions
      AutoPublishAlias: snap
```


```yaml
Resources:
  UnicornStockBrokerFunction:
    Type: AWS::Serverless::Function
    Properties:
      CodeUri: target/UnicornStockBroker-1.1-aws.jar
      FunctionName: unicorn-stock-broker
      SnapStart:
        ApplyOn: PublishedVersions
      AutoPublishAlias: snap
      Handler: org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest
      Policies:
```

https://github.com/aws/aws-lambda-snapstart-java-rules

### Runtime Hooks 

Use when:

- Uniqueness
If your initialization code generates unique content that is included in the snapshot, then the content might not be unique when it is reused across execution environments. To maintain uniqueness when using SnapStart, you must generate unique content after initialization. This includes unique IDs, unique secrets, and entropy that's used to generate pseudorandomness. To learn how to restore uniqueness, see Handling uniqueness with Lambda SnapStart.

- Network connections
The state of connections that your function establishes during the initialization phase isn't guaranteed when Lambda resumes your function from a snapshot. Validate the state of your network connections and re-establish them as necessary. In most cases, network connections that an AWS SDK establishes automatically resume. For other connections, review the best practices.

- Temporary data
Some functions download or initialize ephemeral data, such as temporary credentials or cached timestamps, during the initialization phase. Refresh ephemeral data in the function handler before using it, even when not using SnapStart. 




## Integration with s3

[sdk](https://github.com/aws/aws-sdk-java-v2/#using-the-sdk)

```xml
<dependency>
  <groupId>software.amazon.awssdk</groupId>
  <artifactId>s3</artifactId>
  <version>2.25.40</version>
</dependency>
```



`sam local generate-event`

`sam local generate-event s3 put--bucket MyBucket`

`sam local generate-event s3 put --bucket MyBucket > events/s3.json`