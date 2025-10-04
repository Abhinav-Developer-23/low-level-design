package org.example.model;

import org.example.enums.NotificationType;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a notification sent to a user
 */
public class Notification {
    private final String notificationId;
    private final String userId;
    private final NotificationType type;
    private final String title;
    private final String message;
    private final String relatedId; // ID of related entity (connection, message, job, etc.)
    private boolean read;
    private final LocalDateTime createdAt;

    public Notification(String userId, NotificationType type, String title, String message, String relatedId) {
        this.notificationId = UUID.randomUUID().toString();
        this.userId = userId;
        this.type = type;
        this.title = title;
        this.message = message;
        this.relatedId = relatedId;
        this.read = false;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getNotificationId() {
        return notificationId;
    }

    public String getUserId() {
        return userId;
    }

    public NotificationType getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getRelatedId() {
        return relatedId;
    }

    public boolean isRead() {
        return read;
    }

    public void markAsRead() {
        this.read = true;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

