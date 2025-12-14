package org.example.PubSub.repository;

import org.example.PubSub.model.Subscription;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Repository class for managing subscriptions.
 * Implements Singleton pattern to ensure only one instance exists.
 * Uses HashMap for storing subscriptions by subscriptionId.
 */
public class SubscriptionRepository {
    private static SubscriptionRepository instance;
    private final Map<String, Subscription> subscriptions; // Map of subscriptionId -> Subscription
    private final Map<String, List<String>> subscriptionsByTopic; // Map of topicId -> List of subscriptionIds
    private final Map<String, List<String>> subscriptionsBySubscriber; // Map of subscriberId -> List of subscriptionIds

    /**
     * Private constructor to prevent direct instantiation.
     */
    private SubscriptionRepository() {
        this.subscriptions = new HashMap<>();
        this.subscriptionsByTopic = new HashMap<>();
        this.subscriptionsBySubscriber = new HashMap<>();
    }

    /**
     * Returns the singleton instance of SubscriptionRepository.
     * Uses synchronized method for thread safety.
     *
     * @return The singleton instance
     */
    public static synchronized SubscriptionRepository getInstance() {
        if (instance == null) {
            instance = new SubscriptionRepository();
        }
        return instance;
    }

    /**
     * Adds a subscription to the repository.
     *
     * @param subscription The subscription to add
     * @return true if subscription was added successfully
     */
    public boolean addSubscription(Subscription subscription) {
        if (subscription == null) {
            return false;
        }
        
        subscriptions.put(subscription.getSubscriptionId(), subscription);
        
        // Update topic index
        subscriptionsByTopic.computeIfAbsent(subscription.getTopicId(), k -> new ArrayList<>())
                .add(subscription.getSubscriptionId());
        
        // Update subscriber index
        subscriptionsBySubscriber.computeIfAbsent(subscription.getSubscriberId(), k -> new ArrayList<>())
                .add(subscription.getSubscriptionId());
        
        return true;
    }

    /**
     * Retrieves a subscription by its ID.
     *
     * @param subscriptionId The ID of the subscription
     * @return Optional containing the subscription if found, empty otherwise
     */
    public Optional<Subscription> getSubscriptionById(String subscriptionId) {
        return Optional.ofNullable(subscriptions.get(subscriptionId));
    }

    /**
     * Gets all active subscriptions for a topic.
     *
     * @param topicId The ID of the topic
     * @return List of active subscriptions for the topic
     */
    public List<Subscription> getSubscriptionsByTopic(String topicId) {
        List<String> subscriptionIds = subscriptionsByTopic.getOrDefault(topicId, new ArrayList<>());
        return subscriptionIds.stream()
                .map(subscriptions::get)
                .filter(Objects::nonNull)
                .filter(Subscription::isActive)
                .collect(Collectors.toList());
    }

    /**
     * Gets all active subscriptions for a subscriber (consumer or group).
     *
     * @param subscriberId The ID of the subscriber
     * @return List of active subscriptions for the subscriber
     */
    public List<Subscription> getSubscriptionsBySubscriber(String subscriberId) {
        List<String> subscriptionIds = subscriptionsBySubscriber.getOrDefault(subscriberId, new ArrayList<>());
        return subscriptionIds.stream()
                .map(subscriptions::get)
                .filter(Objects::nonNull)
                .filter(Subscription::isActive)
                .collect(Collectors.toList());
    }

    /**
     * Removes a subscription from the repository.
     *
     * @param subscriptionId The ID of the subscription to remove
     * @return true if subscription was removed successfully
     */
    public boolean removeSubscription(String subscriptionId) {
        Subscription subscription = subscriptions.remove(subscriptionId);
        if (subscription != null) {
            // Remove from topic index
            List<String> topicSubscriptions = subscriptionsByTopic.get(subscription.getTopicId());
            if (topicSubscriptions != null) {
                topicSubscriptions.remove(subscriptionId);
            }
            
            // Remove from subscriber index
            List<String> subscriberSubscriptions = subscriptionsBySubscriber.get(subscription.getSubscriberId());
            if (subscriberSubscriptions != null) {
                subscriberSubscriptions.remove(subscriptionId);
            }
            
            return true;
        }
        return false;
    }

    /**
     * Deactivates a subscription (marks it as inactive).
     *
     * @param subscriptionId The ID of the subscription to deactivate
     * @return true if subscription was deactivated successfully
     */
    public boolean deactivateSubscription(String subscriptionId) {
        Subscription subscription = subscriptions.get(subscriptionId);
        if (subscription != null) {
            subscription.setActive(false);
            return true;
        }
        return false;
    }

    /**
     * Checks if a subscription exists.
     *
     * @param subscriptionId The ID of the subscription
     * @return true if subscription exists
     */
    public boolean subscriptionExists(String subscriptionId) {
        return subscriptions.containsKey(subscriptionId);
    }

    /**
     * Gets all subscriptions in the repository.
     *
     * @return Map of all subscriptions by ID
     */
    public Map<String, Subscription> getAllSubscriptions() {
        return new HashMap<>(subscriptions);
    }
}
