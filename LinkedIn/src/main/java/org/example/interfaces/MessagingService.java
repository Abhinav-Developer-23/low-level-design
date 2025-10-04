package org.example.interfaces;

import org.example.model.Message;

import java.util.List;

/**
 * Interface for messaging functionality
 */
public interface MessagingService {
    /**
     * Send a message to another user
     */
    Message sendMessage(String senderId, String receiverId, String content);

    /**
     * Get conversation between two users
     */
    List<Message> getConversation(String userId1, String userId2);

    /**
     * Get all messages received by a user (inbox)
     */
    List<Message> getInbox(String userId);

    /**
     * Get all messages sent by a user
     */
    List<Message> getSentMessages(String userId);

    /**
     * Mark a message as read
     */
    boolean markAsRead(String messageId, String userId);

    /**
     * Get unread message count for a user
     */
    int getUnreadMessageCount(String userId);

    /**
     * Get message by ID
     */
    Message getMessageById(String messageId);
}

