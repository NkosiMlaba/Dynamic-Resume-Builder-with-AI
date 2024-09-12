package za.co.theemlaba;

import za.co.theemlaba.database.*;
import za.co.theemlaba.domain.*;

// A testing class, to test new features individually
public class Main {
    static UserLoginManager userLoginManager = new UserLoginManager();

    public static void main(String[] args) {
        userLoginManager.storeUser("Thembani", "Mthembu", "thembani@gmail.com", "password123");
        System.out.println(userLoginManager.fetchPassword("thembani@gmail.com"));
        System.out.println(userLoginManager.fetchPassword("thembani@gmail.com").equals("password123"));

        userLoginManager.storeUserData("thembani@gmail.com", "is a good boy");

        userLoginManager.resetUserData("thembani@gmail.com");
        // userLoginManager.deleteAllUserData();





    }
}
