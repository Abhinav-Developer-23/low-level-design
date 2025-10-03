package org.example.services;

import org.example.interfaces.MessagingService;
import org.example.model.Message;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of messaging service.
 * Manages messages between connected users.
 */
public class MessagingServiceImpl implements MessagingService {
    private final Map<String, Message> messages;
    private final ConnectionServiceImpl connectionService;

    public MessagingServiceImpl(ConnectionServiceImpl connectionService) {
        this.messages = new HashMap<>();
        this.connectionService = connectionService;
    }

    @Override
    public Message sendMessage(String senderId, String receiverId, String content) {
        // Validate that users are connected
        if (!connectionService.areConnected(senderId, receiverId)) {
            throw new IllegalArgumentException("Can only send messages to connections");
        }

        // Validate content
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Message content cannot be empty");
        }

        // Create and store message
        Message message = new Message(senderId, receiverId, content);
        messages.put(message.getMessageId(), message);

        // Mark as delivered
        message.markAsDelivered();

        return message;
    }

    @Override
    public List<Message> getConversation(String userId1, String userId2) {
        return messages.values().stream()
                .filter(m -> (m.getSenderId().equals(userId1) && m.getReceiverId().equals(userId2)) ||
                           (m.getSenderId().equals(userId2) && m.getReceiverId().equals(userId1)))
                .sorted(Comparator.comparing(Message::getSentAt))
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> getInbox(String userId) {
        return messages.values().stream()
                .filter(m -> m.getReceiverId().equals(userId))
                .sorted(Comparator.comparing(Message::getSentAt).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> getSentMessages(String userId) {
        return messages.values().stream()
                .filter(m -> m.getSenderId().equals(userId))
                .sorted(Comparator.comparing(Message::getSentAt).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public boolean markAsRead(String messageId) {
        Message message = messages.get(messageId);

        if (message == null) {
            return false;
        }

        message.markAsRead();
        return true;
    }

    @Override
    public int getUnreadCount(String userId) {
        return (int) messages.values().stream()
                .filter(m -> m.getReceiverId().equals(userId) && !m.isRead())
                .count();
    }

    /**
     * Gets a message by its ID.
     */
    public Message getMessage(String messageId) {
        return messages.get(messageId);
    }

    /**
     * Gets all conversations for a user (grouped by conversation partner).
     */
    public Map<String, List<Message>> getConversationsByUser(String userId) {
        return messages.values().stream()
                .filter(m -> m.getSenderId().equals(userId) || m.getReceiverId().equals(userId))
                .collect(Collectors.groupingBy(Message::getConversationId));
    }
}

