package com.miniproject.CONTROLLER.DASHBOARD;

import com.miniproject.CONTROLLER.NotificationsController;
import com.miniproject.ENTITY.Utilisateur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class SecretaryDashboardController {

    @FXML
    private VBox navbarContent;
    @FXML
    private AnchorPane contentPane;
    @FXML
    private AnchorPane navbar;
    private Utilisateur currentUser;

    public void setCurrentUser(Utilisateur user) {
        this.currentUser = user;
    }

    @FXML
    public void initialize() {
        navbar.setOnMouseEntered(event -> expandNavbar());
        navbar.setOnMouseExited(event -> collapseNavbar());
    }

    private void expandNavbar() {
        navbar.setPrefWidth(200);
        navbarContent.getChildren().forEach(node -> {
            if (node instanceof HBox hbox) {
                hbox.getChildren().stream()
                        .filter(child -> child instanceof Label)
                        .forEach(label -> {
                            label.setVisible(true);
                            label.setManaged(true);
                        });
            }
        });
    }

    private void collapseNavbar() {
        navbar.setPrefWidth(60);
        navbarContent.getChildren().forEach(node -> {
            if (node instanceof HBox hbox) {
                hbox.getChildren().stream()
                        .filter(child -> child instanceof Label)
                        .forEach(label -> {
                            label.setVisible(false);
                            label.setManaged(false);
                        });
            }
        });
    }

    @FXML
    private void handleEtudiant() {
        loadView("/com/miniproject/view/Etudiant/EtudiantView.fxml");
    }

    @FXML
    private void handleModules() {
        loadView("/com/miniproject/view/Module/ModuleViewSecretaire.fxml");
    }

    @FXML
    private void handleTableauDeBord() {
        loadView("/com/miniproject/view/TableauDeBord/TableauDeBord.fxml");
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
