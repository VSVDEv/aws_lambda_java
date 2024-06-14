package vsvdev;


import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;


public class App3 implements RequestHandler<S3Event, String> {

     AmazonS3 s3 ;
    private static final String WORD_BREAK = "\\W+";




    public App3() {

    }

    @Override
    public String handleRequest(S3Event event, Context context) {

        String accesskey = "3";
        String secret = "2";
        String endpoint = "http://host.docker.internal:8080";
        s3 = AmazonS3ClientBuilder.standard()
                // .withCredentials(new ProfileCredentialsProvider())
                .withCredentials((new AWSStaticCredentialsProvider(new BasicAWSCredentials(accesskey,secret))))

                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, "us-east-1"))

                // comment if you want to use virtual host style
                //  like https://bucket-name.s3.region-code.amazonaws.com/key-name
                .withPathStyleAccessEnabled(true)

                // localstack  works with both Path-style requests and Virtual-hosted–style requests
                // https://s3.region-code.amazonaws.com/bucket-name/key-name
                // https://bucket-name.s3.region-code.amazonaws.com/key-name

                // https://localhost:4566/bucket-name/key-name
                // https://bucket-name.localhost:4566/key-name
                //  GET https://vsvdevsource.s3.localhost.localstack.cloud:4566/test2.txt
                // GET https://s3.localhost.localstack.cloud:4566/vsvdevsource/test2.txt

                .build();

		
		
        LambdaLogger logger = context.getLogger();

        S3EventNotification.S3EventNotificationRecord record = event.getRecords().get(0);
       // logger.log("*******************The source bucket env is: " + System.getenv("SOURCE_BUCKET"));

        String bucketName = record.getS3().getBucket().getName();
        logger.log("*******************The source bucket is: " + bucketName);
        String key = record.getS3().getObject().getKey();
        logger.log("*******************The key is: " + key);
        String fileName = record.getS3().getObject().getKey();
        logger.log("*******************The filename is: " + fileName);

        String stringToRemove = "/key";
        
        // Using replace method
        String finalKey = fileName.replace(stringToRemove, "");
        logger.log("*******************The final name is: " + finalKey);




        String input = getFileFromS3(finalKey, bucketName);
        logger.log("*******************The input is: " + input);
        Set<String> uniqueWords;
        if (input.length() > 0) {
            uniqueWords = processString(input);
        } else {
            logger.log("Error: Input is empty ");
            throw new RuntimeException("Input is empty");
        }



        StringBuilder processedContent = new StringBuilder();
        for (String word : uniqueWords) {
            processedContent.append(word).append("\n");
        }



        String processedFileName = generateUniqueFileName(finalKey);
        logger.log("******************filename is : " + processedFileName);
        logger.log("******************content is : " + processedContent);
        logger.log("******************Destination bucket is : " + System.getenv("DESTINATION_BUCKET"));

        try {
            writeToS3(processedContent, processedFileName);
            logger.log("Processed file uploaded successfully: " + processedFileName);

            return processedFileName;
        } catch (Exception e) {
            logger.log("Processed file didn't uploaded successfully: " + processedFileName);
            logger.log("The reason is: " + e.getMessage());
            return "";
        }

    }

    public void writeToS3(StringBuilder processedContent, String processedFileName) throws Exception{

        
            ByteArrayInputStream output = new ByteArrayInputStream(processedContent.toString().getBytes(StandardCharsets.UTF_8));
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(processedContent.length());
            String outputBucketName = "vsvdevdestination";
            PutObjectRequest putObjectRequest = new PutObjectRequest(outputBucketName, processedFileName, output, metadata);
            s3.putObject(putObjectRequest);
           
    
    }


    private String getFileFromS3(String fileName, String bucketName) {

        /// String bucketName = System.getenv("SOURCE_BUCKET");

        try {
            S3Object obj = s3.getObject(bucketName, fileName);
            byte[] bytes = obj.getObjectContent().readAllBytes();

            return new String(bytes, StandardCharsets.UTF_8);

        } catch (Exception e) {

            System.out.println("Exception here" + e);

            return "";
        }

    }

    private static Set<String> processString(String content) {
        return Arrays.stream(content.split(WORD_BREAK)).collect(Collectors.toSet());
    }

    private static String generateUniqueFileName(String fileName) {
        String[] parts = fileName.split("\\.");
        String extension = parts.length > 1 ? "." + parts[parts.length - 1] : "";
        return "processed-" + fileName.replace(extension, "") + "-" + System.currentTimeMillis() + extension;
    }
}
