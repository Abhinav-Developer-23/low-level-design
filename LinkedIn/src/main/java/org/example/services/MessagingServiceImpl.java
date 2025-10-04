package org.example.services;

import org.example.interfaces.AuthenticationService;
import org.example.interfaces.ConnectionService;
import org.example.interfaces.MessagingService;
import org.example.model.Message;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Implementation of MessagingService
 * Handles messaging between connected users
 * Thread-safe using ConcurrentHashMap
 */
public class MessagingServiceImpl implements MessagingService {
    private final Map<String, Message> messages;
    private final AuthenticationService authService;
    private final ConnectionService connectionService;

    public MessagingServiceImpl(AuthenticationService authService, ConnectionService connectionService) {
        this.messages = new ConcurrentHashMap<>();
        this.authService = authService;
        this.connectionService = connectionService;
    }

    @Override
    public Message sendMessage(String senderId, String receiverId, String content) {
        if (senderId == null || receiverId == null) {
            throw new IllegalArgumentException("Sender and receiver IDs cannot be null");
        }
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Message content cannot be empty");
        }

        // Verify users exist
        if (authService.getUserById(senderId) == null || authService.getUserById(receiverId) == null) {
            throw new IllegalArgumentException("User not found");
        }

        // Check if users are connected
        if (!connectionService.areConnected(senderId, receiverId)) {
            throw new IllegalArgumentException("Can only send messages to connections");
        }

        Message message = new Message(senderId, receiverId, content);
        message.markAsDelivered();
        messages.put(message.getMessageId(), message);

        return message;
    }

    @Override
    public List<Message> getConversation(String userId1, String userId2) {
        if (userId1 == null || userId2 == null) {
            throw new IllegalArgumentException("User IDs cannot be null");
        }

        return messages.values().stream()
                .filter(m -> (m.getSenderId().equals(userId1) && m.getReceiverId().equals(userId2)) ||
                             (m.getSenderId().equals(userId2) && m.getReceiverId().equals(userId1)))
                .sorted(Comparator.comparing(Message::getSentAt))
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> getInbox(String userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        return messages.values().stream()
                .filter(m -> m.getReceiverId().equals(userId))
                .sorted(Comparator.comparing(Message::getSentAt).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> getSentMessages(String userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        return messages.values().stream()
                .filter(m -> m.getSenderId().equals(userId))
                .sorted(Comparator.comparing(Message::getSentAt).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public boolean markAsRead(String messageId, String userId) {
        Message message = messages.get(messageId);
        if (message == null) {
            throw new IllegalArgumentException("Message not found");
        }

        // Only the receiver can mark as read
        if (!message.getReceiverId().equals(userId)) {
            throw new IllegalArgumentException("Only the receiver can mark message as read");
        }

        message.markAsRead();
        return true;
    }

    @Override
    public int getUnreadMessageCount(String userId) {
        if (userId == null) {
            return 0;
        }

        return (int) messages.values().stream()
                .filter(m -> m.getReceiverId().equals(userId) && !m.isRead())
                .count();
    }

    @Override
    public Message getMessageById(String messageId) {
        return messages.get(messageId);
    }
}

