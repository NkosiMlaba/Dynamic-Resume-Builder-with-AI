package za.co.theemlaba.domain;

import za.co.theemlaba.database.*;

public class UserController {
    private UserLoginManager loginDatabase;

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
}