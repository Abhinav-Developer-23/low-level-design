package org.example.PubSub.interfaces;

import org.example.PubSub.model.Topic;

import java.util.List;

/**
 * Interface for Topic Management operations
 * Defines contract for creating and managing topics
 * Follows Single Responsibility Principle (SRP)
 */
public interface ITopicManager {
    /**
     * Creates a new topic
     * @param topicName The name of the topic
     * @return The created Topic object
     */
    Topic createTopic(String topicName);
    
    /**
     * Gets a topic by its ID
     * @param topicId The ID of the topic
     * @return The Topic object, or null if not found
     */
    Topic getTopic(String topicId);
    
    /**
     * Gets a topic by its name
     * @param topicName The name of the topic
     * @return The Topic object, or null if not found
     */
    Topic getTopicByName(String topicName);
    
    /**
     * Deletes a topic
     * @param topicId The ID of the topic to delete
     * @return true if deleted successfully, false otherwise
     */
    boolean deleteTopic(String topicId);
    
    /**
     * Gets all topics
     * @return List of all topics
     */
    List<Topic> getAllTopics();
}

