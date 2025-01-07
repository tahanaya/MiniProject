package com.miniproject.CONTROLLER;


import com.miniproject.DAO.EtudiantDAOImpl;
import com.miniproject.DAO.NotificationDAOImpl;
import com.miniproject.ENTITY.Notification;
import com.miniproject.ENTITY.Utilisateur;
import com.miniproject.ENTITY.Etudiant;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;


public class NotificationsController {

    private final NotificationDAOImpl notificationDAO = new NotificationDAOImpl();
    private Utilisateur currentUser; // To track the logged-in user

    @FXML private VBox notificationContainer; // The VBox in FXML for displaying notifications

    @FXML private ListView<String> notificationListView; // ListView to display notifications



    //Method to set the current user (called when the user logs in)
    public void setCurrentUser(Utilisateur user) {
        this.currentUser = user;
        System.out.println("Current user set: " + user.getId()); // Debug statement
        loadNotifications(); // Load notifications for the current user
    }

    //Method to load notifications for the logged-in user
    private void loadNotifications() {
        if (currentUser == null) {
            return; // No user logged in, nothing to load
        }

        // Retrieve notifications for the logged-in user
        List<Notification> notifications = notificationDAO.getNotificationsForUser(currentUser.getId());

        // Convert notifications to an observable list for the ListView
        for (Notification notification : notifications) {
            // Create a card for each notification
            AnchorPane card = createNotificationCard(notification);

            // Add the card to the container
            notificationContainer.getChildren().add(card);
        }
    }

    private AnchorPane createNotificationCard(Notification notification) {
        AnchorPane card = new AnchorPane();
        card.setStyle(notification.isSeen() ? "notification-card" : "notification-card unread");

        // Notification content
        Label messageLabel = new Label(notification.getMessage());
        messageLabel.setWrapText(true);
        messageLabel.setStyle("-fx-font-size: 14px;");

        Label timestampLabel = new Label("Timestamp: " + notification.getTimestamp().toString());
        timestampLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: gray;");

        Button viewButton = new Button("View");
        viewButton.setOnAction(e -> showNotificationDetails(notification));
        viewButton.getStyleClass().add("action-button");

        // Layout
        AnchorPane.setTopAnchor(messageLabel, 10.0);
        AnchorPane.setLeftAnchor(messageLabel, 10.0);
        AnchorPane.setRightAnchor(messageLabel, 10.0);

        AnchorPane.setTopAnchor(timestampLabel, 40.0);
        AnchorPane.setLeftAnchor(timestampLabel, 10.0);

        AnchorPane.setTopAnchor(viewButton, 40.0);
        AnchorPane.setRightAnchor(viewButton, 10.0);

        card.getChildren().addAll(messageLabel, timestampLabel, viewButton);

        return card;
    }
    private void showNotificationDetails(Notification notification) {
        // Log the notification message
        System.out.println("Notification Message: " + notification.getMessage());

        // Create an instance of EtudiantDAOImpl to fetch data
        EtudiantDAOImpl etudiantDAO = new EtudiantDAOImpl();

        // Fetch students without modules
        List<Etudiant> students = etudiantDAO.getStudentsWithoutModules();
        ObservableList<Etudiant> observableStudents = FXCollections.observableArrayList(students);

        // Create TableView
        TableView<Etudiant> table = new TableView<>(observableStudents);

        TableColumn<Etudiant, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Etudiant, String> matriculeColumn = new TableColumn<>("Matricule");
        matriculeColumn.setCellValueFactory(new PropertyValueFactory<>("matricule"));

        TableColumn<Etudiant, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getNom() + " " + cellData.getValue().getPrenom()));

        TableColumn<Etudiant, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Etudiant, String> promotionColumn = new TableColumn<>("Promotion");
        promotionColumn.setCellValueFactory(new PropertyValueFactory<>("promotion"));

        table.getColumns().addAll(idColumn, matriculeColumn, nameColumn, emailColumn, promotionColumn);
        table.getStylesheets().add(getClass().getResource("/com/miniproject/css/notification-details.css").toExternalForm());
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Style and Layout
        Label messageLabel = new Label(notification.getMessage());
        messageLabel.getStyleClass().add("dialog-label");

        Label timestampLabel = new Label("Timestamp: " + notification.getTimestamp());
        timestampLabel.getStyleClass().add("dialog-label");

        VBox content = new VBox(
                new Label("Notification Details") {{ getStyleClass().add("dialog-title"); }},
                messageLabel,
                table,
                timestampLabel
        );
        content.getStyleClass().add("dialog-content");

        // Dialog
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Notification Details");
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getStylesheets().add(getClass().getResource("/com/miniproject/css/notification-details.css").toExternalForm());
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.showAndWait();
    }


    //Mark selected notification as seen
    @FXML
    private void markAsSeen() {
        int selectedIndex = notificationListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            List<Notification> notifications = notificationDAO.getNotificationsForUser(currentUser.getId());
            Notification selectedNotification = notifications.get(selectedIndex);
            selectedNotification.setSeen(true); // Mark as seen

            // Update the notification in the database
            notificationDAO.update(selectedNotification);

            // Reload notifications to update the view
            loadNotifications();
        }
    }

    //Delete selected notification
    @FXML
    private void deleteNotification() {
        int selectedIndex = notificationListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            List<Notification> notifications = notificationDAO.getNotificationsForUser(currentUser.getId());
            Notification selectedNotification = notifications.get(selectedIndex);

            // Delete the notification from the database
            notificationDAO.delete(selectedNotification.getId());

            // Reload notifications to update the view
            loadNotifications();
        }
    }
}
