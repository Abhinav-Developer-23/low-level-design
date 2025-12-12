package org.example.PubSub.interfaces;

import org.example.PubSub.enums.ConsumerState;
import org.example.PubSub.model.Message;

/**
 * Interface for Consumer in pub-sub system
 * Defines contract for consuming and processing messages
 * Follows Interface Segregation Principle (ISP)
 */
public interface IConsumer {
    /**
     * Processes a consumed message
     * @param message The message to process
     */
    void consume(Message message);
    
    /**
     * Subscribes to a topic
     * @param topicId The ID of the topic to subscribe to
     */
    void subscribe(String topicId);
    
    /**
     * Unsubscribes from a topic
     * @param topicId The ID of the topic to unsubscribe from
     */
    void unsubscribe(String topicId);
    
    /**
     * Gets the consumer ID
     * @return The unique identifier for this consumer
     */
    String getConsumerId();
    
    /**
     * Gets the consumer name
     * @return The name of this consumer
     */
    String getConsumerName();
    
    /**
     * Gets the current state of the consumer
     * @return The consumer's state
     */
    ConsumerState getState();
    
    /**
     * Sets the state of the consumer
     * @param state The new state for the consumer
     */
    void setState(ConsumerState state);
}

