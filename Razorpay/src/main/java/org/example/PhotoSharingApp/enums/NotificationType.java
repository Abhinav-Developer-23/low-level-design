package org.example.PhotoSharingApp.enums;

/**
 * Enum representing types of notifications in the system.
 * Following Open-Closed Principle - new types can be added without modifying existing code.
 */
public enum NotificationType {
    /**
     * Notification when someone follows you
     */
    NEW_FOLLOWER,
    
    /**
     * Notification when someone likes your photo
     */
    PHOTO_LIKED,
    
    /**
     * Notification when someone comments on your photo
     */
    PHOTO_COMMENTED,
    
    /**
     * Notification when someone you follow posts a new photo
     */
    NEW_PHOTO_FROM_FOLLOWING
}




