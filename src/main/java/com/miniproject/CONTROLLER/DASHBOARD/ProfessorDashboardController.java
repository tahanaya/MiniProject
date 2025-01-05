package com.miniproject.CONTROLLER.DASHBOARD;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class ProfessorDashboardController {

    @FXML
    private AnchorPane contentPane; // The content area in the dashboard


    /**
     * Handles the "Etudiant" button click.
     */
    @FXML
    private void handleEtudiant() {
        System.out.println("Etudiant button clicked!");

        try {
            // Load EtudiantProfesseurView.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/miniproject/view/Etudiant-Professeur/EtudiantProfesseurView.fxml"));
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
     * Handles the "Modules" button click.
     */
    @FXML
    private void handleModules() {
        System.out.println("Modules button clicked!");

        try {
            // Load ModuleView.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/miniproject/view/Module-Professeur/ModuleProfesseurView.fxml"));
            Parent moduleView = loader.load();

            // Set the loaded view into the contentPane
            contentPane.getChildren().clear();
            contentPane.getChildren().add(moduleView);

            // Optionally, anchor the loaded view to fit the contentPane
            AnchorPane.setTopAnchor(moduleView, 0.0);
            AnchorPane.setBottomAnchor(moduleView, 0.0);
            AnchorPane.setLeftAnchor(moduleView, 0.0);
            AnchorPane.setRightAnchor(moduleView, 0.0);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
