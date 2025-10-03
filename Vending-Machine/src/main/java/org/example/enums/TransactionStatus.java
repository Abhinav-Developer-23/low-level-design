package org.example.enums;

/**
 * Enumeration representing the status of a vending machine transaction.
 * Used for tracking transaction lifecycle and error handling.
 */
public enum TransactionStatus {
    INITIATED,      // Transaction started
    PAYMENT_PENDING,// Waiting for payment completion
    PAYMENT_SUCCESS,// Payment processed successfully
    PAYMENT_FAILED, // Payment processing failed
    DISPENSING,     // Product being dispensed
    COMPLETED,      // Transaction completed successfully
    CANCELLED,      // Transaction cancelled by user
    REFUNDED,       // Payment refunded
    ERROR           // Transaction error occurred
}
