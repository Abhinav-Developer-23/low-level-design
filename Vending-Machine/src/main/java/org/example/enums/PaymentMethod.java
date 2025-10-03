package org.example.enums;

/**
 * Represents different payment methods supported by the vending machine.
 */
public enum PaymentMethod {
    CASH("Cash/Coins"),
    CARD("Credit/Debit Card"),
    MOBILE("Mobile Payment");

    private final String description;

    PaymentMethod(String description) {
        this.description = description;
    }

    /**
     * Gets a human-readable description of the payment method.
     * 
     * @return the payment method description
     */
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description;
    }
}

