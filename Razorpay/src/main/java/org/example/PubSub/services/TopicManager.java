package org.example.PubSub.services;

import org.example.PubSub.interfaces.ITopicManager;
import org.example.PubSub.model.Topic;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of ITopicManager interface
 * Manages topic creation, retrieval, and deletion
 * Thread-safe implementation using ConcurrentHashMap
 * Follows Single Responsibility Principle (SRP)
 */
public class TopicManager implements ITopicManager {
    private final Map<String, Topic> topics;
    private final Map<String, String> topicNameToIdMap;
    
    public TopicManager() {
        this.topics = new ConcurrentHashMap<>();
        this.topicNameToIdMap = new ConcurrentHashMap<>();
    }
    
    @Override
    public Topic createTopic(String topicName) {
        if (topicName == null || topicName.isEmpty()) {
            throw new IllegalArgumentException("Topic name cannot be null or empty");
        }
        
        // Check if topic already exists
        if (topicNameToIdMap.containsKey(topicName)) {
            throw new IllegalStateException("Topic with name '" + topicName + "' already exists");
        }
        
        String topicId = UUID.randomUUID().toString();
        Topic topic = new Topic(topicId, topicName);
        
        topics.put(topicId, topic);
        topicNameToIdMap.put(topicName, topicId);
        
        System.out.println("[TopicManager] Created topic: " + topicName + " with ID: " + topicId);
        return topic;
    }
    
    @Override
    public Topic getTopic(String topicId) {
        if (topicId == null || topicId.isEmpty()) {
            return null;
        }
        return topics.get(topicId);
    }
    
    @Override
    public Topic getTopicByName(String topicName) {
        if (topicName == null || topicName.isEmpty()) {
            return null;
        }
        String topicId = topicNameToIdMap.get(topicName);
        return topicId != null ? topics.get(topicId) : null;
    }
    
    @Override
    public boolean deleteTopic(String topicId) {
        if (topicId == null || topicId.isEmpty()) {
            return false;
        }
        
        Topic topic = topics.remove(topicId);
        if (topic != null) {
            topicNameToIdMap.remove(topic.getTopicName());
            System.out.println("[TopicManager] Deleted topic: " + topic.getTopicName());
            return true;
        }
        return false;
    }
    
    @Override
    public List<Topic> getAllTopics() {
        return new ArrayList<>(topics.values());
    }
}

