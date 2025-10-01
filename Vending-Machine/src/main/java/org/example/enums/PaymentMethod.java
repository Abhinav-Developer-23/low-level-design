package org.example.enums;

/**
 * Represents different payment methods supported by the vending machine
 */
public enum PaymentMethod {
    COINS("Coins"),
    CARD("Card"),
    DIGITAL_WALLET("Digital Wallet");

    private final String displayName;

    PaymentMethod(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
