package com.miniproject.CONTROLLER;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LandingPageController {

    private Stage primaryStage;

    // Called by main app to pass in the primary stage
    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    @FXML
    private void goToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/miniproject/view/LoginView.fxml"));
            Parent loginRoot = loader.load();

            // Switch scene to the login page
            primaryStage.setScene(new Scene(loginRoot));
            primaryStage.setTitle("Login");
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
