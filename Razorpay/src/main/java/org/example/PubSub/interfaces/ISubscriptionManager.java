package org.example.PubSub.interfaces;

import org.example.PubSub.model.Subscription;

import java.util.List;

/**
 * Interface for Subscription Management operations
 * Defines contract for managing consumer subscriptions to topics
 * Follows Single Responsibility Principle (SRP)
 */
public interface ISubscriptionManager {
    /**
     * Creates a new subscription
     * @param consumerId The ID of the consumer
     * @param topicId The ID of the topic
     * @return The created Subscription object
     */
    Subscription createSubscription(String consumerId, String topicId);
    
    /**
     * Creates a new subscription with consumer group
     * @param consumerId The ID of the consumer
     * @param topicId The ID of the topic
     * @param consumerGroup The consumer group name
     * @return The created Subscription object
     */
    Subscription createSubscription(String consumerId, String topicId, String consumerGroup);
    
    /**
     * Removes a subscription
     * @param subscriptionId The ID of the subscription to remove
     * @return true if removed successfully, false otherwise
     */
    boolean removeSubscription(String subscriptionId);
    
    /**
     * Gets all subscriptions for a consumer
     * @param consumerId The ID of the consumer
     * @return List of subscriptions
     */
    List<Subscription> getSubscriptionsForConsumer(String consumerId);
    
    /**
     * Gets all subscriptions for a topic
     * @param topicId The ID of the topic
     * @return List of subscriptions
     */
    List<Subscription> getSubscriptionsForTopic(String topicId);
    
    /**
     * Gets a specific subscription
     * @param subscriptionId The ID of the subscription
     * @return The Subscription object, or null if not found
     */
    Subscription getSubscription(String subscriptionId);
}

