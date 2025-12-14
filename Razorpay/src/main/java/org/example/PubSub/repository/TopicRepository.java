package org.example.PubSub.repository;

import org.example.PubSub.model.Topic;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Repository class for managing topics.
 * Implements Singleton pattern to ensure only one instance exists.
 * Uses HashMap for storing topics by topicId and topicName.
 */
public class TopicRepository {
    private static TopicRepository instance;
    private final Map<String, Topic> topicsById; // Map of topicId -> Topic
    private final Map<String, Topic> topicsByName; // Map of topicName -> Topic

    /**
     * Private constructor to prevent direct instantiation.
     */
    private TopicRepository() {
        this.topicsById = new HashMap<>();
        this.topicsByName = new HashMap<>();
    }

    /**
     * Returns the singleton instance of TopicRepository.
     * Uses double-checked locking for thread safety.
     *
     * @return The singleton instance
     */
    public static synchronized TopicRepository getInstance() {
        if (instance == null) {
            instance = new TopicRepository();
        }
        return instance;
    }

    /**
     * Adds a topic to the repository.
     *
     * @param topic The topic to add
     * @return true if topic was added successfully, false if topic with same name already exists
     */
    public boolean addTopic(Topic topic) {
        if (topic == null) {
            return false;
        }
        
        // Check if topic with same name already exists
        if (topicsByName.containsKey(topic.getTopicName())) {
            return false;
        }
        
        topicsById.put(topic.getTopicId(), topic);
        topicsByName.put(topic.getTopicName(), topic);
        return true;
    }

    /**
     * Retrieves a topic by its ID.
     *
     * @param topicId The ID of the topic
     * @return Optional containing the topic if found, empty otherwise
     */
    public Optional<Topic> getTopicById(String topicId) {
        return Optional.ofNullable(topicsById.get(topicId));
    }

    /**
     * Retrieves a topic by its name.
     *
     * @param topicName The name of the topic
     * @return Optional containing the topic if found, empty otherwise
     */
    public Optional<Topic> getTopicByName(String topicName) {
        return Optional.ofNullable(topicsByName.get(topicName));
    }

    /**
     * Removes a topic from the repository.
     *
     * @param topicId The ID of the topic to remove
     * @return true if topic was removed successfully
     */
    public boolean removeTopic(String topicId) {
        Topic topic = topicsById.remove(topicId);
        if (topic != null) {
            topicsByName.remove(topic.getTopicName());
            return true;
        }
        return false;
    }

    /**
     * Checks if a topic exists by its ID.
     *
     * @param topicId The ID of the topic
     * @return true if topic exists
     */
    public boolean topicExists(String topicId) {
        return topicsById.containsKey(topicId);
    }

    /**
     * Checks if a topic exists by its name.
     *
     * @param topicName The name of the topic
     * @return true if topic exists
     */
    public boolean topicExistsByName(String topicName) {
        return topicsByName.containsKey(topicName);
    }

    /**
     * Gets all topics in the repository.
     *
     * @return Map of all topics by ID
     */
    public Map<String, Topic> getAllTopics() {
        return new HashMap<>(topicsById);
    }
}
