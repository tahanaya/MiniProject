package com.miniproject.CONTROLLER;

import com.miniproject.DAO.UserDAO;
import com.miniproject.DAO.UserDAOImpl;
import com.miniproject.ENTITY.Utilisateur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    private final UserDAO userDAO = new UserDAOImpl();

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        Utilisateur user = userDAO.getUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            // Successful login
            redirectToDashboard(user);
        } else {
            // Show error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Error");
            alert.setHeaderText(null);
            alert.setContentText("Invalid username or password.");
            alert.showAndWait();
        }
    }

    private void redirectToDashboard(Utilisateur user) {
        String role = user.getRole();
        String fxmlFile = "";
        switch (role) {
            case "ADMINISTRATEUR":
                fxmlFile = "/com/miniproject/view/Dashboard/AdminDashboardView.fxml";
                break;
            case "SECRETAIRE":
                 fxmlFile = "/com/miniproject/view/Dashboard/SecretaryDashboardView.fxml";

                break;
            case "PROFESSEUR":
                fxmlFile = "/com/miniproject/view/Dashboard/ProfessorDashboardView.fxml";

                break;
            default:
                // If no role matches, do nothing or show error
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Role Error");
                alert.setContentText("Unknown role: " + role);
                alert.showAndWait();
                return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Scene scene = new Scene(loader.load());

            // Use the current window from the username field
            Stage stage = (Stage
                    ) usernameField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Dashboard");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
