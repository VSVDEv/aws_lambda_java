package helloworld;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;


/**
 * Handler for requests to Lambda function.
 */
public class App {

    
	private Double instanceVar = 7.33;
	private static Double staticVar = 8.22;


   public App(){
        System.out.println("*****************Constructor*******************************");
    }

    static {
		System.out.println("*****************Static Block*******************************");
	}
    
    public void handlerWithTimeout (Object input, Context context) throws InterruptedException {
        while(true) {
        Thread.sleep(100);
        System.out.println("Context.getRemainingTimeInMillis() : " +
        context.getRemainingTimeInMillis());
        }
        }

        public void handleVariables() throws InterruptedException {
            //Thread.sleep(5000);
            int localVar = 777;
            System.out.println("Instance: "+instanceVar + " Static Variable: "
            +staticVar + " localVariable " + localVar);
        }

        public void envHandler(Context context) {
		
		System.out.println(System.getenv("restapiurl"));
		System.out.println(context.getAwsRequestId());
		System.out.println(context.getFunctionName());
		System.out.println(context.getRemainingTimeInMillis());
		System.out.println(context.getMemoryLimitInMB());
		System.out.println(context.getLogGroupName());

        String databaseUrl = System.getenv("DATABASE_URL");
        if (databaseUrl == null || databaseUrl.isEmpty())
        System.out.println("DATABASE_URL is not set");
        else
        System.out.println("DATABASE_URL is set to: " + databaseUrl);
        }
	}

   

