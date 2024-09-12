package za.co.theemlaba.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.File;

public class UserLoginManager {
    private String URL;
    private static final String DATABASE_DIR = "database";

    public UserLoginManager() {
        setDatabaseName("login.db");
        initialiseDatabase();
        createEmailIndex();
    }

    /**
     * Sets the name of the database file and updates the URL for database connections.
     * Ensures that the "database" directory exists before setting the database name.
     *
     * @param databaseName The name of the database file to be used. The file will be
     *                     located in the "database" directory. If the file doesn't
     *                     exist, it will be created when a connection is first
     *                     established.
     */
    public void setDatabaseName(String databaseName) {
        createDatabaseDirectory(); // Ensure the directory exists
        this.URL = "jdbc:sqlite:" + DATABASE_DIR + "/" + databaseName;
    }

    /**
     * Creates the "database" directory if it does not already exist.
     */
    private void createDatabaseDirectory() {
        File directory = new File(DATABASE_DIR);
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                System.out.println("Database directory created.");
            } else {
                System.out.println("Failed to create database directory.");
            }
        }
    }

    /**
     * Initializes the database by creating the necessary tables.
     */
    public void initialiseDatabase() {
        createUserParameters();
    }

    /**
     * Creates the Users table if it does not already exist.
     */
    public void createUserParameters() {
        String sql = "CREATE TABLE IF NOT EXISTS Users (\n"
                + " firstname TEXT NOT NULL,\n"
                + " lastname TEXT NOT NULL,\n"
                + " email TEXT NOT NULL,\n"
                + " password TEXT NOT NULL,\n"
                + " information TEXT"
                + ");";
        
        try (Connection conn = DriverManager.getConnection(URL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Database and Users table created.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Stores the user in the database.
     * @param userName The user's first name.
     * @param userLastName The user's last name.
     * @param userEmail The user's email address.
     * @param userPassword The user's password.
     */
    public void storeUser (String userName, String userLastName, String userEmail, String userPassword) {
        String insertQuerry = "INSERT INTO Users (firstname, lastname, email, password) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DriverManager.getConnection(URL);
            PreparedStatement statement = conn.prepareStatement(insertQuerry)) {
            
            statement.setString(1, userName);
            statement.setString(2, userLastName);
            statement.setString(3, userEmail);
            statement.setString(4, userPassword);
            statement.executeUpdate();

            System.out.println("User stored successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Stores the cv of the user in the database.
     * @param userEmail The user's email address.
     * @param userData The user's password.
     */
    public void storeUserData (String userEmail, String userData) {
        String insertQuerry = "UPDATE Users SET information = ? WHERE email = ?";
        
        try (Connection conn = DriverManager.getConnection(URL);
            PreparedStatement statement = conn.prepareStatement(insertQuerry)) {
            
            
            statement.setString(1, userData);
            statement.setString(2, userEmail);
            statement.executeUpdate();

            System.out.println("User data stored successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes all data from the Users table.
     */
    public void deleteUserData () {
        try (Connection conn = DriverManager.getConnection(URL)) {
            conn.createStatement().execute("DROP TABLE IF EXISTS Users");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fetches the password for a given email address.
     * @param email The email address to search for.
     * @return The password associated with the email address, or null if not found.
     */
    public String fetchPassword (String email) {
        String selectQuery = "SELECT password FROM Users WHERE email = ?";
        try (Connection conn = DriverManager.getConnection(URL);
            PreparedStatement statement = conn.prepareStatement(selectQuery)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.getString("password");
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Create an index on the email column in the Users table.
     */
    public void createEmailIndex () {
        String sql = "CREATE INDEX IF NOT EXISTS email_index ON Users(email)";
        try (Connection conn = DriverManager.getConnection(URL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Email index created.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
