package org.example;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;

import java.nio.charset.StandardCharsets;

public class Test {



    public static void main(String[] args) {

        // (1a) Instantiate credentials
        BasicAWSCredentials credentials = new
                BasicAWSCredentials("test", "test");

        Regions region = Regions.fromName("us-east-1");

// (2) Modify to leverage credentials
        AWSLambdaClientBuilder builder = AWSLambdaClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region);



// (3) Build the client, which will ultimately invoke the function
        AWSLambda client = builder.build();
// (4) Create an InvokeRequest with required parameters
        InvokeRequest req = new InvokeRequest()
            .withFunctionName("node")
            //   .withFunctionName("ss")
                .withPayload("{\n" +
                        "  \"key1\": \"value1\",\n" +
                        "  \"key2\": \"value2\",\n" +
                        "  \"key3\": \"value3\"\n" +
                        "}"); // optional
//               .withPayload("{\n" +
//                       "  \"1\": \"10\",\n" +
//                       "  \"2\": \"20\",\n" +
//                       "  \"3\": \"30\"\n" +
//                       "}");

// (5) Invoke the function and capture response
        InvokeResult result = client.invoke(req);
        String ans = new String(result.getPayload().array(), StandardCharsets.UTF_8);
        System.out.println(ans);


    }
}