package org.example.PubSub.model;

import org.example.PubSub.enums.MessageStatus;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a message in the pub-sub system
 * Encapsulates message content, metadata, and tracking information
 */
public class Message {
    private final String messageId;
    private final String content;
    private final LocalDateTime timestamp;
    private MessageStatus status;
    private int retryCount;
    private final String producerId;
    
    public Message(String content, String producerId) {
        this.messageId = UUID.randomUUID().toString();
        this.content = content;
        this.producerId = producerId;
        this.timestamp = LocalDateTime.now();
        this.status = MessageStatus.PUBLISHED;
        this.retryCount = 0;
    }
    
    // Getters
    public String getMessageId() {
        return messageId;
    }
    
    public String getContent() {
        return content;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public MessageStatus getStatus() {
        return status;
    }
    
    public int getRetryCount() {
        return retryCount;
    }
    
    public String getProducerId() {
        return producerId;
    }
    
    // Setters for mutable fields
    public void setStatus(MessageStatus status) {
        this.status = status;
    }
    
    public void incrementRetryCount() {
        this.retryCount++;
    }
    
    @Override
    public String toString() {
        return "Message{" +
                "messageId='" + messageId + '\'' +
                ", content='" + content + '\'' +
                ", timestamp=" + timestamp +
                ", status=" + status +
                ", retryCount=" + retryCount +
                ", producerId='" + producerId + '\'' +
                '}';
    }
}

