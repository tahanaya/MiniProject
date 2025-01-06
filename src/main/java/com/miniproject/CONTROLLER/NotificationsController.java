package com.miniproject.CONTROLLER;

import com.miniproject.DAO.EtudiantDAOImpl;
import com.miniproject.DAO.NotificationDAOImpl;
import com.miniproject.ENTITY.Etudiant;
import com.miniproject.ENTITY.Notification;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

import java.util.List;
import java.time.LocalDateTime;


public class NotificationsController {

    @FXML
    private ListView<Notification> notificationListView;

    @FXML
    private TextArea notificationDetails;

    private final NotificationDAOImpl notificationDAO = new NotificationDAOImpl();

    @FXML
    public void initialize() {
        // Generate notifications
        generateNotifications();

        // Load notifications into the ListView
        loadNotifications();

        // Handle notification selection
        notificationListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                showNotificationDetails(newValue);
            }
        });

        // Apply custom cell factory for unseen notifications
        notificationListView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Notification notification, boolean empty) {
                super.updateItem(notification, empty);

                if (empty || notification == null) {
                    setText(null);
                    setStyle(null);
                } else {
                    setText(notification.getMessage());

                    // Style unseen notifications
                    if (!notification.isSeen()) {
                        setStyle("-fx-font-weight: bold; -fx-text-fill: blue;");
                    } else {
                        setStyle("-fx-font-weight: normal; -fx-text-fill: black;");
                    }
                }
            }
        });
    }

    private void generateNotifications() {
        generateNoModuleNotification();
        generateDeadlineNotifications();
    }

    private void generateNoModuleNotification() {
        EtudiantDAOImpl etudiantDAO = new EtudiantDAOImpl();
        List<Etudiant> studentsWithoutModules = etudiantDAO.getStudentsWithoutModules();

        if (!studentsWithoutModules.isEmpty()) {
            StringBuilder messageBuilder = new StringBuilder("Alert: The following students are not enrolled in any modules:\n");
            for (Etudiant student : studentsWithoutModules) {
                messageBuilder.append("- ").append(student.getNom()).append(" ").append(student.getPrenom())
                        .append(" (ID: ").append(student.getId()).append(")\n");
            }

            String message = messageBuilder.toString();
            notificationDAO.save(new Notification(message, LocalDateTime.now(), false));
        }
    }

    private void generateDeadlineNotifications() {
        // This method should handle deadline-based notifications.
        // You can add your module deadline logic here, similar to the `generateNoModuleNotification` method.
    }

    private void loadNotifications() {
        List<Notification> notifications = notificationDAO.findAll();
        notificationListView.getItems().addAll(notifications);
    }

    private void showNotificationDetails(Notification notification) {
        // Display the selected notification's details in the TextArea
        notificationDetails.setText(
                "Message: " + notification.getMessage() + "\n" +
                        "Timestamp: " + notification.getTimestamp() + "\n" +
                        "Seen: " + (notification.isSeen() ? "Yes" : "No")
        );

        // Mark the notification as "seen"
        if (!notification.isSeen()) {
            notification.setSeen(true);
            notificationDAO.update(notification); // Update the database
            notificationListView.refresh(); // Refresh the ListView to update the style
        }
    }
}
