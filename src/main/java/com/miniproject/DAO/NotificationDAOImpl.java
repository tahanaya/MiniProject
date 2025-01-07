package com.miniproject.DAO;

//import com.miniproject.ENTITY.Etudiant;
//import com.miniproject.ENTITY.Notification;
//import com.miniproject.ENTITY.Module;
//
//import com.miniproject.DATABASE.DatabaseConnection;
//
//import java.sql.*;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//public class NotificationDAOImpl implements GenericDAO<Notification> {
//    private final Connection conn = DatabaseConnection.getInstance().getConnection();
//
//    @Override
//    public Notification findById(int id) {
//        String sql = "SELECT * FROM notification WHERE id = ?";
//        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
//            stmt.setInt(1, id);
//            ResultSet rs = stmt.executeQuery();
//            if (rs.next()) {
//                return mapToNotification(rs);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    @Override
//    public List<Notification> findAll() {
//        List<Notification> notifications = new ArrayList<>();
//        String sql = "SELECT * FROM notification ORDER BY timestamp DESC";
//        try (Statement stmt = conn.createStatement();
//             ResultSet rs = stmt.executeQuery(sql)) {
//            while (rs.next()) {
//                notifications.add(mapToNotification(rs));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return notifications;
//    }
//
//    @Override
//    public void save(Notification entity) {
//        String sql = "INSERT INTO notification (message, timestamp, seen) VALUES (?, ?, ?)";
//        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
//            stmt.setString(1, entity.getMessage());
//            stmt.setTimestamp(2, Timestamp.valueOf(entity.getTimestamp()));
//            stmt.setBoolean(3, entity.isSeen());
//            stmt.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void update(Notification entity) {
//        String sql = "UPDATE notification SET seen = ? WHERE id = ?";
//        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
//            stmt.setBoolean(1, entity.isSeen());
//            stmt.setInt(2, entity.getId());
//            stmt.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void delete(int id) {
//        String sql = "DELETE FROM notification WHERE id = ?";
//        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
//            stmt.setInt(1, id);
//            stmt.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private Notification mapToNotification(ResultSet rs) throws SQLException {
//        Notification notification = new Notification();
//        notification.setId(rs.getInt("id"));
//        notification.setMessage(rs.getString("message"));
//        notification.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
//        notification.setSeen(rs.getBoolean("seen"));
//        return notification;
//    }
//
//    public void generateNoModuleAlert(int studentId, String studentName) {
//        String message = "Alert: Student " + studentName + " (ID: " + studentId + ") is not enrolled in any modules.";
//        save(new Notification(message, LocalDateTime.now(), false));
//    }
//
//    public void generateImportantDateReminder(String moduleName, LocalDate date) {
//        String message = "Reminder: Important date for module " + moduleName + " is approaching (" + date + ").";
//        save(new Notification(message, LocalDateTime.now(), false));
//    }
//
//    public void generateNoModuleNotifications() {
//        EtudiantDAOImpl etudiantDAO = new EtudiantDAOImpl();
//        List<Etudiant> studentsWithoutModules = etudiantDAO.getStudentsWithoutModules();
//
//        if (!studentsWithoutModules.isEmpty()) {
//            StringBuilder messageBuilder = new StringBuilder("Alert: The following students are not enrolled in any modules:\n");
//            for (Etudiant student : studentsWithoutModules) {
//                messageBuilder.append("- ").append(student.getNom()).append(" ").append(student.getPrenom())
//                        .append(" (ID: ").append(student.getId()).append(")\n");
//            }
//
//            String message = messageBuilder.toString();
//            save(new Notification(message, LocalDateTime.now(), false));
//        }
//    }
//
//    public void generateDeadlineNotifications() {
//        ModuleDAOImpl moduleDAO = new ModuleDAOImpl();
//        List<Module> modulesWithDeadlines = moduleDAO.getModulesWithUpcomingDeadlines(7);
//
//        for (Module module : modulesWithDeadlines) {
//            String message = "Reminder: Deadline for module " + module.getNomModule() +
//                    " is approaching on " + module.getDeadline() + ".";
//            save(new Notification(message, LocalDateTime.now(), false));
//        }
//    }
//
//
//
//}

import com.miniproject.ENTITY.Notification;
import com.miniproject.DATABASE.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAOImpl implements NotificationDAO {

    private final Connection conn = DatabaseConnection.getInstance().getConnection();

    @Override
    public Notification findById(int id) {
        String sql = "SELECT * FROM notification WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapToNotification(rs); // Map the result to a Notification object
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if no notification is found
    }

    @Override
    public List<Notification> findAll() {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT * FROM notification ORDER BY timestamp DESC";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                notifications.add(mapToNotification(rs)); // Add each notification to the list
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
    }

    //If we want to go furthermore, and let the user create a notif
    @Override
    public void save(Notification notification) {
        String sql = "INSERT INTO notification (message, timestamp, seen) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, notification.getMessage());
            stmt.setTimestamp(2, Timestamp.valueOf(notification.getTimestamp()));
            stmt.setBoolean(3, notification.isSeen());
            stmt.executeUpdate();

            // Optionally get generated ID
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    notification.setId(keys.getInt(1)); // Set the generated ID on the notification object
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Notification notification) {
        String sql = "UPDATE notification SET seen = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, notification.isSeen());
            stmt.setInt(2, notification.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM notification WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insertNotificationForUsers(Notification notification, List<Integer> userIds) {
        String insertNotificationSql = "INSERT INTO notification (message, timestamp, seen) VALUES (?, ?, ?)";
        String insertNotificationUserSql = "INSERT INTO notification_user (notification_id, user_id) VALUES (?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(insertNotificationSql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, notification.getMessage());
            stmt.setTimestamp(2, Timestamp.valueOf(notification.getTimestamp()));
            stmt.setBoolean(3, notification.isSeen());
            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int notificationId = generatedKeys.getInt(1); // Get the generated notification ID
                try (PreparedStatement userStmt = conn.prepareStatement(insertNotificationUserSql)) {
                    for (int userId : userIds) {
                        userStmt.setInt(1, notificationId); // Set the notification ID
                        userStmt.setInt(2, userId); // Set the user ID
                        userStmt.addBatch();
                    }
                    userStmt.executeBatch();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //This method retrieves all notifications associated with a specific user.It joins the notification and notification_user tables to filter notifications for the given userId.
    @Override
    public List<Notification> getNotificationsForUser(int userId) {
        List<Notification> notifications = new ArrayList<>();
        String sql = """
            SELECT n.id, n.message, n.timestamp, n.seen 
            FROM notification n
            JOIN notification_user nu ON n.id = nu.notification_id
            WHERE nu.user_id = ? 
            ORDER BY n.timestamp DESC
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId); // Set the user ID parameter
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Notification notification = mapToNotification(rs);
                    // Map each result to a Notification object
                    notifications.add(notification);
                    System.out.println("Fetched Notification: " + notification.getMessage()); // Debugging
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return notifications;
    }

    //This helper method converts a ResultSet row into a Notification object.
    private Notification mapToNotification(ResultSet rs) throws SQLException {
        Notification notification = new Notification();
        notification.setId(rs.getInt("id"));
        notification.setMessage(rs.getString("message"));
        notification.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
        notification.setSeen(rs.getBoolean("seen"));
        return notification;
    }
}