package org.example.PhotoSharingApp.service;

import org.example.PhotoSharingApp.enums.NotificationType;
import org.example.PhotoSharingApp.model.Notification;
import org.example.PhotoSharingApp.model.User;

/**
 * Service for managing notifications.
 * Single Responsibility: Handles notification creation and delivery.
 */
public class NotificationService {
    
    /**
     * Sends a notification to a user.
     * 
     * @param recipient the user receiving the notification
     * @param type the notification type
     * @param message the notification message
     * @param relatedEntityId the ID of the related entity (photo, user, etc.)
     */
    public void sendNotification(User recipient, NotificationType type, String message, String relatedEntityId) {
        Notification notification = new Notification(recipient.getUserId(), type, message, relatedEntityId);
        recipient.update(notification);
    }
    
    /**
     * Notifies a user about a new follower.
     * 
     * @param followee the user who was followed
     * @param follower the user who followed
     */
    public void notifyNewFollower(User followee, User follower) {
        String message = follower.getUsername() + " started following you";
        sendNotification(followee, NotificationType.NEW_FOLLOWER, message, follower.getUserId());
    }
    
    /**
     * Notifies a photo owner about a like.
     * 
     * @param photoOwner the owner of the photo
     * @param liker the user who liked
     * @param photoId the photo ID
     */
    public void notifyPhotoLiked(User photoOwner, User liker, String photoId) {
        String message = liker.getUsername() + " liked your photo";
        sendNotification(photoOwner, NotificationType.PHOTO_LIKED, message, photoId);
    }
    
    /**
     * Notifies a photo owner about a comment.
     * 
     * @param photoOwner the owner of the photo
     * @param commenter the user who commented
     * @param photoId the photo ID
     */
    public void notifyPhotoCommented(User photoOwner, User commenter, String photoId) {
        String message = commenter.getUsername() + " commented on your photo";
        sendNotification(photoOwner, NotificationType.PHOTO_COMMENTED, message, photoId);
    }
    
    /**
     * Notifies followers about a new photo upload.
     * 
     * @param follower the follower to notify
     * @param uploader the user who uploaded
     * @param photoId the photo ID
     */
    public void notifyNewPhotoFromFollowing(User follower, User uploader, String photoId) {
        String message = uploader.getUsername() + " posted a new photo";
        sendNotification(follower, NotificationType.NEW_PHOTO_FROM_FOLLOWING, message, photoId);
    }
}





