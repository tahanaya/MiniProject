package com.miniproject.CONTROLLER.DASHBOARD;

import com.miniproject.CONTROLLER.NotificationsController;
import com.miniproject.ENTITY.Utilisateur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class ProfessorDashboardController {

    @FXML
    private AnchorPane contentPane; // The content area in the dashboard
    private Utilisateur currentUser;

    public void setCurrentUser(Utilisateur user) {
        this.currentUser = user;
    }


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

    @FXML
    private void handleTableauDeBord() {
        System.out.println("Tableau de Bord button clicked!");
        loadView("/com/miniproject/view/TableauDeBord/TableauDeBord.fxml");

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
    @FXML
    private void handleLogout() {
        System.out.println("Logout button clicked!");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/miniproject/view/LoginView.fxml"));
            Parent loginView = loader.load();

            contentPane.getScene().setRoot(loginView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleNotifications() {
        System.out.println("Notifications button clicked!");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/miniproject/view/NotificationView.fxml"));
            Parent view = loader.load();

            // Pass the logged-in user to the NotificationsController
            NotificationsController controller = loader.getController();
            controller.setCurrentUser(currentUser); // Make sure 'currentUser' is stored in the dashboard controller

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

}
