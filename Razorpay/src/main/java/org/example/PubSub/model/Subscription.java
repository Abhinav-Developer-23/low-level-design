package org.example.PubSub.model;

import java.util.UUID;

/**
 * Represents a subscription linking a consumer to a topic
 * Manages offset tracking for each consumer on a topic
 */
public class Subscription {
    private final String subscriptionId;
    private final String consumerId;
    private final String topicId;
    private int offset;  // Current offset for this consumer
    private final String consumerGroup;
    
    public Subscription(String consumerId, String topicId, String consumerGroup) {
        this.subscriptionId = UUID.randomUUID().toString();
        this.consumerId = consumerId;
        this.topicId = topicId;
        this.consumerGroup = consumerGroup;
        this.offset = 0;  // Start from beginning
    }
    
    public Subscription(String consumerId, String topicId) {
        this(consumerId, topicId, null);  // No consumer group
    }
    
    /**
     * Updates the offset to the next position
     */
    public void commitOffset() {
        this.offset++;
    }
    
    /**
     * Sets the offset to a specific position (for seeking)
     */
    public void seekToOffset(int newOffset) {
        this.offset = Math.max(0, newOffset);
    }
    
    /**
     * Resets the offset to the beginning
     */
    public void resetOffset() {
        this.offset = 0;
    }
    
    // Getters
    public String getSubscriptionId() {
        return subscriptionId;
    }
    
    public String getConsumerId() {
        return consumerId;
    }
    
    public String getTopicId() {
        return topicId;
    }
    
    public int getOffset() {
        return offset;
    }
    
    public String getConsumerGroup() {
        return consumerGroup;
    }
    
    public boolean hasConsumerGroup() {
        return consumerGroup != null && !consumerGroup.isEmpty();
    }
    
    @Override
    public String toString() {
        return "Subscription{" +
                "subscriptionId='" + subscriptionId + '\'' +
                ", consumerId='" + consumerId + '\'' +
                ", topicId='" + topicId + '\'' +
                ", offset=" + offset +
                ", consumerGroup='" + consumerGroup + '\'' +
                '}';
    }
}

