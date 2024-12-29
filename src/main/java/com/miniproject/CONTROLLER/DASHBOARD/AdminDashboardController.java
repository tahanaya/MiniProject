package com.miniproject.CONTROLLER.DASHBOARD;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class AdminDashboardController {

    @FXML
    private AnchorPane contentPane; // The content area in the dashboard

    /**
     * Handles the "Etudiant" button click.
     */
    @FXML
    private void handleEtudiant() {
        System.out.println("Etudiant button clicked!");

        try {
            // Load EtudiantView.fxml
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

    /**
     * Handles the "Professeur" button click.
     */
    @FXML
    private void handleProfesseur() {
        System.out.println("Professeur button clicked!");

        try {
            // Load ProfesseurView.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/miniproject/view/Professeur/ProfesseurView.fxml"));
            Parent professeurView = loader.load();

            // Set the loaded view into the contentPane
            contentPane.getChildren().clear();
            contentPane.getChildren().add(professeurView);

            // Optionally, anchor the loaded view to fit the contentPane
            AnchorPane.setTopAnchor(professeurView, 0.0);
            AnchorPane.setBottomAnchor(professeurView, 0.0);
            AnchorPane.setLeftAnchor(professeurView, 0.0);
            AnchorPane.setRightAnchor(professeurView, 0.0);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the "Associer Etudiant" button click.
     */
    @FXML
    private void handleAssocierEtudiant() {
        System.out.println("Associer Etudiant button clicked!");
        // TODO: Implement Associer Etudiant functionality
    }

    /**
     * Handles the "Modules" button click.
     */
    @FXML
    private void handleModules() {
        System.out.println("Modules button clicked!");
        // TODO: Implement Modules view loading
    }

    /**
     * Handles the "Inscription" button click.
     */
    @FXML
    private void handleInscription() {
        System.out.println("Inscription button clicked!");
        // TODO: Implement Inscription view loading
    }

    /**
     * Handles the "Associer Prof" button click.
     */
    @FXML
    private void handleAssocierProf() {
        System.out.println("Associer Prof button clicked!");
        // TODO: Implement Associer Prof functionality
    }
}
