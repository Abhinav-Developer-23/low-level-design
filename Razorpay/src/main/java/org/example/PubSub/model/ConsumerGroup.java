package org.example.PubSub.model;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents a consumer group in the pub-sub system.
 * Consumer groups allow multiple consumers to share the workload,
 * where each message is processed by only one consumer in the group.
 */
public class ConsumerGroup {
    // Getters
    @Getter
    private final String groupId;
    @Getter
    private final String groupName;
    @Getter
    private final LocalDateTime createdAt;
    private final Map<String, Consumer> consumers; // Map of consumerId -> Consumer
    private int currentConsumerIndex; // For round-robin distribution

    /**
     * Constructor to create a new consumer group.
     *
     * @param groupName The name of the consumer group
     */
    public ConsumerGroup(String groupName) {
        this.groupId = UUID.randomUUID().toString();
        this.groupName = groupName;
        this.createdAt = LocalDateTime.now();
        this.consumers = new ConcurrentHashMap<>();
        this.currentConsumerIndex = 0;
    }

    /**
     * Adds a consumer to this group.
     *
     * @param consumer The consumer to add
     */
    public void addConsumer(Consumer consumer) {
        if (consumer != null && consumer.isActive()) {
            consumers.put(consumer.getConsumerId(), consumer);
        }
    }

    /**
     * Removes a consumer from this group.
     *
     * @param consumerId The ID of the consumer to remove
     */
    public void removeConsumer(String consumerId) {
        consumers.remove(consumerId);
    }

    /**
     * Gets an active consumer from the group using round-robin strategy.
     *
     * @return An active consumer, or null if no active consumers exist
     */
    public Consumer getNextConsumer() {
        List<Consumer> activeConsumers = getActiveConsumers();
        if (activeConsumers.isEmpty()) {
            return null;
        }
        
        // Round-robin selection
        Consumer selected = activeConsumers.get(currentConsumerIndex % activeConsumers.size());
        currentConsumerIndex = (currentConsumerIndex + 1) % activeConsumers.size();
        return selected;
    }

    /**
     * Gets all active consumers in the group.
     *
     * @return List of active consumers
     */
    public List<Consumer> getActiveConsumers() {
        List<Consumer> activeConsumers = new ArrayList<>();
        for (Consumer consumer : consumers.values()) {
            if (consumer.isActive()) {
                activeConsumers.add(consumer);
            }
        }
        return activeConsumers;
    }

    /**
     * Gets the number of active consumers in the group.
     *
     * @return Count of active consumers
     */
    public int getActiveConsumerCount() {
        return getActiveConsumers().size();
    }

    public Map<String, Consumer> getConsumers() {
        return Collections.unmodifiableMap(consumers);
    }

    @Override
    public String toString() {
        return "ConsumerGroup{" +
                "groupId='" + groupId + '\'' +
                ", groupName='" + groupName + '\'' +
                ", createdAt=" + createdAt +
                ", activeConsumerCount=" + getActiveConsumerCount() +
                '}';
    }
}
