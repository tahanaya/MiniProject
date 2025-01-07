package com.miniproject.DAO;
import com.miniproject.ENTITY.Notification;
import java.util.List;

public interface NotificationDAO extends GenericDAO<Notification>{
    // Custom method to insert a notification for specific users
    void insertNotificationForUsers(Notification notification, List<Integer> userIds);

    // Custom method to retrieve notifications for a specific user
    List<Notification> getNotificationsForUser(int userId);
}
