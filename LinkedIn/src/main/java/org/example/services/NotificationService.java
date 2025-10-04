package org.example.services;

import org.example.enums.NotificationType;
import org.example.interfaces.NotificationObserver;
import org.example.model.Notification;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Service for managing and delivering notifications.
 * Implements the Observer pattern for real-time notification delivery.
 */
public class NotificationService {
    private final Map<String, Notification> notifications;
    private final Map<String, List<NotificationObserver>> observers;

    public NotificationService() {
        this.notifications = new ConcurrentHashMap<>();
        this.observers = new ConcurrentHashMap<>();
    }

    /**
     * Registers an observer for a user's notifications.
     */
    public void registerObserver(NotificationObserver observer) {
        String userId = observer.getUserId();
        observers.computeIfAbsent(userId, k -> new ArrayList<>()).add(observer);
    }

    /**
     * Unregisters an observer.
     */
    public void unregisterObserver(NotificationObserver observer) {
        String userId = observer.getUserId();
        List<NotificationObserver> userObservers = observers.get(userId);
        
        if (userObservers != null) {
            userObservers.remove(observer);
            if (userObservers.isEmpty()) {
                observers.remove(userId);
            }
        }
    }

    /**
     * Creates and sends a notification to a user.
     */
    public Notification sendNotification(String userId, NotificationType type, String title, 
                                        String message, String relatedEntityId) {
        Notification notification = new Notification(userId, type, title, message, relatedEntityId);
        notifications.put(notification.getNotificationId(), notification);

        // Notify all observers for this user
        notifyObservers(userId, notification);

        return notification;
    }

    /**
     * Notifies all observers for a specific user.
     */
    private void notifyObservers(String userId, Notification notification) {
        List<NotificationObserver> userObservers = observers.get(userId);
        
        if (userObservers != null) {
            // Notify all observers asynchronously in real-world scenario
            userObservers.forEach(observer -> observer.onNotificationReceived(notification));
        }
    }

    /**
     * Gets all notifications for a user.
     */
    public List<Notification> getNotificationsForUser(String userId) {
        return notifications.values().stream()
                .filter(n -> n.getUserId().equals(userId))
                .sorted(Comparator.comparing(Notification::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Gets unread notifications for a user.
     */
    public List<Notification> getUnreadNotifications(String userId) {
        return notifications.values().stream()
                .filter(n -> n.getUserId().equals(userId) && !n.isRead())
                .sorted(Comparator.comparing(Notification::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Marks a notification as read.
     */
    public boolean markAsRead(String notificationId) {
        Notification notification = notifications.get(notificationId);
        
        if (notification == null) {
            return false;
        }

        notification.markAsRead();
        return true;
    }

    /**
     * Gets the count of unread notifications for a user.
     */
    public int getUnreadCount(String userId) {
        return (int) notifications.values().stream()
                .filter(n -> n.getUserId().equals(userId) && !n.isRead())
                .count();
    }

    /**
     * Gets a notification by its ID.
     */
    public Notification getNotification(String notificationId) {
        return notifications.get(notificationId);
    }

    /**
     * Clears all notifications for a user.
     */
    public void clearNotifications(String userId) {
        notifications.values().removeIf(n -> n.getUserId().equals(userId));
    }
}

