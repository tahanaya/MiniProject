package com.miniproject.CONTROLLER.DASHBOARD;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class AdminDashboardController {

    @FXML
    private AnchorPane contentPane; // The content area in the dashboard
    @FXML
    private AnchorPane navbar;

    @FXML
    private VBox navbarContent;

    @FXML
    public void initialize() {
        // Add hover listener to navbar
        navbar.setOnMouseEntered(event -> expandNavbar());
        navbar.setOnMouseExited(event -> collapseNavbar());
    }
    private void expandNavbar() {
        navbar.setPrefWidth(200); // Expand navbar width programmatically


        System.out.println("Expanding Navbar");

        navbarContent.getChildren().forEach(node -> {
            if (node instanceof HBox hbox) {
                hbox.getChildren().stream()
                        .filter(child -> child instanceof Label)
                        .forEach(label -> {
                            label.setVisible(true);
                            label.setManaged(true); // Ensure layout space is allocated
                            System.out.println("Label shown: " + ((Label) label).getText() );
                        });
            }
        });
    }
    private void collapseNavbar() {
        System.out.println("Collapsing Navbar");
        navbar.setPrefWidth(60); // Collapse navbar width programmatically

        navbarContent.getChildren().forEach(node -> {
            if (node instanceof HBox hbox) {
                System.out.println("HBox Bounds: " + hbox.getBoundsInParent());
                hbox.getChildren().stream()
                        .filter(child -> child instanceof Label)
                        .forEach(label -> {
                            label.setVisible(false);// Hide label
                            label.setManaged(false); // Ensure the layout adjusts
                        });
            }
        });
    }
    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();

            contentPane.getChildren().clear();
            contentPane.getChildren().add(view);

            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the "Etudiant" button click.
     */
    @FXML
    private void handleEtudiant() {
        System.out.println("Etudiant button clicked!");

            loadView("/com/miniproject/view/Etudiant/EtudiantView.fxml");

    }

    /**
     * Handles the "Professeur" button click.
     */
    @FXML
    private void handleProfesseur() {
        System.out.println("Professeur button clicked!");
        loadView("/com/miniproject/view/Professeur/ProfesseurView.fxml");

    }

    @FXML
    private void handleTableauDeBord() {
        System.out.println("Tableau de Bord button clicked!");
        loadView("/com/miniproject/view/TableauDeBord/TableauDeBord.fxml");

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
        loadView("/com/miniproject/view/Module/ModuleView.fxml");

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
