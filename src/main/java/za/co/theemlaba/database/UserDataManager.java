package za.co.theemlaba.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.io.File;


public class UserDataManager {
    private String URL;
    private static final String DATABASE_DIR = "database";

    public UserDataManager() {
        setDatabaseName("data.db");
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
}