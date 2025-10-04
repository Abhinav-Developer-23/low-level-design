package org.example.interfaces;

import org.example.model.Connection;
import org.example.model.User;

import java.util.List;

/**
 * Interface for managing connections between users
 */
public interface ConnectionService {
    /**
     * Send a connection request
     */
    Connection sendConnectionRequest(String senderId, String receiverId, String message);

    /**
     * Accept a connection request
     */
    boolean acceptConnectionRequest(String connectionId);

    /**
     * Decline a connection request
     */
    boolean declineConnectionRequest(String connectionId);

    /**
     * Remove an existing connection
     */
    boolean removeConnection(String userId1, String userId2);

    /**
     * Get all connections for a user
     */
    List<User> getConnections(String userId);

    /**
     * Get pending connection requests for a user
     */
    List<Connection> getPendingRequests(String userId);

    /**
     * Check if two users are connected
     */
    boolean areConnected(String userId1, String userId2);

    /**
     * Get connection by ID
     */
    Connection getConnectionById(String connectionId);

    /**
     * Get connection count for a user
     */
    int getConnectionCount(String userId);
}

