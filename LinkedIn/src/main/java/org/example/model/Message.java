package org.example.model;

import org.example.enums.MessageStatus;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a message between two users.
 */
public class Message {
    private final String messageId;
    private final String senderId;
    private final String receiverId;
    private final String content;
    private MessageStatus status;
    private final LocalDateTime sentAt;
    private LocalDateTime deliveredAt;
    private LocalDateTime readAt;
    private String conversationId; // To group messages in conversations

    public Message(String senderId, String receiverId, String content) {
        this.messageId = UUID.randomUUID().toString();
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.status = MessageStatus.SENT;
        this.sentAt = LocalDateTime.now();
        this.conversationId = generateConversationId(senderId, receiverId);
    }

    // Generate a consistent conversation ID for two users
    private String generateConversationId(String userId1, String userId2) {
        // Ensure consistent conversation ID regardless of who is sender/receiver
        if (userId1.compareTo(userId2) < 0) {
            return userId1 + "_" + userId2;
        } else {
            return userId2 + "_" + userId1;
        }
    }

    // Getters
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

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public LocalDateTime getDeliveredAt() {
        return deliveredAt;
    }

    public LocalDateTime getReadAt() {
        return readAt;
    }

    public String getConversationId() {
        return conversationId;
    }

    // Status update methods
    public void markAsDelivered() {
        this.status = MessageStatus.DELIVERED;
        this.deliveredAt = LocalDateTime.now();
    }

    public void markAsRead() {
        this.status = MessageStatus.READ;
        this.readAt = LocalDateTime.now();
    }

    public void markAsFailed() {
        this.status = MessageStatus.FAILED;
    }

    public boolean isRead() {
        return status == MessageStatus.READ;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageId='" + messageId + '\'' +
                ", senderId='" + senderId + '\'' +
                ", receiverId='" + receiverId + '\'' +
                ", content='" + content.substring(0, Math.min(content.length(), 50)) + "...'" +
                ", status=" + status +
                ", sentAt=" + sentAt +
                '}';
    }
}

