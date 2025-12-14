package org.example.PubSub.interfaces;

import org.example.PubSub.model.Message;

/**
 * Interface for handling messages.
 * Consumers implement this interface to process messages they receive.
 * Follows the Strategy pattern for different message handling strategies.
 */
public interface IMessageHandler {
    /**
     * Handles a message received from a topic.
     *
     * @param message The message to handle
     * @return true if message was handled successfully, false otherwise
     */
    boolean handleMessage(Message message);
}
