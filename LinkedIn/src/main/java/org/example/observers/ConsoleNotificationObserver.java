package org.example.observers;

import org.example.interfaces.NotificationObserver;
import org.example.model.Notification;

/**
 * Console-based notification observer for demonstration.
 * In production, this would be replaced with push notifications, websockets, etc.
 */
public class ConsoleNotificationObserver implements NotificationObserver {
    private final String userId;

    public ConsoleNotificationObserver(String userId) {
        this.userId = userId;
    }

    @Override
    public void onNotificationReceived(Notification notification) {
        System.out.println("\n[NEW NOTIFICATION] for User: " + userId);
        System.out.println("   Type: " + notification.getType());
        System.out.println("   Title: " + notification.getTitle());
        System.out.println("   Message: " + notification.getMessage());
        System.out.println("   Time: " + notification.getCreatedAt());
        System.out.println("-------------------------------------------");
    }

    @Override
    public String getUserId() {
        return userId;
    }
}

