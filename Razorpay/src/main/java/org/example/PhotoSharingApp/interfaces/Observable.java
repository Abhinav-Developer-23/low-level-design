package org.example.PhotoSharingApp.interfaces;

/**
 * Observer Pattern: Interface for objects that can be observed.
 * Defines methods for managing observers.
 */
public interface Observable {
    /**
     * Adds an observer to be notified of events.
     * 
     * @param observer the observer to add
     */
    void addObserver(Observer observer);
    
    /**
     * Removes an observer from the notification list.
     * 
     * @param observer the observer to remove
     */
    void removeObserver(Observer observer);
    
    /**
     * Notifies all registered observers of an event.
     */
    void notifyObservers();
}






