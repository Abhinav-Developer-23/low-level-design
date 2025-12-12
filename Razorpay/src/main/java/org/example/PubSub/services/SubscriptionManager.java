package org.example.PubSub.services;

import org.example.PubSub.interfaces.ISubscriptionManager;
import org.example.PubSub.model.ConsumerGroup;
import org.example.PubSub.model.Subscription;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Implementation of ISubscriptionManager interface
 * Manages subscriptions between consumers and topics
 * Handles consumer groups for load balancing
 * Thread-safe implementation using ConcurrentHashMap
 * Follows Single Responsibility Principle (SRP)
 */
public class SubscriptionManager implements ISubscriptionManager {
    private final Map<String, Subscription> subscriptions;
    private final Map<String, List<String>> consumerSubscriptions;
    private final Map<String, List<String>> topicSubscriptions;
    private final Map<String, ConsumerGroup> consumerGroups;
    
    public SubscriptionManager() {
        this.subscriptions = new ConcurrentHashMap<>();
        this.consumerSubscriptions = new ConcurrentHashMap<>();
        this.topicSubscriptions = new ConcurrentHashMap<>();
        this.consumerGroups = new ConcurrentHashMap<>();
    }
    
    @Override
    public Subscription createSubscription(String consumerId, String topicId) {
        return createSubscription(consumerId, topicId, null);
    }
    
    @Override
    public Subscription createSubscription(String consumerId, String topicId, String consumerGroup) {
        if (consumerId == null || consumerId.isEmpty()) {
            throw new IllegalArgumentException("Consumer ID cannot be null or empty");
        }
        if (topicId == null || topicId.isEmpty()) {
            throw new IllegalArgumentException("Topic ID cannot be null or empty");
        }
        
        Subscription subscription = new Subscription(consumerId, topicId, consumerGroup);
        String subscriptionId = subscription.getSubscriptionId();
        
        // Store subscription
        subscriptions.put(subscriptionId, subscription);
        
        // Add to consumer subscriptions
        consumerSubscriptions.computeIfAbsent(consumerId, k -> new ArrayList<>())
                             .add(subscriptionId);
        
        // Add to topic subscriptions
        topicSubscriptions.computeIfAbsent(topicId, k -> new ArrayList<>())
                          .add(subscriptionId);
        
        // Handle consumer group if specified
        if (consumerGroup != null && !consumerGroup.isEmpty()) {
            String groupKey = topicId + ":" + consumerGroup;
            ConsumerGroup group = consumerGroups.computeIfAbsent(
                groupKey, k -> new ConsumerGroup(consumerGroup, topicId)
            );
            group.addConsumer(consumerId);
        }
        
        System.out.println("[SubscriptionManager] Created subscription: " + subscriptionId);
        return subscription;
    }
    
    @Override
    public boolean removeSubscription(String subscriptionId) {
        if (subscriptionId == null || subscriptionId.isEmpty()) {
            return false;
        }
        
        Subscription subscription = subscriptions.remove(subscriptionId);
        if (subscription == null) {
            return false;
        }
        
        String consumerId = subscription.getConsumerId();
        String topicId = subscription.getTopicId();
        
        // Remove from consumer subscriptions
        List<String> consumerSubs = consumerSubscriptions.get(consumerId);
        if (consumerSubs != null) {
            consumerSubs.remove(subscriptionId);
            if (consumerSubs.isEmpty()) {
                consumerSubscriptions.remove(consumerId);
            }
        }
        
        // Remove from topic subscriptions
        List<String> topicSubs = topicSubscriptions.get(topicId);
        if (topicSubs != null) {
            topicSubs.remove(subscriptionId);
            if (topicSubs.isEmpty()) {
                topicSubscriptions.remove(topicId);
            }
        }
        
        // Handle consumer group removal
        if (subscription.hasConsumerGroup()) {
            String groupKey = topicId + ":" + subscription.getConsumerGroup();
            ConsumerGroup group = consumerGroups.get(groupKey);
            if (group != null) {
                group.removeConsumer(consumerId);
                if (group.isEmpty()) {
                    consumerGroups.remove(groupKey);
                }
            }
        }
        
        System.out.println("[SubscriptionManager] Removed subscription: " + subscriptionId);
        return true;
    }
    
    @Override
    public List<Subscription> getSubscriptionsForConsumer(String consumerId) {
        if (consumerId == null || consumerId.isEmpty()) {
            return Collections.emptyList();
        }
        
        List<String> subscriptionIds = consumerSubscriptions.get(consumerId);
        if (subscriptionIds == null) {
            return Collections.emptyList();
        }
        
        return subscriptionIds.stream()
                             .map(subscriptions::get)
                             .filter(Objects::nonNull)
                             .collect(Collectors.toList());
    }
    
    @Override
    public List<Subscription> getSubscriptionsForTopic(String topicId) {
        if (topicId == null || topicId.isEmpty()) {
            return Collections.emptyList();
        }
        
        List<String> subscriptionIds = topicSubscriptions.get(topicId);
        if (subscriptionIds == null) {
            return Collections.emptyList();
        }
        
        return subscriptionIds.stream()
                             .map(subscriptions::get)
                             .filter(Objects::nonNull)
                             .collect(Collectors.toList());
    }
    
    @Override
    public Subscription getSubscription(String subscriptionId) {
        if (subscriptionId == null || subscriptionId.isEmpty()) {
            return null;
        }
        return subscriptions.get(subscriptionId);
    }
    
    /**
     * Gets a consumer group for a topic
     */
    public ConsumerGroup getConsumerGroup(String topicId, String groupName) {
        if (topicId == null || groupName == null) {
            return null;
        }
        String groupKey = topicId + ":" + groupName;
        return consumerGroups.get(groupKey);
    }
    
    /**
     * Gets all consumer groups
     */
    public Collection<ConsumerGroup> getAllConsumerGroups() {
        return consumerGroups.values();
    }
}

