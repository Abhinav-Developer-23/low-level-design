package org.example.interfaces;

import org.example.model.Notification;

/**
 * Observer interface for notification system
 * Part of Observer Design Pattern
 */
public interface NotificationObserver {
    /**
     * Called when a notification is sent to the user
     */
    void onNotification(Notification notification);

    /**
     * Get the user ID this observer is watching
     */
    String getUserId();
}

