package org.example.interfaces;

import org.example.model.Notification;

/**
 * Observer interface for receiving notifications.
 * Implements the Observer design pattern for real-time notifications.
 */
public interface NotificationObserver {
    /**
     * Called when a new notification is available for the observer.
     *
     * @param notification The notification to be delivered
     */
    void onNotificationReceived(Notification notification);

    /**
     * Returns the user ID this observer is watching notifications for.
     *
     * @return User ID
     */
    String getUserId();
}

