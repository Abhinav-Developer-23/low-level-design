package org.example.PubSub.interfaces;

import org.example.PubSub.model.Message;

/**
 * Interface for Producer in pub-sub system
 * Defines contract for publishing messages to topics
 * Follows Interface Segregation Principle (ISP)
 */
public interface IProducer {
    /**
     * Publishes a message to a specific topic
     * @param topicId The ID of the topic to publish to
     * @param content The content of the message
     * @return The published Message object
     */
    Message publish(String topicId, String content);
    
    /**
     * Gets the producer ID
     * @return The unique identifier for this producer
     */
    String getProducerId();
    
    /**
     * Gets the producer name
     * @return The name of this producer
     */
    String getProducerName();
}

