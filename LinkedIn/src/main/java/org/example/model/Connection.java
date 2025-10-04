package org.example.model;

import org.example.enums.ConnectionStatus;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a connection request between two users
 */
public class Connection {
    private final String connectionId;
    private final String senderId;
    private final String receiverId;
    private ConnectionStatus status;
    private String message;
    private final LocalDateTime requestedAt;
    private LocalDateTime respondedAt;

    public Connection(String senderId, String receiverId) {
        this(senderId, receiverId, null);
    }

    public Connection(String senderId, String receiverId, String message) {
        this.connectionId = UUID.randomUUID().toString();
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.status = ConnectionStatus.PENDING;
        this.requestedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getConnectionId() {
        return connectionId;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public ConnectionStatus getStatus() {
        return status;
    }

    public void setStatus(ConnectionStatus status) {
        this.status = status;
        if (status != ConnectionStatus.PENDING) {
            this.respondedAt = LocalDateTime.now();
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    public LocalDateTime getRespondedAt() {
        return respondedAt;
    }

    public boolean isPending() {
        return status == ConnectionStatus.PENDING;
    }

    public boolean isAccepted() {
        return status == ConnectionStatus.ACCEPTED;
    }

    public String getOtherUserId(String userId) {
        return userId.equals(senderId) ? receiverId : senderId;
    }
}

