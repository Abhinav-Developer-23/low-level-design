package org.example.enums;

/**
 * Types of notifications in the system
 */
public enum NotificationType {
    CONNECTION_REQUEST,     // New connection request received
    CONNECTION_ACCEPTED,    // Connection request was accepted
    MESSAGE,                // New message received
    JOB_POSTING,           // New job posted that matches user criteria
    JOB_APPLICATION        // New application for posted job
}

