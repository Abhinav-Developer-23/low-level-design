package org.example.interfaces;

import org.example.model.Message;

import java.util.List;

/**
 * Interface for messaging operations between users.
 */
public interface MessagingService {
    /**
     * Sends a message from one user to another.
     *
     * @param senderId ID of the sender
     * @param receiverId ID of the receiver
     * @param content Message content
     * @return The sent message
     * @throws IllegalArgumentException if users are not connected
     */
    Message sendMessage(String senderId, String receiverId, String content);

    /**
     * Gets all messages in a conversation between two users.
     *
     * @param userId1 First user ID
     * @param userId2 Second user ID
     * @return List of messages in chronological order
     */
    List<Message> getConversation(String userId1, String userId2);

    /**
     * Gets all messages received by a user.
     *
     * @param userId User ID
     * @return List of received messages
     */
    List<Message> getInbox(String userId);

    /**
     * Gets all messages sent by a user.
     *
     * @param userId User ID
     * @return List of sent messages
     */
    List<Message> getSentMessages(String userId);

    /**
     * Marks a message as read.
     *
     * @param messageId Message ID
     * @return true if marked successfully
     */
    boolean markAsRead(String messageId);

    /**
     * Gets count of unread messages for a user.
     *
     * @param userId User ID
     * @return Number of unread messages
     */
    int getUnreadCount(String userId);
}

