package za.co.theemlaba.domain.models;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import java.io.*;
import java.nio.file.*;
import io.github.cdimascio.dotenv.Dotenv;
import kong.unirest.json.JSONObject;

public class Llama3Request {
    
    public String generateCVFromLlama (String existingResume, String jobDescription) {
        Dotenv dotenv = loadEnvObject();
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
        String content = extractResponseString(response);
        return content;
    }

    public Dotenv loadEnvObject() {
        try {
            return Dotenv.configure().load();
        } catch (Exception e1) {
            try {
                return Dotenv.configure().directory("/app").load();
            } catch (Exception e2) {
                try {
                    return Dotenv.configure().directory("/etc/secrets").load();
                } catch (Exception e3) {
                    throw new IllegalStateException("Could not load environment variables from both the root, /app and /etc/secrets", e2);
                }
            }
        }
    }
    

    public String generateCoverLetterFromLlama (String existingResume, String coverLetter, boolean includesDescription) {
        Dotenv dotenv = loadEnvObject();
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
        String content = extractResponseString(response);
        
        return content;
    }

    public String extractResponseString (HttpResponse<JsonNode> response) {
        JsonNode jsonObject = response.getBody();
        JSONObject choicesObject = jsonObject.getObject()
                                             .getJSONArray("choices")
                                             .getJSONObject(0)
                                             .getJSONObject("message");
        String content = choicesObject.getString("content");
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

