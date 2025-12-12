package org.example.PubSub.services;

import org.example.PubSub.model.Subscription;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages offset tracking for subscriptions
 * Provides thread-safe offset management
 * Follows Single Responsibility Principle (SRP)
 */
public class OffsetManager {
    private final Map<String, Integer> offsets;
    
    public OffsetManager() {
        this.offsets = new ConcurrentHashMap<>();
    }
    
    /**
     * Gets the current offset for a subscription
     */
    public int getOffset(String subscriptionId) {
        return offsets.getOrDefault(subscriptionId, 0);
    }
    
    /**
     * Commits (increments) the offset for a subscription
     */
    public void commitOffset(String subscriptionId) {
        offsets.compute(subscriptionId, (k, v) -> (v == null) ? 1 : v + 1);
    }
    
    /**
     * Sets the offset to a specific value for a subscription
     */
    public void seekToOffset(String subscriptionId, int offset) {
        if (offset < 0) {
            throw new IllegalArgumentException("Offset cannot be negative");
        }
        offsets.put(subscriptionId, offset);
    }
    
    /**
     * Resets the offset for a subscription to 0
     */
    public void resetOffset(String subscriptionId) {
        offsets.put(subscriptionId, 0);
    }
    
    /**
     * Removes offset tracking for a subscription
     */
    public void removeOffset(String subscriptionId) {
        offsets.remove(subscriptionId);
    }
    
    /**
     * Updates subscription offset from internal tracking
     */
    public void updateSubscriptionOffset(Subscription subscription) {
        if (subscription != null) {
            int currentOffset = getOffset(subscription.getSubscriptionId());
            subscription.seekToOffset(currentOffset);
        }
    }
    
    /**
     * Synchronizes internal offset with subscription
     */
    public void syncFromSubscription(Subscription subscription) {
        if (subscription != null) {
            offsets.put(subscription.getSubscriptionId(), subscription.getOffset());
        }
    }
}

