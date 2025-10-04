package org.example.observers;

import org.example.interfaces.NotificationObserver;
import org.example.model.Notification;

/**
 * Email implementation of NotificationObserver
 * Simulates sending email notifications
 */
public class EmailNotificationObserver implements NotificationObserver {
    private final String userId;
    private final String emailAddress;

    public EmailNotificationObserver(String userId, String emailAddress) {
        this.userId = userId;
        this.emailAddress = emailAddress;
    }

    @Override
    public void onNotification(Notification notification) {
        // In production, this would send an actual email using an email service
        System.out.println("\n=== EMAIL NOTIFICATION ===");
        System.out.println("To: " + emailAddress);
        System.out.println("Subject: " + notification.getTitle());
        System.out.println("Body: " + notification.getMessage());
        System.out.println("Notification Type: " + notification.getType());
        System.out.println("==========================\n");
    }

    @Override
    public String getUserId() {
        return userId;
    }

    public String getEmailAddress() {
        return emailAddress;
    }
}

