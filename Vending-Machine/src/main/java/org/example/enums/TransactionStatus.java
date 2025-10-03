package org.example.enums;

/**
 * Represents the different statuses of a vending machine transaction.
 */
public enum TransactionStatus {
    PENDING("Awaiting payment"),
    PROCESSING("Processing payment"),
    COMPLETED("Transaction completed successfully"),
    FAILED("Transaction failed"),
    CANCELLED("Transaction cancelled by user"),
    REFUNDING("Refunding payment");

    private final String description;

    TransactionStatus(String description) {
        this.description = description;
    }

    /**
     * Gets a human-readable description of the transaction status.
     * 
     * @return the status description
     */
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return name() + ": " + description;
    }
}

