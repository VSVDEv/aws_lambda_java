package helloworld;

import java.util.List;
import java.util.Map;


/**
 * Handler for requests to Lambda function.
 */
public class App {

    public int handleInt(int input) {
        return input * 10;
    }

    public double handleDouble(double input) {
        return input + 10;
    }


    public Member handleObject(Member input) {

        return new Member(input.getId() + 10L, input.getFullName() + "modified", input.getBalance());
    }


    public List<String> handleList(List<String> input) {
        input.replaceAll(item -> item + "modified by Lambda");

        return input;
    }


    public Map<String, Integer> handleMap(Map<String, Integer> input) {
        input.replaceAll((key, value) -> value + 100);
        return input;

    }

    public boolean handleBoolean(boolean input) {
        return !input;
    }
}
