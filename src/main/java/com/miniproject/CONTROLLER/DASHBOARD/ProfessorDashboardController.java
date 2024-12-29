package com.miniproject.CONTROLLER.DASHBOARD;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ProfessorDashboardController {

    @FXML
    private Label welcomeLabel;

    public void setWelcomeMessage(String username, String role) {
        if (welcomeLabel != null) {
            welcomeLabel.setText("Welcome " + username + "! Your role is: " + role + " (Professor)");
        }
    }

    // Add Professor-specific methods, e.g.
    // @FXML
    // private void showTaughtModules() { ... }
    // @FXML
    // private void viewEnrolledStudents() { ... }
}
