package org.example.enums;

/**
 * Represents the status of a vending machine transaction
 */
public enum TransactionStatus {
    PENDING,        // Transaction initiated
    PROCESSING,     // Payment being processed
    COMPLETED,      // Transaction successful
    FAILED,         // Transaction failed
    REFUNDED        // Payment refunded
}
