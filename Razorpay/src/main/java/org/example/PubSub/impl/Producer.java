package org.example.PubSub.impl;

import org.example.PubSub.interfaces.IProducer;
import org.example.PubSub.model.Message;
import org.example.PubSub.system.PubSubSystem;

import java.util.UUID;

/**
 * Implementation of IProducer interface
 * Responsible for publishing messages to topics
 * Follows Single Responsibility Principle (SRP)
 */
public class Producer implements IProducer {
    private final String producerId;
    private final String producerName;
    private final PubSubSystem pubSubSystem;
    
    public Producer(String producerName, PubSubSystem pubSubSystem) {
        this.producerId = UUID.randomUUID().toString();
        this.producerName = producerName;
        this.pubSubSystem = pubSubSystem;
    }
    
    @Override
    public Message publish(String topicId, String content) {
        if (topicId == null || topicId.isEmpty()) {
            throw new IllegalArgumentException("Topic ID cannot be null or empty");
        }
        if (content == null) {
            throw new IllegalArgumentException("Message content cannot be null");
        }
        
        // Create message with producer information
        Message message = new Message(content, producerId);
        
        // Publish message through PubSubSystem
        pubSubSystem.publishMessage(topicId, message);
        
        System.out.println("[Producer: " + producerName + "] Published message: " + 
                          message.getMessageId() + " to topic: " + topicId);
        
        return message;
    }
    
    @Override
    public String getProducerId() {
        return producerId;
    }
    
    @Override
    public String getProducerName() {
        return producerName;
    }
    
    @Override
    public String toString() {
        return "Producer{" +
                "producerId='" + producerId + '\'' +
                ", producerName='" + producerName + '\'' +
                '}';
    }
}

