package com.miniproject.CONTROLLER;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AdminDashboardController {

    @FXML
    private Label welcomeLabel;

    public void setWelcomeMessage(String username, String role) {
        if (welcomeLabel != null) {
            welcomeLabel.setText("Welcome " + username + "! Your role is: " + role);
        }
    }
}
