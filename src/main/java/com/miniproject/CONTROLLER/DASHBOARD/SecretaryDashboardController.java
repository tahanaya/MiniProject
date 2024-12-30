package com.miniproject.CONTROLLER.DASHBOARD;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class SecretaryDashboardController {

    @FXML
    private AnchorPane contentPane;

    @FXML
    private void handleEtudiant() {
        System.out.println("Etudiant button clicked!");

        try {
            // Load EtudiantProfesseurView.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/miniproject/view/Etudiant/EtudiantView.fxml"));
            Parent etudiantView = loader.load();

            // Set the loaded view into the contentPane
            contentPane.getChildren().clear();
            contentPane.getChildren().add(etudiantView);

            // Optionally, anchor the loaded view to fit the contentPane
            AnchorPane.setTopAnchor(etudiantView, 0.0);
            AnchorPane.setBottomAnchor(etudiantView, 0.0);
            AnchorPane.setLeftAnchor(etudiantView, 0.0);
            AnchorPane.setRightAnchor(etudiantView, 0.0);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleScheduleMeetings(ActionEvent event) {
        System.out.println("Schedule Meetings clicked!");
        // Add functionality to load the "Schedule Meetings" view here
    }

    @FXML
    private void handleOption3(ActionEvent event) {
        System.out.println("Option 3 clicked!");
        // Add functionality for Option 3
    }
}