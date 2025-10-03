package org.example.enums;

/**
 * Enum representing different payment methods supported by the vending machine.
 */
public enum PaymentMethod {
    CASH("Cash payment"),
    CARD("Card payment"),
    MOBILE("Mobile payment");

    private final String description;

    PaymentMethod(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
