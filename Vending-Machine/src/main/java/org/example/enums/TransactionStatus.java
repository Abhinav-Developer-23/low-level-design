package org.example.enums;

/**
 * Enum representing the status of a vending machine transaction.
 */
public enum TransactionStatus {
    PENDING("Transaction initiated"),
    PROCESSING("Transaction being processed"),
    COMPLETED("Transaction completed successfully"),
    FAILED("Transaction failed"),
    CANCELLED("Transaction cancelled"),
    REFUNDING("Transaction being refunded");

    private final String description;

    TransactionStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
