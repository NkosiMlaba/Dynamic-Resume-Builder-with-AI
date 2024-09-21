package za.co.theemlaba.database;

// A testing class, to test new features individually
public class MainDatabase {
    static UserManager userLoginManager = new UserManager();

    public static void main(String[] args) {
        
        System.out.println(userLoginManager.isExistingUser("thembani@gmail.com"));
        
        userLoginManager.storeUser("Thembani", "Mthembu", "thembani@gmail.com", "password123");
        // System.out.println(userLoginManager.fetchPassword("thembani@gmail.com"));
        System.out.println("Password matches? : " + userLoginManager.fetchPassword("thembani@gmail.com").equals("password123"));

        String userData = "Resume Data found here";
        userData = cleanData(userData);
        userLoginManager.createEntries("thembani@gmail.com", ".docx");

        userLoginManager.updateUserResume("thembani@gmail.com", userData);
        System.out.println(userLoginManager.fetchUserResume("thembani@gmail.com"));
        
        System.out.println(userLoginManager.hasExistingJobDescription("thembani@gmail.com"));
        userLoginManager.updateUserJobDescription("thembani@gmail.com", "software developer");
        System.out.println(userLoginManager.fetchUserJobDescription("thembani@gmail.com"));
        // userLoginManager.resetUserData("thembani@gmail.com");
        // userLoginManager.deleteUser("thembani@gmail.com"); // Only deletes on the Users table
        
        System.out.println(userLoginManager.fetchUserDocTypePreference("thembani@gmail.com"));

        userLoginManager.updateUserDocTypePreference("thembani@gmail.com", ".pdf");
        System.out.println(userLoginManager.fetchUserDocTypePreference("thembani@gmail.com"));

        
        userLoginManager.deleteAllUserData();

        // String data = userLoginManager.fetchUserData("thembani@gmail.com");
        // System.out.println(data);

    }

    public static String cleanData (String data) {
    data = data.replaceAll("\\n{2,}", "\n");
    data = data.replace("\"", "\\\"")
                .replace("\\", "\\\\")
                .replace("\n", "\\n")
                .replace("\r", "");
    
    return data;
    }
}
