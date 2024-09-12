package za.co.theemlaba.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import za.co.theemlaba.domain.response.Response;

import java.io.File;
import java.io.IOException;

public class JsonHandler {

    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * This function serializes a Response object into a JSON string.
     *
     * @param response The Response object to be serialized.
     * @return A JSON string representation of the input Response object.
     *         Returns null if an error occurs during the serialization process.
     *
     * @throws IOException If an error occurs while writing the Response object to a JSON string.
     */
    public static String serializeResponse(Response response) {
        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonString;
    }
    
    /**
     * This function deserializes a JSON file into a JsonNode object.
     *
     * @param file The file containing the JSON data to be deserialized.
     * @return The deserialized {JsonNode} object.
     *         Returns null if an error occurs during the deserialization process.
     *
     * @throws IOException If an error occurs while reading the JSON file.
     */
    public static JsonNode deserializeJsonFile(File file) throws IOException {
        JsonNode jsonNode =  objectMapper.readTree(file);
        return jsonNode;
    }

    /**
     * This function deserializes a JSON string into a {@link JsonNode} object.
     *
     * @param jsonString The JSON string to be deserialized.
     * @return The deserialized {JsonNode} object.
     *         Returns {null} if an error occurs during the deserialization process.
     *
     * @throws IOException If an error occurs while reading the JSON string.
     */
    public static JsonNode deserializeJsonString(String jsonString) {
        JsonNode jsonNode = null;
        try{
            jsonNode =  objectMapper.readTree(jsonString);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return jsonNode;
    }

    /**
     * This function checks if a given string is a valid JSON representation.
     *
     * @param string The string to be checked.
     * @return A boolean value indicating whether the input string is a valid JSON.
     *         Returns {true} if the string is a valid JSON, {false} otherwise.
     *
     * @throws Exception If an error occurs during the JSON parsing process.
 */
    public static Boolean isJsonString(String string) {
        try {
            objectMapper.readTree(string);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
