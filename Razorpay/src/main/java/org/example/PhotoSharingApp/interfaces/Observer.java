package org.example.PhotoSharingApp.interfaces;

import org.example.PhotoSharingApp.model.Notification;

/**
 * Observer Pattern: Interface for objects that want to be notified of events.
 * Allows loose coupling between subjects and observers.
 */
public interface Observer {
    /**
     * Called when an event occurs that this observer is interested in.
     * 
     * @param notification the notification containing event details
     */
    void update(Notification notification);
}




