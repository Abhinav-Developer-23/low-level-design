package org.example.interfaces;

import org.example.model.Connection;
import org.example.model.User;

import java.util.List;

/**
 * Interface for managing user connections.
 */
public interface ConnectionService {
    /**
     * Sends a connection request from one user to another.
     *
     * @param senderId ID of the user sending the request
     * @param receiverId ID of the user receiving the request
     * @param message Optional message with the request
     * @return The created connection request
     */
    Connection sendConnectionRequest(String senderId, String receiverId, String message);

    /**
     * Accepts a connection request.
     *
     * @param connectionId ID of the connection to accept
     * @return true if accepted successfully
     */
    boolean acceptConnectionRequest(String connectionId);

    /**
     * Declines a connection request.
     *
     * @param connectionId ID of the connection to decline
     * @return true if declined successfully
     */
    boolean declineConnectionRequest(String connectionId);

    /**
     * Removes an existing connection between two users.
     *
     * @param userId1 First user ID
     * @param userId2 Second user ID
     * @return true if removed successfully
     */
    boolean removeConnection(String userId1, String userId2);

    /**
     * Gets all connections for a user.
     *
     * @param userId User ID
     * @return List of connected users
     */
    List<User> getConnections(String userId);

    /**
     * Gets all pending connection requests for a user.
     *
     * @param userId User ID
     * @return List of pending connection requests
     */
    List<Connection> getPendingRequests(String userId);

    /**
     * Checks if two users are connected.
     *
     * @param userId1 First user ID
     * @param userId2 Second user ID
     * @return true if users are connected
     */
    boolean areConnected(String userId1, String userId2);
}

