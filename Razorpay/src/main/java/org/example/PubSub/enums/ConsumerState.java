package org.example.PubSub.enums;

/**
 * Enum representing the state of a consumer
 * Used for lifecycle management of consumers
 */
public enum ConsumerState {
    ACTIVE,         // Consumer is actively consuming messages
    PAUSED,         // Consumer is paused, not consuming new messages
    STOPPED,        // Consumer has been stopped
    ERROR           // Consumer encountered an error
}

