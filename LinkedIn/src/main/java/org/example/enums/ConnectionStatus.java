package org.example.enums;

/**
 * Represents the status of a connection request between users.
 */
public enum ConnectionStatus {
    PENDING,    // Connection request sent but not yet accepted
    ACCEPTED,   // Connection request accepted, users are connected
    DECLINED,   // Connection request declined
    BLOCKED     // User has been blocked
}

