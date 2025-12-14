package org.example.PubSub.model;

import lombok.Getter;
import org.example.PubSub.enums.SubscriptionType;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a subscription in the pub-sub system.
 * A subscription connects a consumer or consumer group to a topic.
 */
public class Subscription {
    // Getters
    @Getter
    private final String subscriptionId;
    @Getter
    private final String topicId;
    @Getter
    private final String subscriberId; // Can be consumerId or groupId
    @Getter
    private final SubscriptionType subscriptionType;
    @Getter
    private final LocalDateTime createdAt;
    private boolean isActive;

    /**
     * Constructor to create a new subscription.
     *
     * @param topicId The ID of the topic being subscribed to
     * @param subscriberId The ID of the consumer or consumer group
     * @param subscriptionType The type of subscription (INDIVIDUAL or GROUP)
     */
    public Subscription(String topicId, String subscriberId, SubscriptionType subscriptionType) {
        this.subscriptionId = UUID.randomUUID().toString();
        this.topicId = topicId;
        this.subscriberId = subscriberId;
        this.subscriptionType = subscriptionType;
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
        return "Subscription{" +
                "subscriptionId='" + subscriptionId + '\'' +
                ", topicId='" + topicId + '\'' +
                ", subscriberId='" + subscriberId + '\'' +
                ", subscriptionType=" + subscriptionType +
                ", createdAt=" + createdAt +
                ", isActive=" + isActive +
                '}';
    }
}
