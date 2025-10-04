package org.example.model;

import org.example.enums.ConnectionStatus;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a connection between two users.
 */
public class Connection {
    private final String connectionId;
    private final String senderId;
    private final String receiverId;
    private ConnectionStatus status;
    private final LocalDateTime requestedAt;
    private LocalDateTime respondedAt;
    private String message; // Optional message with connection request

    public Connection(String senderId, String receiverId) {
        this.connectionId = UUID.randomUUID().toString();
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.status = ConnectionStatus.PENDING;
        this.requestedAt = LocalDateTime.now();
    }

    public Connection(String senderId, String receiverId, String message) {
        this(senderId, receiverId);
        this.message = message;
    }

    // Getters
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

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    public LocalDateTime getRespondedAt() {
        return respondedAt;
    }

    public String getMessage() {
        return message;
    }

    // Setters
    public void setStatus(ConnectionStatus status) {
        this.status = status;
        if (status == ConnectionStatus.ACCEPTED || status == ConnectionStatus.DECLINED) {
            this.respondedAt = LocalDateTime.now();
        }
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isPending() {
        return status == ConnectionStatus.PENDING;
    }

    public boolean isAccepted() {
        return status == ConnectionStatus.ACCEPTED;
    }

    @Override
    public String toString() {
        return "Connection{" +
                "connectionId='" + connectionId + '\'' +
                ", senderId='" + senderId + '\'' +
                ", receiverId='" + receiverId + '\'' +
                ", status=" + status +
                ", requestedAt=" + requestedAt +
                '}';
    }
}

