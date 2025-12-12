package org.example.PubSub.impl;

import org.example.PubSub.enums.ConsumerState;
import org.example.PubSub.interfaces.IConsumer;
import org.example.PubSub.model.Message;
import org.example.PubSub.system.PubSubSystem;

import java.util.UUID;

/**
 * Implementation of IConsumer interface
 * Responsible for consuming and processing messages from topics
 * Follows Single Responsibility Principle (SRP)
 */
public class Consumer implements IConsumer {
    private final String consumerId;
    private final String consumerName;
    private final PubSubSystem pubSubSystem;
    private ConsumerState state;
    private final MessageProcessor messageProcessor;
    
    public Consumer(String consumerName, PubSubSystem pubSubSystem) {
        this(consumerName, pubSubSystem, new DefaultMessageProcessor());
    }
    
    public Consumer(String consumerName, PubSubSystem pubSubSystem, MessageProcessor processor) {
        this.consumerId = UUID.randomUUID().toString();
        this.consumerName = consumerName;
        this.pubSubSystem = pubSubSystem;
        this.state = ConsumerState.ACTIVE;
        this.messageProcessor = processor;
    }
    
    @Override
    public void consume(Message message) {
        if (state != ConsumerState.ACTIVE) {
            System.out.println("[Consumer: " + consumerName + "] Not active. Cannot consume message.");
            return;
        }
        
        if (message == null) {
            return;
        }
        
        System.out.println("[Consumer: " + consumerName + "] Consuming message: " + 
                          message.getMessageId() + " - Content: " + message.getContent());
        
        // Process the message using the injected processor
        messageProcessor.process(message, consumerName);
    }
    
    @Override
    public void subscribe(String topicId) {
        pubSubSystem.subscribe(consumerId, topicId, consumerName);
        System.out.println("[Consumer: " + consumerName + "] Subscribed to topic: " + topicId);
    }
    
    @Override
    public void unsubscribe(String topicId) {
        pubSubSystem.unsubscribe(consumerId, topicId);
        System.out.println("[Consumer: " + consumerName + "] Unsubscribed from topic: " + topicId);
    }
    
    @Override
    public String getConsumerId() {
        return consumerId;
    }
    
    @Override
    public String getConsumerName() {
        return consumerName;
    }
    
    @Override
    public ConsumerState getState() {
        return state;
    }
    
    @Override
    public void setState(ConsumerState state) {
        this.state = state;
        System.out.println("[Consumer: " + consumerName + "] State changed to: " + state);
    }
    
    @Override
    public String toString() {
        return "Consumer{" +
                "consumerId='" + consumerId + '\'' +
                ", consumerName='" + consumerName + '\'' +
                ", state=" + state +
                '}';
    }
    
    /**
     * Strategy interface for message processing
     * Allows different processing strategies to be injected
     */
    public interface MessageProcessor {
        void process(Message message, String consumerName);
    }
    
    /**
     * Default implementation of MessageProcessor
     * Simply logs the message processing
     */
    private static class DefaultMessageProcessor implements MessageProcessor {
        @Override
        public void process(Message message, String consumerName) {
            // Simulate message processing
            try {
                Thread.sleep(100); // Simulate processing time
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("[Consumer: " + consumerName + "] Successfully processed message: " + 
                              message.getMessageId());
        }
    }
}

