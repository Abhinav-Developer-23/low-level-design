package org.example.PubSub.interfaces;

import org.example.PubSub.model.Subscription;

/**
 * Interface for subscribing to topics.
 * Follows the Subscriber pattern from the Observer design pattern.
 * Only supports consumer group subscriptions.
 */
public interface ISubscriber {
    /**
     * Subscribes a consumer group to a topic.
     * Messages published to the topic will be distributed to one consumer in the group
     * using round-robin strategy.
     *
     * @param topicId The ID of the topic to subscribe to
     * @param groupId The ID of the consumer group subscribing
     * @return The created subscription, or null if topic or group doesn't exist
     */
    Subscription subscribe(String topicId, String groupId);

    /**
     * Unsubscribes a consumer group from a topic.
     *
     * @param subscriptionId The ID of the subscription to cancel
     * @return true if unsubscription was successful
     */
    boolean unsubscribe(String subscriptionId);
}
