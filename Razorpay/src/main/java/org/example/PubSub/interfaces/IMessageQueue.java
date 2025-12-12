package org.example.PubSub.interfaces;

import org.example.PubSub.model.Message;

/**
 * Interface for Message Queue operations
 * Defines contract for queue-based message storage
 * Allows for different queue implementations (memory, persistent, etc.)
 */
public interface IMessageQueue {
    /**
     * Adds a message to the queue
     * @param message The message to enqueue
     */
    void enqueue(Message message);
    
    /**
     * Removes and returns the next message from the queue
     * @return The next message, or null if queue is empty
     */
    Message dequeue();
    
    /**
     * Returns the next message without removing it
     * @return The next message, or null if queue is empty
     */
    Message peek();
    
    /**
     * Checks if the queue is empty
     * @return true if queue is empty, false otherwise
     */
    boolean isEmpty();
    
    /**
     * Gets the size of the queue
     * @return The number of messages in the queue
     */
    int size();
}

