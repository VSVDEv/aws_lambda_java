package org.example;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;

import java.nio.charset.StandardCharsets;

public class Localstack {
    private static AmazonS3 s3;


    public static void main(String[] args) {
        System.out.println("Hello World!");

        String accesskey = "test";
        String secret = "test";
        String endpoint = "s3.localhost.localstack.cloud:4566";


        s3 = AmazonS3ClientBuilder.standard()
                // .withCredentials(new ProfileCredentialsProvider())
                .withCredentials((new AWSStaticCredentialsProvider(new BasicAWSCredentials(accesskey,secret))))
                //   .withRegion(Regions.US_EAST_1)
                //.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:4566", "us-east-1"))
               // works with both Path-style requests and Virtual-hosted–style requests
                // https://s3.region-code.amazonaws.com/bucket-name/key-name
                // https://bucket-name.s3.region-code.amazonaws.com/key-name

                // https://localhost:4566/bucket-name/key-name
                // https://bucket-name.localhost:4566/key-name
                 //  GET https://svdev.s3.localhost.localstack.cloud:4566/test2.txt
                // GET https://s3.localhost.localstack.cloud:4566 /svdev/test2.txt
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, "us-east-1"))
                .withPathStyleAccessEnabled(true)
                .build();


        // GET http://sample.localhost:4566/2.txt  application/octet-stream


       // String bucketName = "sample";
        String bucketName = "vsvdevsource";
        //  String fileName = "err.jpeg";
        String fileName = "test2.txt";

        String result;

        try {
            S3Object obj = s3.getObject(bucketName, fileName);
            byte[] bytes = obj.getObjectContent().readAllBytes();

            result = new String(bytes, StandardCharsets.UTF_8);

        } catch (Exception e) {

            System.out.println("Exception here" + e);

            result = "";
        }


        System.out.println(result);



    }
}
