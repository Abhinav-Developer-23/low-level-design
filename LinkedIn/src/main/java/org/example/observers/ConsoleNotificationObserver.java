package org.example.observers;

import org.example.interfaces.NotificationObserver;
import org.example.model.Notification;

/**
 * Console implementation of NotificationObserver
 * Prints notifications to console
 */
public class ConsoleNotificationObserver implements NotificationObserver {
    private final String userId;

    public ConsoleNotificationObserver(String userId) {
        this.userId = userId;
    }

    @Override
    public void onNotification(Notification notification) {
        System.out.println("\n=== CONSOLE NOTIFICATION ===");
        System.out.println("Type: " + notification.getType());
        System.out.println("Title: " + notification.getTitle());
        System.out.println("Message: " + notification.getMessage());
        System.out.println("Time: " + notification.getCreatedAt());
        System.out.println("===========================\n");
    }

    @Override
    public String getUserId() {
        return userId;
    }
}

