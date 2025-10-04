package org.example.services;

import org.example.enums.ConnectionStatus;
import org.example.interfaces.AuthenticationService;
import org.example.interfaces.ConnectionService;
import org.example.model.Connection;
import org.example.model.User;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Implementation of ConnectionService
 * Manages connections between users
 * Thread-safe using ConcurrentHashMap
 */
public class ConnectionServiceImpl implements ConnectionService {
    private final Map<String, Connection> connections;
    private final AuthenticationService authService;

    public ConnectionServiceImpl(AuthenticationService authService) {
        this.connections = new ConcurrentHashMap<>();
        this.authService = authService;
    }

    @Override
    public Connection sendConnectionRequest(String senderId, String receiverId, String message) {
        if (senderId == null || receiverId == null) {
            throw new IllegalArgumentException("Sender and receiver IDs cannot be null");
        }
        if (senderId.equals(receiverId)) {
            throw new IllegalArgumentException("Cannot send connection request to yourself");
        }

        User sender = authService.getUserById(senderId);
        User receiver = authService.getUserById(receiverId);

        if (sender == null || receiver == null) {
            throw new IllegalArgumentException("User not found");
        }

        if (areConnected(senderId, receiverId)) {
            throw new IllegalArgumentException("Users are already connected");
        }

        // Check if there's already a pending request
        boolean hasPendingRequest = connections.values().stream()
                .anyMatch(c -> c.isPending() && 
                    ((c.getSenderId().equals(senderId) && c.getReceiverId().equals(receiverId)) ||
                     (c.getSenderId().equals(receiverId) && c.getReceiverId().equals(senderId))));

        if (hasPendingRequest) {
            throw new IllegalArgumentException("Connection request already pending");
        }

        Connection connection = new Connection(senderId, receiverId, message);
        connections.put(connection.getConnectionId(), connection);
        receiver.addPendingRequest(connection.getConnectionId());

        return connection;
    }

    @Override
    public boolean acceptConnectionRequest(String connectionId) {
        Connection connection = connections.get(connectionId);
        if (connection == null) {
            throw new IllegalArgumentException("Connection not found");
        }

        if (!connection.isPending()) {
            throw new IllegalArgumentException("Connection request is not pending");
        }

        connection.setStatus(ConnectionStatus.ACCEPTED);

        // Update both users' connection lists
        User sender = authService.getUserById(connection.getSenderId());
        User receiver = authService.getUserById(connection.getReceiverId());

        sender.addConnection(receiver.getUserId());
        receiver.addConnection(sender.getUserId());
        receiver.removePendingRequest(connectionId);

        return true;
    }

    @Override
    public boolean declineConnectionRequest(String connectionId) {
        Connection connection = connections.get(connectionId);
        if (connection == null) {
            throw new IllegalArgumentException("Connection not found");
        }

        if (!connection.isPending()) {
            throw new IllegalArgumentException("Connection request is not pending");
        }

        connection.setStatus(ConnectionStatus.DECLINED);

        User receiver = authService.getUserById(connection.getReceiverId());
        receiver.removePendingRequest(connectionId);

        return true;
    }

    @Override
    public boolean removeConnection(String userId1, String userId2) {
        if (!areConnected(userId1, userId2)) {
            throw new IllegalArgumentException("Users are not connected");
        }

        User user1 = authService.getUserById(userId1);
        User user2 = authService.getUserById(userId2);

        user1.removeConnection(userId2);
        user2.removeConnection(userId1);

        return true;
    }

    @Override
    public List<User> getConnections(String userId) {
        User user = authService.getUserById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
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
            throw new IllegalArgumentException("User not found");
        }

        return user.getPendingRequestIds().stream()
                .map(connections::get)
                .filter(c -> c != null && c.isPending())
                .collect(Collectors.toList());
    }

    @Override
    public boolean areConnected(String userId1, String userId2) {
        User user1 = authService.getUserById(userId1);
        if (user1 == null) {
            return false;
        }
        return user1.isConnectedTo(userId2);
    }

    @Override
    public Connection getConnectionById(String connectionId) {
        return connections.get(connectionId);
    }

    @Override
    public int getConnectionCount(String userId) {
        User user = authService.getUserById(userId);
        if (user == null) {
            return 0;
        }
        return user.getConnectionIds().size();
    }
}

