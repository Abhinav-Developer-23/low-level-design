package org.example.model;

import org.example.enums.MessageStatus;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a message between two connected users
 */
public class Message {
    private final String messageId;
    private final String senderId;
    private final String receiverId;
    private final String content;
    private MessageStatus status;
    private final LocalDateTime sentAt;
    private LocalDateTime readAt;
    private final String conversationId;

    public Message(String senderId, String receiverId, String content) {
        this.messageId = UUID.randomUUID().toString();
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.status = MessageStatus.SENT;
        this.sentAt = LocalDateTime.now();
        // Create a consistent conversation ID for both users
        this.conversationId = generateConversationId(senderId, receiverId);
    }

    private String generateConversationId(String userId1, String userId2) {
        // Ensure consistent conversation ID regardless of who sent the message
        return userId1.compareTo(userId2) < 0 
            ? userId1 + "_" + userId2 
            : userId2 + "_" + userId1;
    }

    // Getters and Setters
    public String getMessageId() {
        return messageId;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getContent() {
        return content;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public LocalDateTime getReadAt() {
        return readAt;
    }

    public void markAsRead() {
        this.status = MessageStatus.READ;
        this.readAt = LocalDateTime.now();
    }

    public void markAsDelivered() {
        this.status = MessageStatus.DELIVERED;
    }

    public String getConversationId() {
        return conversationId;
    }

    public boolean isRead() {
        return status == MessageStatus.READ;
    }
}

