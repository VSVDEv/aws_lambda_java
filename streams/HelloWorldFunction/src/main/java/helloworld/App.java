package helloworld;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;


/**
 * Handler for requests to Lambda function.
 */
public class App {

    public void handleStream(InputStream inputStream, OutputStream outputStream)
    throws IOException {
    int letter;
    while((letter = inputStream.read()) != -1) {
    outputStream.write(Character.toUpperCase(letter));
     } 
 }


    public void handleScanner(InputStream is, OutputStream os) {
    Scanner s = new Scanner(is).useDelimiter("\\A");
    System.out.println(s.hasNext() ? s.next() : "No input detected");
    }

}
