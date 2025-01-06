package com.miniproject.DAO;

import com.miniproject.ENTITY.Etudiant;
import com.miniproject.ENTITY.Notification;
import com.miniproject.ENTITY.Module;

import com.miniproject.DATABASE.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAOImpl implements GenericDAO<Notification> {
    private final Connection conn = DatabaseConnection.getInstance().getConnection();

    @Override
    public Notification findById(int id) {
        String sql = "SELECT * FROM notification WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapToNotification(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Notification> findAll() {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT * FROM notification ORDER BY timestamp DESC";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                notifications.add(mapToNotification(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
    }

    @Override
    public void save(Notification entity) {
        String sql = "INSERT INTO notification (message, timestamp, seen) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, entity.getMessage());
            stmt.setTimestamp(2, Timestamp.valueOf(entity.getTimestamp()));
            stmt.setBoolean(3, entity.isSeen());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Notification entity) {
        String sql = "UPDATE notification SET seen = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, entity.isSeen());
            stmt.setInt(2, entity.getId());
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

    private Notification mapToNotification(ResultSet rs) throws SQLException {
        Notification notification = new Notification();
        notification.setId(rs.getInt("id"));
        notification.setMessage(rs.getString("message"));
        notification.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
        notification.setSeen(rs.getBoolean("seen"));
        return notification;
    }

    public void generateNoModuleAlert(int studentId, String studentName) {
        String message = "Alert: Student " + studentName + " (ID: " + studentId + ") is not enrolled in any modules.";
        save(new Notification(message, LocalDateTime.now(), false));
    }

    public void generateImportantDateReminder(String moduleName, LocalDate date) {
        String message = "Reminder: Important date for module " + moduleName + " is approaching (" + date + ").";
        save(new Notification(message, LocalDateTime.now(), false));
    }

    public void generateNoModuleNotifications() {
        EtudiantDAOImpl etudiantDAO = new EtudiantDAOImpl();
        List<Etudiant> studentsWithoutModules = etudiantDAO.getStudentsWithoutModules();

        for (Etudiant student : studentsWithoutModules) {
            String message = "Alert: Student " + student.getNom() + " " + student.getPrenom() +
                    " (ID: " + student.getId() + ") is not enrolled in any modules.";
            save(new Notification(message, LocalDateTime.now(), false));
        }
    }

    public void generateDeadlineNotifications() {
        ModuleDAOImpl moduleDAO = new ModuleDAOImpl();
        List<Module> modulesWithDeadlines = moduleDAO.getModulesWithUpcomingDeadlines(7);

        for (Module module : modulesWithDeadlines) {
            String message = "Reminder: Deadline for module " + module.getNomModule() +
                    " is approaching on " + module.getDeadline() + ".";
            save(new Notification(message, LocalDateTime.now(), false));
        }
    }



}
