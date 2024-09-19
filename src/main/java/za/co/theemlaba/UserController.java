package za.co.theemlaba;

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

    public String handleRegister(String receivedData) {
        return null;
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
                // database.storeUser(receivedData.get("firstname"), receivedData.get("lastname"), receivedData.get("email"), receivedData.get("password"));
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


    
}