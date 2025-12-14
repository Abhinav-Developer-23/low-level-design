package org.example.PubSub.model;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Represents a topic in the pub-sub system.
 * Topics are channels where messages are published and from which consumers subscribe.
 */
public class Topic {
    private final String topicId;
    private final String topicName;
    private final LocalDateTime createdAt;
    private final BlockingQueue<Message> messageQueue; // Queue to hold messages

    /**
     * Constructor to create a new topic.
     *
     * @param topicName The name of the topic
     */
    public Topic(String topicName) {
        this.topicId = UUID.randomUUID().toString();
        this.topicName = topicName;
        this.createdAt = LocalDateTime.now();
        this.messageQueue = new LinkedBlockingQueue<>();
    }

    // Getters
    public String getTopicId() {
        return topicId;
    }

    public String getTopicName() {
        return topicName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public BlockingQueue<Message> getMessageQueue() {
        return messageQueue;
    }

    /**
     * Adds a message to the topic's message queue.
     *
     * @param message The message to add
     * @return true if message was added successfully
     */
    public boolean addMessage(Message message) {
        return messageQueue.offer(message);
    }

    /**
     * Retrieves and removes a message from the topic's message queue.
     *
     * @return The message, or null if queue is empty
     */
    public Message pollMessage() {
        return messageQueue.poll();
    }

    /**
     * Gets the number of messages currently in the queue.
     *
     * @return The size of the message queue
     */
    public int getMessageCount() {
        return messageQueue.size();
    }

    @Override
    public String toString() {
        return "Topic{" +
                "topicId='" + topicId + '\'' +
                ", topicName='" + topicName + '\'' +
                ", createdAt=" + createdAt +
                ", messageCount=" + messageQueue.size() +
                '}';
    }
}
