package org.example.PubSub.service;

import org.example.PubSub.enums.MessageStatus;
import org.example.PubSub.enums.SubscriptionType;
import org.example.PubSub.interfaces.IMessageHandler;
import org.example.PubSub.interfaces.IPublisher;
import org.example.PubSub.interfaces.ISubscriber;
import org.example.PubSub.model.*;
import org.example.PubSub.repository.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Main service class for the Pub-Sub system.
 * Implements both IPublisher and ISubscriber interfaces.
 * Handles message publishing, subscription management, and message distribution.
 * Follows Single Responsibility Principle - manages pub-sub operations.
 */
public class PubSubService implements IPublisher, ISubscriber {
    private final TopicRepository topicRepository;
    private final ConsumerRepository consumerRepository;
    private final ConsumerGroupRepository consumerGroupRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final Map<String, IMessageHandler> messageHandlers; // Map of consumerId -> MessageHandler

    /**
     * Constructor initializes repositories and message handlers map.
     */
    public PubSubService() {
        this.topicRepository = TopicRepository.getInstance();
        this.consumerRepository = ConsumerRepository.getInstance();
        this.consumerGroupRepository = ConsumerGroupRepository.getInstance();
        this.subscriptionRepository = SubscriptionRepository.getInstance();
        this.messageHandlers = new HashMap<>();
    }

    // ========== IPublisher Implementation ==========

    @Override
    public Topic createTopic(String topicName) {
        if (topicName == null || topicName.trim().isEmpty()) {
            return null;
        }
        
        // Check if topic already exists
        if (topicRepository.topicExistsByName(topicName)) {
            return null;
        }
        
        Topic topic = new Topic(topicName);
        if (topicRepository.addTopic(topic)) {
            return topic;
        }
        return null;
    }

    @Override
    public Message publish(String topicId, String payload) {
        Optional<Topic> topicOpt = topicRepository.getTopicById(topicId);
        if (topicOpt.isEmpty()) {
            return null;
        }
        
        Topic topic = topicOpt.get();
        Message message = new Message(topicId, payload);
        
        // Add message to topic's queue
        topic.addMessage(message);
        
        // Distribute message to all subscribers
        distributeMessage(topic, message);
        
        return message;
    }

    // ========== ISubscriber Implementation ==========

    @Override
    public Subscription subscribe(String topicId, String groupId) {
        // Validate topic and consumer group exist
        if (!topicRepository.topicExists(topicId) || 
            !consumerGroupRepository.consumerGroupExists(groupId)) {
            return null;
        }
        
        Subscription subscription = new Subscription(
            topicId, 
            groupId, 
            SubscriptionType.GROUP
        );
        
        if (subscriptionRepository.addSubscription(subscription)) {
            return subscription;
        }
        return null;
    }

    @Override
    public boolean unsubscribe(String subscriptionId) {
        return subscriptionRepository.deactivateSubscription(subscriptionId);
    }

    // ========== Consumer and ConsumerGroup Management ==========

    /**
     * Creates a new consumer.
     *
     * @param consumerName The name of the consumer
     * @return The created consumer, or null if creation failed
     */
    public Consumer createConsumer(String consumerName) {
        if (consumerName == null || consumerName.trim().isEmpty()) {
            return null;
        }
        
        Consumer consumer = new Consumer(consumerName);
        if (consumerRepository.addConsumer(consumer)) {
            return consumer;
        }
        return null;
    }

    /**
     * Creates a new consumer group.
     *
     * @param groupName The name of the consumer group
     * @return The created consumer group, or null if creation failed
     */
    public ConsumerGroup createConsumerGroup(String groupName) {
        if (groupName == null || groupName.trim().isEmpty()) {
            return null;
        }
        
        ConsumerGroup consumerGroup = new ConsumerGroup(groupName);
        if (consumerGroupRepository.addConsumerGroup(consumerGroup)) {
            return consumerGroup;
        }
        return null;
    }

    /**
     * Adds a consumer to a consumer group.
     *
     * @param groupId The ID of the consumer group
     * @param consumerId The ID of the consumer to add
     * @return true if consumer was added successfully
     */
    public boolean addConsumerToGroup(String groupId, String consumerId) {
        Optional<ConsumerGroup> groupOpt = consumerGroupRepository.getConsumerGroupById(groupId);
        Optional<Consumer> consumerOpt = consumerRepository.getConsumerById(consumerId);
        
        if (groupOpt.isEmpty() || consumerOpt.isEmpty()) {
            return false;
        }
        
        ConsumerGroup group = groupOpt.get();
        Consumer consumer = consumerOpt.get();
        group.addConsumer(consumer);
        return true;
    }

    /**
     * Registers a message handler for a consumer.
     *
     * @param consumerId The ID of the consumer
     * @param handler The message handler implementation
     */
    public void registerMessageHandler(String consumerId, IMessageHandler handler) {
        if (consumerId != null && handler != null) {
            messageHandlers.put(consumerId, handler);
        }
    }

    // ========== Message Distribution Logic ==========

    /**
     * Distributes a message to all subscribers of a topic.
     * Only consumer groups are supported - each group receives the message
     * and distributes it to one consumer in the group using round-robin.
     *
     * @param topic The topic the message belongs to
     * @param message The message to distribute
     */
    private void distributeMessage(Topic topic, Message message) {
        List<Subscription> subscriptions = subscriptionRepository.getSubscriptionsByTopic(topic.getTopicId());
        
        for (Subscription subscription : subscriptions) {
            if (!subscription.isActive()) {
                continue;
            }
            
            // Only group subscriptions are supported
            if (subscription.getSubscriptionType() == SubscriptionType.GROUP) {
                // Group subscription - deliver to one consumer in the group
                deliverToConsumerGroup(subscription.getSubscriberId(), message);
            }
        }
    }

    /**
     * Delivers a message to a consumer group (round-robin distribution).
     *
     * @param groupId The ID of the consumer group
     * @param message The message to deliver
     */
    private void deliverToConsumerGroup(String groupId, Message message) {
        Optional<ConsumerGroup> groupOpt = consumerGroupRepository.getConsumerGroupById(groupId);
        if (groupOpt.isEmpty()) {
            return;
        }
        
        ConsumerGroup group = groupOpt.get();
        Consumer selectedConsumer = group.getNextConsumer();
        
        if (selectedConsumer != null && selectedConsumer.isActive()) {
            // Get message handler for the selected consumer
            IMessageHandler handler = messageHandlers.get(selectedConsumer.getConsumerId());
            if (handler != null) {
                boolean success = handler.handleMessage(message);
                if (success) {
                    message.setStatus(MessageStatus.PROCESSED);
                    message.setProcessedBy(groupId + ":" + selectedConsumer.getConsumerId());
                } else {
                    message.setStatus(MessageStatus.FAILED);
                }
            }
        }
    }

    // ========== Utility Methods ==========

    /**
     * Gets a topic by name.
     *
     * @param topicName The name of the topic
     * @return Optional containing the topic if found
     */
    public Optional<Topic> getTopicByName(String topicName) {
        return topicRepository.getTopicByName(topicName);
    }

    /**
     * Gets a consumer by ID.
     *
     * @param consumerId The ID of the consumer
     * @return Optional containing the consumer if found
     */
    public Optional<Consumer> getConsumerById(String consumerId) {
        return consumerRepository.getConsumerById(consumerId);
    }

    /**
     * Gets a consumer group by ID.
     *
     * @param groupId The ID of the consumer group
     * @return Optional containing the consumer group if found
     */
    public Optional<ConsumerGroup> getConsumerGroupById(String groupId) {
        return consumerGroupRepository.getConsumerGroupById(groupId);
    }
}
