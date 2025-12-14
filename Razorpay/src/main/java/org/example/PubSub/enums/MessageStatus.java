package org.example.PubSub.enums;

/**
 * Enum representing the status of a message in the pub-sub system.
 * Messages can be in different states during their lifecycle.
 */
public enum MessageStatus {
    /**
     * Message is pending and waiting to be processed by consumers
     */
    PENDING,
    
    /**
     * Message has been successfully processed by a consumer
     */
    PROCESSED,
    
    /**
     * Message processing failed or encountered an error
     */
    FAILED
}
