package org.example.services;

import org.example.enums.ConnectionStatus;
import org.example.interfaces.ConnectionService;
import org.example.model.Connection;
import org.example.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of connection service.
 * Manages connections between users.
 */
public class ConnectionServiceImpl implements ConnectionService {
    private final Map<String, Connection> connections;
    private final AuthenticationServiceImpl authService;

    public ConnectionServiceImpl(AuthenticationServiceImpl authService) {
        this.connections = new HashMap<>();
        this.authService = authService;
    }

    @Override
    public Connection sendConnectionRequest(String senderId, String receiverId, String message) {
        // Validate users exist
        User sender = authService.getUserById(senderId);
        User receiver = authService.getUserById(receiverId);

        if (sender == null || receiver == null) {
            throw new IllegalArgumentException("Invalid sender or receiver ID");
        }

        if (senderId.equals(receiverId)) {
            throw new IllegalArgumentException("Cannot send connection request to yourself");
        }

        // Check if already connected
        if (areConnected(senderId, receiverId)) {
            throw new IllegalArgumentException("Users are already connected");
        }

        // Check if request already exists
        if (hasPendingRequest(senderId, receiverId)) {
            throw new IllegalArgumentException("Connection request already exists");
        }

        // Create connection request
        Connection connection = new Connection(senderId, receiverId, message);
        connections.put(connection.getConnectionId(), connection);

        // Update user's pending requests
        receiver.addPendingConnectionRequest(connection.getConnectionId());

        return connection;
    }

    @Override
    public boolean acceptConnectionRequest(String connectionId) {
        Connection connection = connections.get(connectionId);

        if (connection == null || !connection.isPending()) {
            return false;
        }

        // Update connection status
        connection.setStatus(ConnectionStatus.ACCEPTED);

        // Update both users' connection lists
        User sender = authService.getUserById(connection.getSenderId());
        User receiver = authService.getUserById(connection.getReceiverId());

        if (sender != null && receiver != null) {
            sender.addConnection(receiver.getUserId());
            receiver.addConnection(sender.getUserId());
            receiver.removePendingConnectionRequest(connectionId);
            return true;
        }

        return false;
    }

    @Override
    public boolean declineConnectionRequest(String connectionId) {
        Connection connection = connections.get(connectionId);

        if (connection == null || !connection.isPending()) {
            return false;
        }

        // Update connection status
        connection.setStatus(ConnectionStatus.DECLINED);

        // Remove from pending requests
        User receiver = authService.getUserById(connection.getReceiverId());
        if (receiver != null) {
            receiver.removePendingConnectionRequest(connectionId);
            return true;
        }

        return false;
    }

    @Override
    public boolean removeConnection(String userId1, String userId2) {
        if (!areConnected(userId1, userId2)) {
            return false;
        }

        User user1 = authService.getUserById(userId1);
        User user2 = authService.getUserById(userId2);

        if (user1 != null && user2 != null) {
            user1.removeConnection(userId2);
            user2.removeConnection(userId1);
            return true;
        }

        return false;
    }

    @Override
    public List<User> getConnections(String userId) {
        User user = authService.getUserById(userId);

        if (user == null) {
            return new ArrayList<>();
        }

        return user.getConnectionIds().stream()
                .map(authService::getUserById)
                .filter(u -> u != null)
                .collect(Collectors.toList());
    }

    @Override
    public List<Connection> getPendingRequests(String userId) {
        User user = authService.getUserById(userId);

        if (user == null) {
            return new ArrayList<>();
        }

        return user.getPendingConnectionRequestIds().stream()
                .map(connections::get)
                .filter(c -> c != null && c.isPending())
                .collect(Collectors.toList());
    }

    @Override
    public boolean areConnected(String userId1, String userId2) {
        User user1 = authService.getUserById(userId1);
        return user1 != null && user1.isConnectedWith(userId2);
    }

    // Helper methods

    /**
     * Checks if there's a pending connection request between two users.
     */
    private boolean hasPendingRequest(String userId1, String userId2) {
        return connections.values().stream()
                .anyMatch(c -> c.isPending() &&
                        ((c.getSenderId().equals(userId1) && c.getReceiverId().equals(userId2)) ||
                         (c.getSenderId().equals(userId2) && c.getReceiverId().equals(userId1))));
    }

    /**
     * Gets a connection by ID.
     */
    public Connection getConnection(String connectionId) {
        return connections.get(connectionId);
    }
}

