package org.example.PubSub.interfaces;

import org.example.PubSub.model.Message;

/**
 * Interface for publishing messages to topics.
 * Follows the Publisher pattern from the Observer design pattern.
 */
public interface IPublisher {
    /**
     * Publishes a message to a topic.
     *
     * @param topicId The ID of the topic to publish to
     * @param payload The data/content to publish (must be a String)
     * @return The created message, or null if topic doesn't exist
     */
    Message publish(String topicId, String payload);

    /**
     * Creates a new topic.
     *
     * @param topicName The name of the topic to create
     * @return The created topic, or null if topic already exists
     */
    org.example.PubSub.model.Topic createTopic(String topicName);
}
