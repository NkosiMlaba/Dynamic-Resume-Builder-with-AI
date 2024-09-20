package za.co.theemlaba.database;

import java.io.IOException;
import java.nio.file.*;

// A testing class, to test new features individually
public class MainDatabase {
    static UserManager userLoginManager = new UserManager();

    public static void main(String[] args) {
        
        // System.out.println(userLoginManager.isExistingUser("thembani@gmail.com"));
        
        userLoginManager.storeUser("Thembani", "Mthembu", "thembani@gmail.com", "password123");
        // System.out.println(userLoginManager.fetchPassword("thembani@gmail.com"));
        System.out.println("Password matches? : " + userLoginManager.fetchPassword("thembani@gmail.com").equals("password123"));

        String userData = "Resume Data found here";
        userData = cleanData(userData);
        userLoginManager.createResumesEntry("thembani@gmail.com");
        userLoginManager.createJobdescriptionsEntry("thembani@gmail.coHm");

        userLoginManager.updateUserResume("thembani@gmail.com", userData);
        System.out.println(userLoginManager.fetchUserResume("thembani@gmail.com"));
        
        // System.out.println(userLoginManager.isExistingUser("thembani@gmail.com"));
        // userLoginManager.resetUserData("thembani@gmail.com");
        // userLoginManager.deleteUser("thembani@gmail.com"); // Only deletes on the Users table
        userLoginManager.deleteAllUserData();

        // String data = userLoginManager.fetchUserData("thembani@gmail.com");
        // System.out.println(data);

    }

    private static String readFile(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String cleanData (String data) {
    data = data.replaceAll("\\n{2,}", "\n");

    // Escape problematic characters for JSON
    data = data.replace("\"", "\\\"")
                .replace("\\", "\\\\")
                .replace("\n", "\\n")
                .replace("\r", "");
    
    return data;
    }
}
