package org.example.PubSub.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Represents a topic in the pub-sub system
 * Topics store messages in a queue and maintain list of subscriptions
 * Thread-safe implementation using ConcurrentLinkedQueue
 */
public class Topic {
    private final String topicId;
    private final String topicName;
    private final Queue<Message> messageQueue;
    private final List<Subscription> subscriptions;
    private final Object lock = new Object();
    
    public Topic(String topicId, String topicName) {
        this.topicId = topicId;
        this.topicName = topicName;
        this.messageQueue = new ConcurrentLinkedQueue<>();
        this.subscriptions = new ArrayList<>();
    }
    
    /**
     * Adds a message to the topic's queue
     */
    public void addMessage(Message message) {
        messageQueue.offer(message);
    }
    
    /**
     * Gets the message at a specific offset
     */
    public Message getMessageAtOffset(int offset) {
        if (offset < 0 || offset >= messageQueue.size()) {
            return null;
        }
        int currentIndex = 0;
        for (Message message : messageQueue) {
            if (currentIndex == offset) {
                return message;
            }
            currentIndex++;
        }
        return null;
    }
    
    /**
     * Adds a subscription to this topic
     */
    public void addSubscription(Subscription subscription) {
        synchronized (lock) {
            subscriptions.add(subscription);
        }
    }
    
    /**
     * Removes a subscription from this topic
     */
    public void removeSubscription(Subscription subscription) {
        synchronized (lock) {
            subscriptions.remove(subscription);
        }
    }
    
    // Getters
    public String getTopicId() {
        return topicId;
    }
    
    public String getTopicName() {
        return topicName;
    }
    
    public Queue<Message> getMessageQueue() {
        return messageQueue;
    }
    
    public List<Subscription> getSubscriptions() {
        synchronized (lock) {
            return new ArrayList<>(subscriptions);
        }
    }
    
    public int getMessageCount() {
        return messageQueue.size();
    }
    
    @Override
    public String toString() {
        return "Topic{" +
                "topicId='" + topicId + '\'' +
                ", topicName='" + topicName + '\'' +
                ", messageCount=" + messageQueue.size() +
                ", subscriptionsCount=" + subscriptions.size() +
                '}';
    }
}

