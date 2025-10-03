package org.example.enums;

/**
 * Transaction status tracking
 */
public enum TransactionStatus {
    PENDING("Pending"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled"),
    FAILED("Failed");

    private final String displayName;

    TransactionStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

