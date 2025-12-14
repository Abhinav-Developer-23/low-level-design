package org.example.PubSub.handler;

import org.example.PubSub.interfaces.IMessageHandler;
import org.example.PubSub.model.Message;

/**
 * Default implementation of IMessageHandler.
 * Provides a simple message handling strategy that prints messages to console.
 * Follows Strategy pattern - can be replaced with different implementations.
 */
public class DefaultMessageHandler implements IMessageHandler {
    private final String consumerName;

    /**
     * Constructor to create a message handler for a specific consumer.
     *
     * @param consumerName The name of the consumer using this handler
     */
    public DefaultMessageHandler(String consumerName) {
        this.consumerName = consumerName;
    }

    @Override
    public boolean handleMessage(Message message) {
        try {
            System.out.println("[" + consumerName + "] Received message: " + 
                             message.getPayload() + " from topic: " + message.getTopicId());
            // Simulate message processing
            Thread.sleep(100); // Simulate processing time
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        } catch (Exception e) {
            System.err.println("[" + consumerName + "] Error processing message: " + e.getMessage());
            return false;
        }
    }
}
