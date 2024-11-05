package za.co.theemlaba.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.File;

public class UserManager {
    private String URL;
    private static final String DATABASE_DIR = "src/main/resources/database";

    public UserManager() {
        setDatabaseName("Database.db");
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
        this.URL = "jdbc:sqlite:" + DATABASE_DIR + File.separator + databaseName;
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
        createUserTable();
        createUserResumesTable();
        createUserJobDescriptionsTable();
        createUserPreferencesTable();
    }

    /**
     * Creates the Users table if it does not already exist.
     */
    public void createUserTable() {
        String sql = "CREATE TABLE IF NOT EXISTS Users (\n"
                + " firstname TEXT NOT NULL,\n"
                + " lastname TEXT NOT NULL,\n"
                + " email TEXT NOT NULL PRIMARY KEY,\n"
                + " password TEXT NOT NULL\n"
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
     * Creates the Resumes table if it does not already exist.
     */
    public void createUserResumesTable() {
        String sql = "CREATE TABLE IF NOT EXISTS Resumes (\n"
                + " resume_email TEXT NOT NULL,\n"
                + " resume TEXT,\n"
                + " FOREIGN KEY (resume_email) REFERENCES Users(email) ON DELETE CASCADE\n"
                + ");";
        
        try (Connection conn = DriverManager.getConnection(URL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Resumes table created.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Creates the Jobdescriptions table if it does not already exist.
     */
    public void createUserJobDescriptionsTable() {
        String sql = "CREATE TABLE IF NOT EXISTS Jobdescriptions (\n"
                + " jobdescription_email TEXT NOT NULL,\n"
                + " jobdescription TEXT,\n"
                + " FOREIGN KEY (jobdescription_email) REFERENCES Users(email) ON DELETE CASCADE"
                + ");";
        
        try (Connection conn = DriverManager.getConnection(URL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Jobdescription table created.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Creates the Preferences table if it does not already exist.
     */
    public void createUserPreferencesTable() {
        String sql = "CREATE TABLE IF NOT EXISTS Preferences (\n"
                + " preference_email TEXT NOT NULL,\n"
                + " format TEXT NOT NULL,\n"
                + " FOREIGN KEY (preference_email) REFERENCES Users(email) ON DELETE CASCADE\n"
                + ");";
        
        try (Connection conn = DriverManager.getConnection(URL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Preferences table created.");
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
     * Create the entry of the user in the resumes table.
     * @param userEmail The user's email address.
     */
    public void createResumesEntry (String userEmail) {
        String insertQuerry = "INSERT INTO Resumes (resume_email) VALUES (?)";
        
        try (Connection conn = DriverManager.getConnection(URL);
            PreparedStatement statement = conn.prepareStatement(insertQuerry)) {
            statement.setString(1, userEmail);
            statement.executeUpdate();
            System.out.println("User resume placeholder stored successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the entry of the user in the Job descriptions table.
     * @param userEmail The user's email address.
     */
    public void createJobdescriptionsEntry (String userEmail) {
        String insertQuerry = "INSERT INTO Jobdescriptions (jobdescription_email) VALUES (?)";
        
        try (Connection conn = DriverManager.getConnection(URL);
            PreparedStatement statement = conn.prepareStatement(insertQuerry)) {
            statement.setString(1, userEmail);
            statement.executeUpdate();
            System.out.println("User Jobdescriptions placeholder stored successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the entry of the user in the Preferences table.
     * @param userEmail The user's email address.
     */
    public void createPreferencesEntry (String userEmail, String format) {
        String insertQuerry = "INSERT INTO Preferences (preference_email, format) VALUES (?, ?)";
        
        try (Connection conn = DriverManager.getConnection(URL);
            PreparedStatement statement = conn.prepareStatement(insertQuerry)) {
            statement.setString(1, userEmail);
            statement.setString(2, format);
            statement.executeUpdate();
            System.out.println("User Preferences stored successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Fetches the preferred document format of the user in the database.
     * @param userEmail The user's email address.
     */
    public String fetchUserDocTypePreference (String userEmail) {
        String selectQuery = "SELECT format FROM Preferences WHERE preference_email = ?";
        
        try (Connection conn = DriverManager.getConnection(URL);
            PreparedStatement statement = conn.prepareStatement(selectQuery)) {
            statement.setString(1, userEmail);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.getString("format");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Fetches the resume of the user in the database.
     * @param userEmail The user's email address.
     */
    public String fetchUserResume (String userEmail) {
        String selectQuery = "SELECT resume FROM Resumes WHERE resume_email = ?";
        
        try (Connection conn = DriverManager.getConnection(URL);
            PreparedStatement statement = conn.prepareStatement(selectQuery)) {
            statement.setString(1, userEmail);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.getString("resume");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Fetches the last used job description of the user in the database.
     * @param userEmail The user's email address.
     */
    public String fetchUserJobDescription (String userEmail) {
        String selectQuery = "SELECT jobdescription FROM Jobdescriptions WHERE jobdescription_email = ?";
        
        try (Connection conn = DriverManager.getConnection(URL);
            PreparedStatement statement = conn.prepareStatement(selectQuery)) {
            statement.setString(1, userEmail);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.getString("jobdescription");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Updates the resume of the user in the database with a new one
     * @param userEmail The user's email address.
     * @param userData The user's resume.
     */
    public void updateUserResume (String userEmail, String userData) {
        String updateQuery = "UPDATE Resumes SET resume = ? WHERE resume_email = ?";
        
        try (Connection conn = DriverManager.getConnection(URL);
            PreparedStatement statement = conn.prepareStatement(updateQuery)) {
            statement.setString(1, userData);
            statement.setString(2, userEmail);
            statement.executeUpdate();
            System.out.println("User resume updated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the last use job description of the user in the database with a new one
     * @param userEmail The user's email address.
     * @param userJobDescription The user's job description.
     */
    public void updateUserJobDescription (String userEmail, String userJobDescription) {
        String updateQuery = "UPDATE Jobdescriptions SET jobdescription = ? WHERE jobdescription_email = ?";
        
        try (Connection conn = DriverManager.getConnection(URL);
            PreparedStatement statement = conn.prepareStatement(updateQuery)) {
            statement.setString(1, userJobDescription);
            statement.setString(2, userEmail);
            statement.executeUpdate();
            System.out.println("User job description updated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the user's document type preference in the database
     * @param userEmail The user's email address.
     * @param userFormat The user's preferred document type.
     */
    public void updateUserDocTypePreference (String userEmail, String userFormat) {
        String updateQuery = "UPDATE Preferences SET format = ? WHERE preference_email = ?";
        
        try (Connection conn = DriverManager.getConnection(URL);
            PreparedStatement statement = conn.prepareStatement(updateQuery)) {
            statement.setString(1, userFormat);
            statement.setString(2, userEmail);
            statement.executeUpdate();
            System.out.println("User document type preference updated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Resets the resume of the user in the database
     * @param userEmail The user's email address.
     */
    public void resetUserResume (String userEmail) {
        String updateQuery = "UPDATE Resumes SET resume = NULL WHERE resume_email = ?";
        
        try (Connection conn = DriverManager.getConnection(URL);
            PreparedStatement statement = conn.prepareStatement(updateQuery)) {
            statement.setString(1, userEmail);
            statement.executeUpdate();
            System.out.println("User resume removed successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes the user from the Users table in the database
     * @param userEmail The user's email address.
     */
    public void deleteUser (String userEmail) {
        String deleteQuery = "DELETE FROM Users WHERE email = ?";
        
        try (Connection conn = DriverManager.getConnection(URL);
            PreparedStatement statement = conn.prepareStatement(deleteQuery)) {
            statement.execute("PRAGMA foreign_keys = ON;");
            statement.setString(1, userEmail);
            statement.executeUpdate();
            System.out.println("User deleted successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes all data from the Users table.
     */
    public void deleteAllUserData () {
        try (Connection conn = DriverManager.getConnection(URL)) {
            conn.createStatement().execute("DROP TABLE IF EXISTS Users");
            conn.createStatement().execute("DROP TABLE IF EXISTS Resumes");
            conn.createStatement().execute("DROP TABLE IF EXISTS Jobdescriptions");
            conn.createStatement().execute("DROP TABLE IF EXISTS Preferences");
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
     * Check if user exists in the database
     * @param email The email address to search for.
     */
    public boolean isExistingUser (String email) {
        String countQuerry = "SELECT COUNT(*) FROM Users WHERE email = ?";
        try (Connection conn = DriverManager.getConnection(URL);
            PreparedStatement statement = conn.prepareStatement(countQuerry)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                } else {
                    return false;
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks if the user has a job description in the database.
     * @param userEmail The user's email address.
     */
    public boolean hasExistingJobDescription (String userEmail) {
        String selectQuery = "SELECT jobdescription FROM Jobdescriptions WHERE jobdescription_email = ?";
        
        try (Connection conn = DriverManager.getConnection(URL);
            PreparedStatement statement = conn.prepareStatement(selectQuery)) {
            statement.setString(1, userEmail);
            ResultSet resultSet = statement.executeQuery();
            String data = resultSet.getString("jobdescription");
            if (data != null) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks if the user has an existing resume in the database.
     * @param userEmail The user's email address.
     */
    public boolean hasExistingResume (String userEmail) {
        String selectQuery = "SELECT resume FROM Resumes WHERE resume_email = ?";
        
        try (Connection conn = DriverManager.getConnection(URL);
            PreparedStatement statement = conn.prepareStatement(selectQuery)) {
            statement.setString(1, userEmail);
            ResultSet resultSet = statement.executeQuery();
            String data = resultSet.getString("resume");
            if (data != null) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
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

    /**
     * Calls the methods that create the enrtries in the database
     */
    public void createEntries(String email, String format) {
        createResumesEntry(email);
        createJobdescriptionsEntry(email);
        createPreferencesEntry(email, format);
    }
}
