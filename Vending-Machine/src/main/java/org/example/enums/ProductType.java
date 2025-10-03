package org.example.enums;

/**
 * Enum representing different types of products available in the vending machine.
 */
public enum ProductType {
    CHIPS("Potato Chips", 150),
    CHOCOLATE("Chocolate Bar", 200),
    SODA("Soda Can", 175),
    CANDY("Candy", 75),
    GUM("Chewing Gum", 50),
    WATER("Bottled Water", 125);

    private final String name;
    private final int priceInCents;

    ProductType(String name, int priceInCents) {
        this.name = name;
        this.priceInCents = priceInCents;
    }

    public String getName() {
        return name;
    }

    public int getPriceInCents() {
        return priceInCents;
    }
}
