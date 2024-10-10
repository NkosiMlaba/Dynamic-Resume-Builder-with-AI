package unittests;
import za.co.theemlaba.webapi.UserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {

    private UserController userController;

    @BeforeEach
    void setUp() {
        userController = new UserController();
    }

    @Test
    void testHandleStoreResumeData() {
        Map<String, String> inputData = new HashMap<>();
        inputData.put("resume", "Sample Resume");
        inputData.put("email", "test@example.com");

        String result = userController.handleStoreResumeData(inputData);
        assertEquals("test@example.com", result);
    }

    @Test
    void testAuthenticateUser_InvalidCredentials() {
        Map<String, String> inputData = new HashMap<>();
        inputData.put("email", "invalidUser@example.com");
        inputData.put("password", "wrongPassword");

        String result = userController.authenticateUser(inputData);
        assertNull(result);
    }

    @Test
    void testRegisterUser_PasswordMismatch() {
        Map<String, String> inputData = new HashMap<>();
        inputData.put("firstname", "John");
        inputData.put("lastname", "Doe");
        inputData.put("email", "newUser@example.com");
        inputData.put("password", "password1");
        inputData.put("confirmpassword", "password2");

        String result = userController.registerUser(inputData);
        assertNull(result);
    }

    @Test
    void testGenerateResumeAsString() {
        String existingResume = "Existing Resume";
        String jobDescription = "Job Description";

        String result = userController.generateResumeAsString(existingResume, jobDescription);
        assertNotNull(result);
    }

    @Test
    void testUpdatePreferredFormat() {
        String email = "test@example.com";
        String formatData = "pdf";

        String result = userController.updatePreferredFormat(email, formatData);
        assertNull(result);
    }

}
