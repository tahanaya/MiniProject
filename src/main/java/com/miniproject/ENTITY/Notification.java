package com.miniproject.ENTITY;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Notification {
    private int id;
    private String message;
    private LocalDateTime timestamp;
    private boolean seen; // To track if the notification has been read
    private List<Utilisateur> users;     //  list of users associated with the notification
    private String type; // New field to store notification type

    // Custom constructor without ID (useful for creating new notifications)
    public Notification(String message, LocalDateTime timestamp, boolean seen) {
        this.message = message;
        this.timestamp = timestamp;
        this.seen = seen;
    }
}
