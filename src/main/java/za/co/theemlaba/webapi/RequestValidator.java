package za.co.theemlaba.webapi;

import java.util.Map;
import io.javalin.http.Context;

/*
 * This class provides methods to validate incoming requests.
 */
public class RequestValidator {
    
    protected static boolean validateRegister(Context ctx) {
        String password = ctx.formParam("password");
        String email = ctx.formParam("email");

        // Validate the form data (basic example)
        if (password == null || password.isEmpty() ||
            email == null || email.isEmpty()) {
            return false;
        }
        
        return true;
    }
}
