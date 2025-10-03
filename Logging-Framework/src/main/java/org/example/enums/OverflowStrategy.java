package org.example.enums;

/**
 * Enumeration for handling queue overflow in async logging.
 */
public enum OverflowStrategy {
    /**
     * Block the calling thread until space is available in the queue.
     * Guarantees no message loss but may impact performance.
     */
    BLOCK,

    /**
     * Drop the oldest message in the queue to make room for the new one.
     * Maintains throughput but may lose old messages.
     */
    DROP_OLDEST,

    /**
     * Drop the new message if the queue is full.
     * Maintains throughput but may lose new messages.
     */
    DROP_NEWEST,

    /**
     * Log an error and drop the message.
     * Good for monitoring queue overflow issues.
     */
    LOG_AND_DROP
}

