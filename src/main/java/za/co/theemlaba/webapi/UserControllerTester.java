package za.co.theemlaba.webapi;
import java.util.HashMap;
import java.util.Map;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import java.io.*;
import java.nio.file.*;
import io.github.cdimascio.dotenv.Dotenv;
import kong.unirest.json.JSONObject;

import za.co.theemlaba.database.*;


public class UserControllerTester {
    public static void main(String[] args) {
        UserController controller = new UserController();
        UserManager database = new UserManager();
        
        // userMap.put("email", "thembani@gmail.com");
        // userMap.put("password", "password123");
        // userMap.put("firstname", "password123");
        // userMap.put("lastname", "password123");
        // userMap.put("confirmpassword", "password123");
        // testing login and password
        // System.out.println(controller.authenticateUser(userMap));

        // userMap.put("email", "kaya@gmail.com");
        // userMap.put("password", "password123");
        // userMap.put("firstname", "kaya");
        // userMap.put("lastname", "dladla");
        // userMap.put("confirmpassword", "password123");
        // new user all correct
        // System.out.println(controller.registerUser(userMap));

        // userMap.put("email", "kaya@gmail.com");
        // userMap.put("password", "password123");
        // userMap.put("firstname", "kaya");
        // userMap.put("lastname", "dladla");
        // userMap.put("confirmpassword", "password113");
        // new user incorrect password
        // System.out.println(controller.registerUser(userMap));

        // String role = args.length > 1 ? args[1] : "Software developer";
        // System.out.println(role);

        // String userCV = controller.generateResumeAsString("LucaShirlow@gmail.com", role);
        // controller.generateResumeAsDocument("LucaShirlow@gmail.com", userCV);

        // String userCL = controller.generateCoverLetterAsString("LucaShirlow@gmail.com", role, false);
        // controller.generateCoverLetterAsDocument("LucaShirlow@gmail.com", userCL);

        database.storeUser("firstname", "lastname", "LucaShirlow@gmail.com", "password");
        database.createEntries("LucaShirlow@gmail.com", ".docx");

        String userCV = readFile("input.txt").trim().replace("\n", "").replace("\\", "");
        Map<String, String>resumeMap = new HashMap<>();
        resumeMap.put("email", "LucaShirlow@gmail.com");
        resumeMap.put("resume", userCV);
        // System.out.println(userCV);
        controller.handleStoreResumeData(resumeMap);

        String existingResume = database.fetchUserResume("LucaShirlow@gmail.com");
        System.out.println(existingResume);

        Map<String, String> jobDescr = new HashMap<>();
        jobDescr.put("email", "LucaShirlow@gmail.com");
        jobDescr.put("jobdescription", "Software developer");
        controller.handleJobDescription(jobDescr);

        // database.deleteAllUserData();

        
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
