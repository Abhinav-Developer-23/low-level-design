package org.example.PhotoSharingApp.model;

import org.example.PhotoSharingApp.enums.NotificationType;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a notification sent to a user.
 * Immutable class for thread safety.
 */
public class Notification {
    private final String notificationId;
    private final String recipientUserId;
    private final NotificationType type;
    private final String message;
    private final LocalDateTime timestamp;
    private final String relatedEntityId; // Could be photoId, userId, etc.
    
    public Notification(String recipientUserId, NotificationType type, String message, String relatedEntityId) {
        this.notificationId = UUID.randomUUID().toString();
        this.recipientUserId = recipientUserId;
        this.type = type;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.relatedEntityId = relatedEntityId;
    }
    
    // Getters
    public String getNotificationId() {
        return notificationId;
    }
    
    public String getRecipientUserId() {
        return recipientUserId;
    }
    
    public NotificationType getType() {
        return type;
    }
    
    public String getMessage() {
        return message;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public String getRelatedEntityId() {
        return relatedEntityId;
    }
    
    @Override
    public String toString() {
        return String.format("Notification{type=%s, message='%s', time=%s}",
            type, message, timestamp);
    }
}







