package za.co.theemlaba.domain.models;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import java.io.*;
import java.nio.file.*;
import io.github.cdimascio.dotenv.Dotenv;
import kong.unirest.json.JSONObject;

public class Llama3Request {
    static String role = "Software Developer";
    static String Cv = readFile("input.txt").trim().replace("\n", "").replace("\\", "");

    public static String main(String[] args) {
        role = args.length > 1 ? args[1] : "Software developer";
        Llama3Request script = new Llama3Request();
        String content = script.generateCVFromLlama(Cv, role);
        return content;
    }
    
    public String generateCVFromLlama (String existingResume, String jobDescription) {
        Dotenv dotenv = Dotenv.configure().load();
        String apiKey = dotenv.get("GROQ_API_KEY");
        String prompt1 = dotenv.get("Ins1");
        String prompt2 = dotenv.get("Ins2");
        String prompt3 = dotenv.get("Ins3");
        String beforeRolePrompt = dotenv.get("Bfr");

        if (apiKey == null) {
            throw new IllegalStateException("API_KEY environment variable not set");
        }

        String prompt = beforeRolePrompt + jobDescription + " using this cv '" + existingResume + "',"
                + prompt1 + prompt2 + prompt3;
        HttpResponse<JsonNode> response = sendGroqRequest(makeJsonString(prompt), apiKey);

        System.out.println(response.getStatus());
        JsonNode jsonObject = response.getBody();
        System.out.println(jsonObject.toString());

        JSONObject choicesObject = jsonObject.getObject()
                                             .getJSONArray("choices")
                                             .getJSONObject(0)
                                             .getJSONObject("message");
        String content = choicesObject.getString("content");
        System.out.println(content);
        return content;
    }

    public String generateCoverLetterFromLlama (String existingResume, String coverLetter, boolean includesDescription) {
        Dotenv dotenv = Dotenv.configure().load();
        String apiKey = dotenv.get("GROQ_API_KEY");

        if (apiKey == null) {
            throw new IllegalStateException("API_KEY environment variable not set");
        }

        String prompt = null;
        if (includesDescription) {
            prompt = "Generate cover letter for this job description: '" + coverLetter + "' using this cv '" + existingResume + "'";
        } else {
            prompt = "Generate cover letter using this cv '" + existingResume + "'";
        }
        
        HttpResponse<JsonNode> response = sendGroqRequest(makeJsonString(prompt), apiKey);

        System.out.println(response.getStatus());
        JsonNode jsonObject = response.getBody();
        System.out.println(jsonObject.toString());

        JSONObject choicesObject = jsonObject.getObject()
                                             .getJSONArray("choices")
                                             .getJSONObject(0)
                                             .getJSONObject("message");
        String content = choicesObject.getString("content");
        System.out.println(content);
        return content;
    }

    public HttpResponse<JsonNode> sendGroqRequest(String launchString, String apiKey) {
        return Unirest.post("https://api.groq.com/openai/v1/chat/completions")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .body(launchString)
                .asJson();
    }

    String makeJsonString (String promptString) {
        return "{" +
                "\"messages\": [{" +
                "\"role\":\"user\"," +
                "\"content\":\"" + promptString + "\"" +"}]," +
                "\"model\": \"llama3-8b-8192\"" +
                "}";
    }

    public static String readFile(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

