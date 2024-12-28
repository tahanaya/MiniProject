package com.miniproject.CONTROLLER;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SecretaryDashboardController {

    @FXML
    private Label welcomeLabel;

    /**
     * Simple method to set a welcome message or other info.
     * You can call this after loading the FXML, e.g., from the LoginController.
     */
    public void setWelcomeMessage(String username, String role) {
        if (welcomeLabel != null) {
            welcomeLabel.setText("Welcome " + username + "! Your role is: " + role + " (Secretary)");
        }
    }

    // Add more methods to handle Secretary-specific functionalities.
    // For instance:
    // @FXML
    // private void showStudents() { ... }
    //
    // @FXML
    // private void manageEnrollments() { ... }
}
