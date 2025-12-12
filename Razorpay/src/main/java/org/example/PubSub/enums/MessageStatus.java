package org.example.PubSub.enums;

/**
 * Enum representing the status of a message in the pub-sub system
 * Used to track message lifecycle and processing state
 */
public enum MessageStatus {
    PUBLISHED,      // Message has been published to topic
    PROCESSING,     // Message is being processed by consumer
    ACKNOWLEDGED,   // Consumer has acknowledged successful processing
    FAILED,         // Message processing failed
    DEAD_LETTER     // Message moved to dead letter queue after max retries
}

