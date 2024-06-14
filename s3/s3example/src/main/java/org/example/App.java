package org.example;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Hello world!
 */
public class App {

    private static AmazonS3 s3;


    public static void main(String[] args) throws IOException {
        System.out.println("Hello World!");

       // String accesskey = "4";
        String accesskey = "3";
       // String secret = "1";
        String secret = "2";
        String endpoint = "http://localhost:8080";


        s3 = AmazonS3ClientBuilder.standard()
                .withCredentials((new AWSStaticCredentialsProvider(new BasicAWSCredentials(accesskey,secret))))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, "us-east-1"))
                .withPathStyleAccessEnabled(true)
                .build();

        // works http://localhost:8080/vsvdevsource/2.txt
        // not implemented GET http://vsvdevsource.localhost:8080/2.txt  application/octet-stream


        String bucketName = "vsvdevsource";
      // String fileName = "err.jpeg";
       String fileName = "test2.txt";

        String result;

//        File file = null;
//
//        try {
//            S3Object obj = s3.getObject(bucketName, fileName);
//            byte[] bytes = obj.getObjectContent().readAllBytes();
//
//            result = new String(bytes, StandardCharsets.UTF_8);
//
//            Path path = Paths.get("/test2.txt");
//
//            // string -> bytes
//            Path write = Files.write(path, result.getBytes(StandardCharsets.UTF_8));
//
//            file = write.toFile();
//
//
//        } catch (Exception e) {
//
//            System.out.println("Exception here" + e);
//
//            result = "";
//        }
//
//
//        System.out.println(result);


//        try {
//            s3.putObject("vsvdevdestination", "test2.txt", new File("test2.txt"));
//        } catch (Exception e) {
//            System.out.println("Exception here" + e);
//        }

    // File f = new File("err.jpg");
    File f = new File("test2.txt");
  //  File f = new File("t.yaml");
       //
     //   Path path =  Paths.get("test2.txt");
     //   String content = Files.lines(path)
           //     .collect(Collectors.joining(System.lineSeparator()));






      // PutObjectRequest request = new PutObjectRequest("vsvdevdestination", "err.jpg",f);
     //  PutObjectRequest request = new PutObjectRequest("vsvdevdestination", "t.yaml",f);
    PutObjectRequest request = new PutObjectRequest("vsvdevdestination", "test2.txt", f);
        //PutObjectRequest request = new PutObjectRequest("vsvdevdestination", "test2.txt", String.valueOf(new ByteArrayInputStream(result.getBytes())));
        s3.putObject(request);

    }
}
