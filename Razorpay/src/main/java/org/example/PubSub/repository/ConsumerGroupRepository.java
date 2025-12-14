package org.example.PubSub.repository;

import org.example.PubSub.model.ConsumerGroup;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Repository class for managing consumer groups.
 * Implements Singleton pattern to ensure only one instance exists.
 * Uses HashMap for storing consumer groups by groupId.
 */
public class ConsumerGroupRepository {
    private static ConsumerGroupRepository instance;
    private final Map<String, ConsumerGroup> consumerGroups; // Map of groupId -> ConsumerGroup

    /**
     * Private constructor to prevent direct instantiation.
     */
    private ConsumerGroupRepository() {
        this.consumerGroups = new HashMap<>();
    }

    /**
     * Returns the singleton instance of ConsumerGroupRepository.
     * Uses synchronized method for thread safety.
     *
     * @return The singleton instance
     */
    public static synchronized ConsumerGroupRepository getInstance() {
        if (instance == null) {
            instance = new ConsumerGroupRepository();
        }
        return instance;
    }

    /**
     * Adds a consumer group to the repository.
     *
     * @param consumerGroup The consumer group to add
     * @return true if consumer group was added successfully
     */
    public boolean addConsumerGroup(ConsumerGroup consumerGroup) {
        if (consumerGroup == null) {
            return false;
        }
        consumerGroups.put(consumerGroup.getGroupId(), consumerGroup);
        return true;
    }

    /**
     * Retrieves a consumer group by its ID.
     *
     * @param groupId The ID of the consumer group
     * @return Optional containing the consumer group if found, empty otherwise
     */
    public Optional<ConsumerGroup> getConsumerGroupById(String groupId) {
        return Optional.ofNullable(consumerGroups.get(groupId));
    }

    /**
     * Removes a consumer group from the repository.
     *
     * @param groupId The ID of the consumer group to remove
     * @return true if consumer group was removed successfully
     */
    public boolean removeConsumerGroup(String groupId) {
        return consumerGroups.remove(groupId) != null;
    }

    /**
     * Checks if a consumer group exists.
     *
     * @param groupId The ID of the consumer group
     * @return true if consumer group exists
     */
    public boolean consumerGroupExists(String groupId) {
        return consumerGroups.containsKey(groupId);
    }

    /**
     * Gets all consumer groups in the repository.
     *
     * @return Map of all consumer groups by ID
     */
    public Map<String, ConsumerGroup> getAllConsumerGroups() {
        return new HashMap<>(consumerGroups);
    }
}
