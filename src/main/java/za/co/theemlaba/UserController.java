package za.co.theemlaba;

import java.util.HashMap;
import java.util.Map;

import za.co.theemlaba.database.*;
import za.co.theemlaba.domain.models.*;
import za.co.theemlaba.domain.resumeGenerator.*;

public class UserController {
    private UserManager database = new UserManager();
    private Llama3Request request = new Llama3Request();
    private GenerateResume generator = new GenerateResume();


    public UserController() {
        
    }


    /**
     * Handles a command by creating a new command object, executing it, and returning the response as a JSON string.
     *
     * @param request The command to be executed.
     * @return The response to the command as a JSON string.
     */
    public String handleCommand(String request) {
        String responseJsonString = "";
        try {
            return responseJsonString;
        } catch (IllegalArgumentException e) {
            return responseJsonString;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String handleStoreResumeData(Map<String, String> receivedData) {
        try {
            String resume = receivedData.get("resume");
            String email = receivedData.get("email");
            database.updateUserResume(email, resume);
            return email;
        } catch (Exception e) {
            return null;
        }
    }

    public String handleLogin(String receivedData) {
        return null;
    }

    public String authenticateUser(Map<String, String> receivedData) {
        try {
            if (database.isExistingUser(receivedData.get("email"))) {
                String password = database.fetchPassword(receivedData.get("email"));
                if (password.equals(receivedData.get("password"))) {
                    return receivedData.get("email");
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String registerUser (Map<String, String> receivedData) {
        try {
            if (!receivedData.get("password").equals(receivedData.get("confirmpassword"))) {
                return null;
            } else if (database.isExistingUser(receivedData.get("email"))) {
                return null;
            } else {
                //TODO: store user information
                database.storeUser(receivedData.get("firstname"), receivedData.get("lastname"), receivedData.get("email"), receivedData.get("password"));
                database.createEntries(receivedData.get("email"));
                return receivedData.get("email");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String generateResumeAsString (String existingResume, String jobDescription) {
        return request.generateCVFromLlama(existingResume, jobDescription);
    }

    public void generateResumeAsDocument (String email, String stringCV) {
        generator.generateCV(email, stringCV);
    }

    public String handleJobDescription (Map<String, String> receivedData) {
        try {
            String jobDescription = receivedData.get("jobdescription");
            jobDescription = cleanData(jobDescription);
            String email = receivedData.get("email");
            String existingResume = database.fetchUserResume(email);
            String stringCV = generateResumeAsString(existingResume, jobDescription);
            generateResumeAsDocument(email, stringCV);
            database.updateUserJobDescription(email, jobDescription);
            return email;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String cleanData (String data) {
        data = data.replaceAll("\\n{2,}", "\n");
        data = data.replace("\"", "\\\"")
                    .replace("\\", "\\\\")
                    .replace("\n", "\\n")
                    .replace("\r", "");
        return data;
    }

    public String getCvFilePath(String email) {
        return "src/main/resources/resumes/" + email + "/resume.docx";
    }

    public String regenerateResume (String email) {
        String existingResume = database.fetchUserResume(email);
        String jobDescription = database.fetchUserJobDescription(email);
        String stringCV = generateResumeAsString(existingResume, jobDescription);
        generateResumeAsDocument(email, stringCV);
        return email;
    }

    public Map<String, Object> hasLastJobDescription (String email) {
        Map<String, Object> model = new HashMap<>();
        
        if (database.hasExistingJobDescription(email)) {
            String lastJobDescription = database.fetchUserJobDescription(email);
            model.put("hasLastJobDescription", lastJobDescription!= null);
            model.put("lastJobDescription", lastJobDescription);
        } else {
            model.put("hasLastJobDescription", false);
            model.put("lastJobDescription", null);
        }
        
        return model;
    }
    
}