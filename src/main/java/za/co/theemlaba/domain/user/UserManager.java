package za.co.theemlaba.domain.user;

import com.fasterxml.jackson.databind.JsonNode;

import za.co.theemlaba.domain.user.register.RegistrationForm;
import za.co.theemlaba.json.JsonHandler;
import za.co.theemlaba.domain.response.*;

/*
 * This class handles user registration and login operations.
 */
public class UserManager {
    private RegistrationForm registrationForm;
    
    public UserManager() {
        setLogin();
    }

    private void setLogin () {
        this.registrationForm = new RegistrationForm();
    }

    public String handleRegister(String receivedData) {
        BasicResponse errorResponse = new BasicResponse("Data found: " + receivedData);
        return JsonHandler.serializeResponse(errorResponse);
    }

    public String handleLogin(String receivedData) {
        BasicResponse errorResponse = new BasicResponse("Data found: " + receivedData);
        return JsonHandler.serializeResponse(errorResponse);
    }

    public boolean isValidFormat (String receivedData) {
        JsonNode requestJson;
        try {
            System.out.println(receivedData);
            requestJson = JsonHandler.deserializeJsonString(receivedData);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
