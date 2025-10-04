package org.example.observers;

import org.example.interfaces.NotificationObserver;
import org.example.model.Notification;

/**
 * Email-based notification observer.
 * In production, this would integrate with an email service (SendGrid, AWS SES, etc.)
 */
public class EmailNotificationObserver implements NotificationObserver {
    private final String userId;
    private final String emailAddress;

    public EmailNotificationObserver(String userId, String emailAddress) {
        this.userId = userId;
        this.emailAddress = emailAddress;
    }

    @Override
    public void onNotificationReceived(Notification notification) {
        // Simulate sending an email
        sendEmail(emailAddress, notification.getTitle(), notification.getMessage());
    }

    @Override
    public String getUserId() {
        return userId;
    }

    /**
     * Simulates sending an email notification.
     * In production, integrate with actual email service.
     */
    private void sendEmail(String to, String subject, String body) {
        System.out.println("[EMAIL SENT]");
        System.out.println("   To: " + to);
        System.out.println("   Subject: " + subject);
        System.out.println("   Body: " + body);
        System.out.println("-------------------------------------------");
    }
}

