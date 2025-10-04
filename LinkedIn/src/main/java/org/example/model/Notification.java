package org.example.model;

import org.example.enums.NotificationType;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a notification sent to a user.
 */
public class Notification {
    private final String notificationId;
    private final String userId; // Recipient of the notification
    private final NotificationType type;
    private final String title;
    private final String message;
    private final String relatedEntityId; // ID of related entity (connection, job, message, etc.)
    private final LocalDateTime createdAt;
    private boolean isRead;

    public Notification(String userId, NotificationType type, String title, String message, String relatedEntityId) {
        this.notificationId = UUID.randomUUID().toString();
        this.userId = userId;
        this.type = type;
        this.title = title;
        this.message = message;
        this.relatedEntityId = relatedEntityId;
        this.createdAt = LocalDateTime.now();
        this.isRead = false;
    }

    // Getters
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

    public String getRelatedEntityId() {
        return relatedEntityId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isRead() {
        return isRead;
    }

    // Mark notification as read
    public void markAsRead() {
        this.isRead = true;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "type=" + type +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", isRead=" + isRead +
                ", createdAt=" + createdAt +
                '}';
    }
}

