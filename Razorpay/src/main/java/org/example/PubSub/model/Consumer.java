package org.example.PubSub.model;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a consumer in the pub-sub system.
 * Consumers subscribe to topics and receive messages from them.
 */
public class Consumer {
    // Getters
    @Getter
    private final String consumerId;
    @Getter
    private final String consumerName;
    @Getter
    private final LocalDateTime createdAt;
    private boolean isActive;

    /**
     * Constructor to create a new consumer.
     *
     * @param consumerName The name of the consumer
     */
    public Consumer(String consumerName) {
        this.consumerId = UUID.randomUUID().toString();
        this.consumerName = consumerName;
        this.createdAt = LocalDateTime.now();
        this.isActive = true;
    }

    public boolean isActive() {
        return isActive;
    }

    // Setters
    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "Consumer{" +
                "consumerId='" + consumerId + '\'' +
                ", consumerName='" + consumerName + '\'' +
                ", createdAt=" + createdAt +
                ", isActive=" + isActive +
                '}';
    }
}
