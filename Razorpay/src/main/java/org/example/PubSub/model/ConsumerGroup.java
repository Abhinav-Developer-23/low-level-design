package org.example.PubSub.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents a consumer group for load balancing
 * Multiple consumers in same group share message consumption
 */
public class ConsumerGroup {
    private final String groupId;
    private final String topicId;
    private final List<String> consumerIds;
    private final AtomicInteger roundRobinCounter;
    private int sharedOffset;
    
    public ConsumerGroup(String groupId, String topicId) {
        this.groupId = groupId;
        this.topicId = topicId;
        this.consumerIds = new ArrayList<>();
        this.roundRobinCounter = new AtomicInteger(0);
        this.sharedOffset = 0;
    }
    
    /**
     * Adds a consumer to this group
     */
    public void addConsumer(String consumerId) {
        if (!consumerIds.contains(consumerId)) {
            consumerIds.add(consumerId);
        }
    }
    
    /**
     * Removes a consumer from this group
     */
    public void removeConsumer(String consumerId) {
        consumerIds.remove(consumerId);
    }
    
    /**
     * Gets the next consumer in round-robin fashion
     */
    public String getNextConsumer() {
        if (consumerIds.isEmpty()) {
            return null;
        }
        int index = roundRobinCounter.getAndIncrement() % consumerIds.size();
        return consumerIds.get(index);
    }
    
    /**
     * Commits the shared offset for the group
     */
    public void commitOffset() {
        this.sharedOffset++;
    }
    
    // Getters
    public String getGroupId() {
        return groupId;
    }
    
    public String getTopicId() {
        return topicId;
    }
    
    public List<String> getConsumerIds() {
        return new ArrayList<>(consumerIds);
    }
    
    public int getSharedOffset() {
        return sharedOffset;
    }
    
    public boolean isEmpty() {
        return consumerIds.isEmpty();
    }
    
    @Override
    public String toString() {
        return "ConsumerGroup{" +
                "groupId='" + groupId + '\'' +
                ", topicId='" + topicId + '\'' +
                ", consumerCount=" + consumerIds.size() +
                ", sharedOffset=" + sharedOffset +
                '}';
    }
}

