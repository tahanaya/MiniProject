package com.miniproject.DATABASE;

import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static DatabaseConnection instance;
    @Getter
    private Connection connection;

    private final String url = "jdbc:postgresql://localhost:5432/MiniProject";
    private final String username = "postgres";
    private final String password = "Ayoub2003";

    private DatabaseConnection() {
        try {
            System.out.println("Loading database driver...");
            Class.forName("org.postgresql.Driver");

            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to the database successfully!");
        } catch (ClassNotFoundException e) {
            System.err.println("Database driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database: " + e.getMessage());
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    // Close connection method
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed successfully.");
            }
        } catch (SQLException e) {
            System.err.println("Error while closing the connection: " + e.getMessage());
        }
    }
}
