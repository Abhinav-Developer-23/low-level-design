package org.example.services;

import org.example.enums.NotificationType;
import org.example.interfaces.NotificationObserver;
import org.example.model.Notification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * Service for managing notifications
 * Implements Observer Design Pattern
 * Thread-safe using ConcurrentHashMap and CopyOnWriteArrayList
 */
public class NotificationService {
    private final Map<String, Notification> notifications;
    private final List<NotificationObserver> observers;

    public NotificationService() {
        this.notifications = new ConcurrentHashMap<>();
        this.observers = new CopyOnWriteArrayList<>();
    }

    /**
     * Register an observer to receive notifications
     */
    public void registerObserver(NotificationObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    /**
     * Unregister an observer
     */
    public void unregisterObserver(NotificationObserver observer) {
        observers.remove(observer);
    }

    /**
     * Send a notification to a user
     */
    public Notification sendNotification(String userId, NotificationType type, 
                                         String title, String message, String relatedId) {
        Notification notification = new Notification(userId, type, title, message, relatedId);
        notifications.put(notification.getNotificationId(), notification);

        // Notify all observers watching this user
        notifyObservers(notification);

        return notification;
    }

    /**
     * Notify all registered observers for a specific user
     */
    private void notifyObservers(Notification notification) {
        observers.stream()
                .filter(observer -> observer.getUserId().equals(notification.getUserId()))
                .forEach(observer -> observer.onNotification(notification));
    }

    /**
     * Get all notifications for a user
     */
    public List<Notification> getNotifications(String userId) {
        if (userId == null) {
            return new ArrayList<>();
        }

        return notifications.values().stream()
                .filter(n -> n.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    /**
     * Get unread notifications for a user
     */
    public List<Notification> getUnreadNotifications(String userId) {
        if (userId == null) {
            return new ArrayList<>();
        }

        return notifications.values().stream()
                .filter(n -> n.getUserId().equals(userId) && !n.isRead())
                .collect(Collectors.toList());
    }

    /**
     * Mark notification as read
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
     * Get notification by ID
     */
    public Notification getNotificationById(String notificationId) {
        return notifications.get(notificationId);
    }

    /**
     * Get count of unread notifications for a user
     */
    public int getUnreadCount(String userId) {
        if (userId == null) {
            return 0;
        }

        return (int) notifications.values().stream()
                .filter(n -> n.getUserId().equals(userId) && !n.isRead())
                .count();
    }
}

