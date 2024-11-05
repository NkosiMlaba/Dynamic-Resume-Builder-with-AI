package unittests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import za.co.theemlaba.database.UserManager;

public class DatabaseTest {
    private UserManager userManager;

    @BeforeEach
    public void setUp() {
        userManager = new UserManager();
        userManager.setDatabaseName("TestDatabase1.db");
    }

    @AfterEach
    public void tearDown() {
        userManager.deleteAllUserData();
    }

    @Test
    public void testIsExistingUserForUserNotCreated() {
        assertFalse(userManager.isExistingUser("thembani@gmail.com"));
    }

    @Test
    public void testStoreUser() {
        userManager.storeUser("Thembani", "Mthembu", "thembani@gmail.com", "password123");
        assertTrue(userManager.isExistingUser("thembani@gmail.com"));
    }

    @Test
    public void testUserPasswordMatch () {
        userManager.storeUser("Thembani", "Mthembu", "thembani@gmail.com", "password123");
        assertTrue(userManager.fetchPassword("thembani@gmail.com").equals("password123"));
    }

    @Test
    public void testUserPassordDoNotMatch () {
        userManager.storeUser("Thembani", "Mthembu", "thembani@gmail.com", "password123");
        assertFalse(userManager.fetchPassword("thembani@gmail.com").equals("password456"));
    }

    @Test
    public void testDefaultUserResumeIsNull () {
        userManager.storeUser("Thembani", "Mthembu", "thembani@gmail.com", "password123");
        assertNull(userManager.fetchUserResume("thembani@gmail.com"));
    }

    @Test
    public void testDefaultUserJobDescriptionIsNull () {
        userManager.storeUser("Thembani", "Mthembu", "thembani@gmail.com", "password123");
        assertNull(userManager.fetchUserJobDescription("thembani@gmail.com"));
    }

    @Test
    public void testResumeCanBeStoredAndFetched () {
        userManager.storeUser("Thembani", "Mthembu", "thembani@gmail.com", "password123");
        String userData = "ResumeData";
        userData = userManager.cleanData(userData);
        userManager.createEntries("thembani@gmail.com", ".docx");
        userManager.updateUserResume("thembani@gmail.com", userData);
        assertEquals("ResumeData", userManager.fetchUserResume("thembani@gmail.com"));
    }

    @Test
    public void testJobDescriptionCanBeStoredAndFetched () {
        userManager.storeUser("Thembani", "Mthembu", "thembani@gmail.com", "password123");
        userManager.createEntries("thembani@gmail.com", ".docx");
        String userJobDescription = "Software Developer";
        userManager.updateUserJobDescription("thembani@gmail.com", userJobDescription);
        assertEquals("Software Developer", userManager.fetchUserJobDescription("thembani@gmail.com"));
    }

    @Test
    public void testDocumentFormatCanBeUpdatedAndFetched () {
        userManager.storeUser("Thembani", "Mthembu", "thembani@gmail.com", "password123");
        userManager.createEntries("thembani@gmail.com", ".docx");
        userManager.updateUserDocTypePreference("thembani@gmail.com", ".pdf");
        assertEquals(".pdf", userManager.fetchUserDocTypePreference("thembani@gmail.com"));
    }
}
