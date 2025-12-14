package org.example.PubSub.repository;

import org.example.PubSub.model.Consumer;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Repository class for managing consumers.
 * Implements Singleton pattern to ensure only one instance exists.
 * Uses HashMap for storing consumers by consumerId.
 */
public class ConsumerRepository {
    private static ConsumerRepository instance;
    private final Map<String, Consumer> consumers; // Map of consumerId -> Consumer

    /**
     * Private constructor to prevent direct instantiation.
     */
    private ConsumerRepository() {
        this.consumers = new HashMap<>();
    }

    /**
     * Returns the singleton instance of ConsumerRepository.
     * Uses synchronized method for thread safety.
     *
     * @return The singleton instance
     */
    public static synchronized ConsumerRepository getInstance() {
        if (instance == null) {
            instance = new ConsumerRepository();
        }
        return instance;
    }

    /**
     * Adds a consumer to the repository.
     *
     * @param consumer The consumer to add
     * @return true if consumer was added successfully
     */
    public boolean addConsumer(Consumer consumer) {
        if (consumer == null) {
            return false;
        }
        consumers.put(consumer.getConsumerId(), consumer);
        return true;
    }

    /**
     * Retrieves a consumer by its ID.
     *
     * @param consumerId The ID of the consumer
     * @return Optional containing the consumer if found, empty otherwise
     */
    public Optional<Consumer> getConsumerById(String consumerId) {
        return Optional.ofNullable(consumers.get(consumerId));
    }

    /**
     * Removes a consumer from the repository.
     *
     * @param consumerId The ID of the consumer to remove
     * @return true if consumer was removed successfully
     */
    public boolean removeConsumer(String consumerId) {
        return consumers.remove(consumerId) != null;
    }

    /**
     * Checks if a consumer exists.
     *
     * @param consumerId The ID of the consumer
     * @return true if consumer exists
     */
    public boolean consumerExists(String consumerId) {
        return consumers.containsKey(consumerId);
    }

    /**
     * Gets all consumers in the repository.
     *
     * @return Map of all consumers by ID
     */
    public Map<String, Consumer> getAllConsumers() {
        return new HashMap<>(consumers);
    }
}
