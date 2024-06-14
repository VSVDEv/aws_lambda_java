package helloworld;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handler for requests to Lambda function.
 */
public class App {

    
    public String handleRequest(InputStream inputStream, Context context) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(inputStream);
            String jsonString = objectMapper.writeValueAsString(jsonNode);
            System.out.println("Input JSON: " + jsonString);

            // Determine the type of the JSON data
            String dataType = determineJsonType(jsonNode);
            System.out.println("Data type: " + dataType);
            String result = processJsonData(jsonNode, dataType);
            System.out.println("Processed data: " + result);

            return "Success";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error";
        }
    }

    private String determineJsonType(JsonNode jsonNode) {
        if (jsonNode.isArray()) {
            return "Array";
        } else if (jsonNode.isObject()) {
            if (isMap(jsonNode)) {
                return "Map";
            } else {
                return "Object";
            }
        } else if (jsonNode.isTextual()) {
            return "String";
        } else if (jsonNode.isNumber()) {
            if (jsonNode.isInt() || jsonNode.isLong()) {
                return "Integer";
            } else if (jsonNode.isFloat() || jsonNode.isDouble()) {
                return "Float";
            }
        } else if (jsonNode.isBoolean()) {
            return "Boolean";
        }
        return "Unknown";
    }

    private boolean isMap(JsonNode jsonNode) {
        for (JsonNode node : jsonNode) {
            if (node.isObject() || node.isArray() || node.isNumber() || node.isTextual() || node.isBoolean()) {
                return true;
            }
        }
        return false;
    }

    private String processJsonData(JsonNode jsonNode, String dataType) {
        switch (dataType) {
            case "Array":
                // Process array
                List<String> arrayList = new ArrayList<>();
                for (JsonNode node : jsonNode) {
                    arrayList.add(node.asText());
                }
                return "Array data: " + handleList(arrayList).toString();
            case "Object":
                // Process object
                Map<String, Object> objectMap = new HashMap<>();
                jsonNode.fields().forEachRemaining(entry -> objectMap.put(entry.getKey(), entry.getValue().asText()));
                System.out.println("**************************************THE OBJECT**********************************");
                System.out.println(objectMap);
                String processedObject = handleObject(objectMap);
                System.out.println("Processed object: " + processedObject);
                return "Object data: " + processedObject;
            case "Map":
                // Process map
                Map<String, Integer> map = new HashMap<>();
                jsonNode.fields().forEachRemaining(entry -> map.put(entry.getKey(), entry.getValue().asInt()));
                return "Map data: " + handleMap(map).toString();
            case "String":
                // Process string
                return "String data: " + jsonNode.asText();
            case "Integer":
                // Process integer
                return "Integer data: " + handleInt(jsonNode.asInt());
            case "Float":
                // Process float
                return "Float data: " + handleDouble(jsonNode.asDouble());
            case "Boolean":
                // Process boolean
                return "Boolean data: " + handleBoolean(jsonNode.asBoolean());
            default:
                return "Unknown data type";
        }
    }

    public int handleInt(int input) {
        return input * 10;
    }

    public double handleDouble(double input) {
        return input + 10;
    }

    public String handleObject(Map<String, Object> input) {
        if (input.containsKey("id")) {
            int id = (int) input.get("id") + 10;
            input.put("id", id);
        }

        StringBuilder result = new StringBuilder();
        result.append("{ ");
        for (Map.Entry<String, Object> entry : input.entrySet()) {
            result.append(entry.getKey()).append(": ").append(entry.getValue()).append(", ");
        }
        result.setLength(result.length() - 2);  // Remove the last comma and space
        result.append(" }");
        return result.toString();
    }

    public List<String> handleList(List<String> input) {
        input.replaceAll(item -> item + " modified by Lambda");
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
