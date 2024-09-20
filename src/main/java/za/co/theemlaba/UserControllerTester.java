package za.co.theemlaba;
import java.util.Map;

import java.util.HashMap;

public class UserControllerTester {
    public static void main(String[] args) {
        UserController controller = new UserController();
        Map<String, String> userMap = new HashMap<>();
        
        userMap.put("email", "thembani@gmail.com");
        userMap.put("password", "password123");
        userMap.put("firstname", "password123");
        userMap.put("lastname", "password123");
        userMap.put("confirmpassword", "password123");
        // testing login and password
        System.out.println(controller.authenticateUser(userMap));

        userMap.put("email", "kaya@gmail.com");
        userMap.put("password", "password123");
        userMap.put("firstname", "kaya");
        userMap.put("lastname", "dladla");
        userMap.put("confirmpassword", "password123");
        // new user all correct
        System.out.println(controller.registerUser(userMap));

        userMap.put("email", "kaya@gmail.com");
        userMap.put("password", "password123");
        userMap.put("firstname", "kaya");
        userMap.put("lastname", "dladla");
        userMap.put("confirmpassword", "password113");
        // new user incorrect password
        System.out.println(controller.registerUser(userMap));

        String role = args.length > 1 ? args[1] : "Software developer";
        System.out.println(role);


        // String userCV = controller.generateResumeAsString("thembani@gmail.com", role);
        // controller.generateResumeAsDocument("thembani@gmail.com", userCV);

        Map<String, String> jobDescr = new HashMap<>();
        jobDescr.put("email", "thembani@gmail.com");
        jobDescr.put("jobdescription", "Software developer");
        controller.handleJobDescription(jobDescr);
    }
}
