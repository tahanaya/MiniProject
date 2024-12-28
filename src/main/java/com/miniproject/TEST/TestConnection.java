package com.miniproject.TEST;

import com.miniproject.DATABASE.DatabaseConnection;

import java.sql.Connection;

public class TestConnection {

    public static void main(String[] args) {
        DatabaseConnection db = DatabaseConnection.getInstance();

        try (Connection connection = db.getConnection()) {

            System.out.println("mr7ba bik rak t connectiti.");
        } catch (Exception e) {
            System.err.println("Error using the connection: " + e.getMessage());
        } finally {
            db.closeConnection();
        }
    }
}
