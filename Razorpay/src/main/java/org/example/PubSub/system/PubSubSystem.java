package org.example.PubSub.system;

import org.example.PubSub.impl.Consumer;
import org.example.PubSub.interfaces.ISubscriptionManager;
import org.example.PubSub.interfaces.ITopicManager;
import org.example.PubSub.model.Message;
import org.example.PubSub.model.Subscription;
import org.example.PubSub.model.Topic;
import org.example.PubSub.services.OffsetManager;
import org.example.PubSub.services.SubscriptionManager;
import org.example.PubSub.services.TopicManager;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Central coordinator for the Pub-Sub system
 * Manages topics, subscriptions, message publishing, and message delivery
 * Implements Singleton pattern for system-wide access
 * Follows Single Responsibility Principle (SRP) and Dependency Inversion Principle (DIP)
 */
public class PubSubSystem {
    private static PubSubSystem instance;
    
    private final ITopicManager topicManager;
    private final ISubscriptionManager subscriptionManager;
    private final OffsetManager offsetManager;
    private final Map<String, Consumer> consumers;
    private final ExecutorService messageDeliveryExecutor;
    private final ScheduledExecutorService pollingExecutor;
    private volatile boolean isRunning;
    
    /**
     * Private constructor for Singleton pattern
     */
    private PubSubSystem() {
        this.topicManager = new TopicManager();
        this.subscriptionManager = new SubscriptionManager();
        this.offsetManager = new OffsetManager();
        this.consumers = new ConcurrentHashMap<>();
        this.messageDeliveryExecutor = Executors.newFixedThreadPool(10);
        this.pollingExecutor = Executors.newScheduledThreadPool(5);
        this.isRunning = false;
    }
    
    /**
     * Gets the singleton instance of PubSubSystem
     */
    public static synchronized PubSubSystem getInstance() {
        if (instance == null) {
            instance = new PubSubSystem();
        }
        return instance;
    }
    
    /**
     * Creates a new topic
     */
    public Topic createTopic(String topicName) {
        return topicManager.createTopic(topicName);
    }
    
    /**
     * Gets a topic by ID
     */
    public Topic getTopic(String topicId) {
        return topicManager.getTopic(topicId);
    }
    
    /**
     * Gets a topic by name
     */
    public Topic getTopicByName(String topicName) {
        return topicManager.getTopicByName(topicName);
    }
    
    /**
     * Publishes a message to a topic
     */
    public void publishMessage(String topicId, Message message) {
        Topic topic = topicManager.getTopic(topicId);
        if (topic == null) {
            throw new IllegalArgumentException("Topic not found: " + topicId);
        }
        
        // Add message to topic queue
        topic.addMessage(message);
        
        System.out.println("[PubSubSystem] Message published to topic: " + topic.getTopicName());
        
        // Trigger message delivery to subscribers
        deliverMessageToSubscribers(topic, message);
    }
    
    /**
     * Subscribes a consumer to a topic
     */
    public void subscribe(String consumerId, String topicId, String consumerName) {
        subscribe(consumerId, topicId, consumerName, null);
    }
    
    /**
     * Subscribes a consumer to a topic with consumer group
     */
    public void subscribe(String consumerId, String topicId, String consumerName, String consumerGroup) {
        Topic topic = topicManager.getTopic(topicId);
        if (topic == null) {
            throw new IllegalArgumentException("Topic not found: " + topicId);
        }
        
        // Create subscription
        Subscription subscription = subscriptionManager.createSubscription(consumerId, topicId, consumerGroup);
        topic.addSubscription(subscription);
        
        System.out.println("[PubSubSystem] Consumer " + consumerName + 
                          " subscribed to topic: " + topic.getTopicName());
    }
    
    /**
     * Unsubscribes a consumer from a topic
     */
    public void unsubscribe(String consumerId, String topicId) {
        List<Subscription> subscriptions = subscriptionManager.getSubscriptionsForConsumer(consumerId);
        
        for (Subscription subscription : subscriptions) {
            if (subscription.getTopicId().equals(topicId)) {
                Topic topic = topicManager.getTopic(topicId);
                if (topic != null) {
                    topic.removeSubscription(subscription);
                }
                subscriptionManager.removeSubscription(subscription.getSubscriptionId());
                offsetManager.removeOffset(subscription.getSubscriptionId());
            }
        }
    }
    
    /**
     * Registers a consumer with the system
     */
    public void registerConsumer(Consumer consumer) {
        consumers.put(consumer.getConsumerId(), consumer);
        System.out.println("[PubSubSystem] Registered consumer: " + consumer.getConsumerName());
    }
    
    /**
     * Unregisters a consumer from the system
     */
    public void unregisterConsumer(String consumerId) {
        Consumer consumer = consumers.remove(consumerId);
        if (consumer != null) {
            // Remove all subscriptions for this consumer
            List<Subscription> subscriptions = subscriptionManager.getSubscriptionsForConsumer(consumerId);
            for (Subscription subscription : subscriptions) {
                Topic topic = topicManager.getTopic(subscription.getTopicId());
                if (topic != null) {
                    topic.removeSubscription(subscription);
                }
                subscriptionManager.removeSubscription(subscription.getSubscriptionId());
                offsetManager.removeOffset(subscription.getSubscriptionId());
            }
            System.out.println("[PubSubSystem] Unregistered consumer: " + consumer.getConsumerName());
        }
    }
    
    /**
     * Delivers a message to all subscribers of a topic
     */
    private void deliverMessageToSubscribers(Topic topic, Message message) {
        List<Subscription> subscriptions = subscriptionManager.getSubscriptionsForTopic(topic.getTopicId());
        
        for (Subscription subscription : subscriptions) {
            messageDeliveryExecutor.submit(() -> {
                Consumer consumer = consumers.get(subscription.getConsumerId());
                if (consumer != null) {
                    consumer.consume(message);
                    offsetManager.commitOffset(subscription.getSubscriptionId());
                    subscription.commitOffset();
                }
            });
        }
    }
    
    /**
     * Starts the polling mechanism for consumers to fetch messages
     * This allows pull-based consumption
     */
    public void startPolling() {
        if (isRunning) {
            System.out.println("[PubSubSystem] Polling already running");
            return;
        }
        
        isRunning = true;
        System.out.println("[PubSubSystem] Started polling for message delivery");
        
        pollingExecutor.scheduleAtFixedRate(() -> {
            try {
                pollAndDeliverMessages();
            } catch (Exception e) {
                System.err.println("[PubSubSystem] Error in polling: " + e.getMessage());
            }
        }, 0, 500, TimeUnit.MILLISECONDS);
    }
    
    /**
     * Stops the polling mechanism
     */
    public void stopPolling() {
        isRunning = false;
        System.out.println("[PubSubSystem] Stopped polling");
    }
    
    /**
     * Polls topics for messages and delivers to consumers based on their offsets
     */
    private void pollAndDeliverMessages() {
        List<Topic> topics = topicManager.getAllTopics();
        
        for (Topic topic : topics) {
            List<Subscription> subscriptions = subscriptionManager.getSubscriptionsForTopic(topic.getTopicId());
            
            for (Subscription subscription : subscriptions) {
                int currentOffset = subscription.getOffset();
                Message message = topic.getMessageAtOffset(currentOffset);
                
                if (message != null) {
                    Consumer consumer = consumers.get(subscription.getConsumerId());
                    if (consumer != null) {
                        messageDeliveryExecutor.submit(() -> {
                            consumer.consume(message);
                            offsetManager.commitOffset(subscription.getSubscriptionId());
                            subscription.commitOffset();
                        });
                    }
                }
            }
        }
    }
    
    /**
     * Seeks to a specific offset for a subscription
     */
    public void seekToOffset(String consumerId, String topicId, int offset) {
        List<Subscription> subscriptions = subscriptionManager.getSubscriptionsForConsumer(consumerId);
        
        for (Subscription subscription : subscriptions) {
            if (subscription.getTopicId().equals(topicId)) {
                subscription.seekToOffset(offset);
                offsetManager.seekToOffset(subscription.getSubscriptionId(), offset);
                System.out.println("[PubSubSystem] Seeked to offset " + offset + 
                                  " for consumer: " + consumerId);
            }
        }
    }
    
    /**
     * Gets the current offset for a consumer on a topic
     */
    public int getOffset(String consumerId, String topicId) {
        List<Subscription> subscriptions = subscriptionManager.getSubscriptionsForConsumer(consumerId);
        
        for (Subscription subscription : subscriptions) {
            if (subscription.getTopicId().equals(topicId)) {
                return subscription.getOffset();
            }
        }
        return -1;
    }
    
    /**
     * Shuts down the system gracefully
     */
    public void shutdown() {
        stopPolling();
        messageDeliveryExecutor.shutdown();
        pollingExecutor.shutdown();
        
        try {
            if (!messageDeliveryExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                messageDeliveryExecutor.shutdownNow();
            }
            if (!pollingExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                pollingExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            messageDeliveryExecutor.shutdownNow();
            pollingExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        System.out.println("[PubSubSystem] System shut down successfully");
    }
    
    /**
     * Gets system statistics
     */
    public void printSystemStats() {
        System.out.println("\n========== PubSub System Statistics ==========");
        System.out.println("Total Topics: " + topicManager.getAllTopics().size());
        System.out.println("Total Consumers: " + consumers.size());
        
        for (Topic topic : topicManager.getAllTopics()) {
            System.out.println("\nTopic: " + topic.getTopicName());
            System.out.println("  Messages: " + topic.getMessageCount());
            System.out.println("  Subscriptions: " + topic.getSubscriptions().size());
        }
        System.out.println("=============================================\n");
    }
    
    // Getters for managers (for advanced usage)
    public ITopicManager getTopicManager() {
        return topicManager;
    }
    
    public ISubscriptionManager getSubscriptionManager() {
        return subscriptionManager;
    }
    
    public OffsetManager getOffsetManager() {
        return offsetManager;
    }
}

