package org.example.enums;

/**
 * Represents different product categories with predefined prices.
 * All prices are stored in cents to avoid floating-point precision issues.
 */
public enum ProductType {
    CHIPS("Chips", 125),           // $1.25
    CHOCOLATE("Chocolate", 150),    // $1.50
    SODA("Soda", 175),             // $1.75
    CANDY("Candy", 100),           // $1.00
    GUM("Gum", 50),                // $0.50
    WATER("Water", 125);           // $1.25

    private final String name;
    private final int priceInCents;

    ProductType(String name, int priceInCents) {
        this.name = name;
        this.priceInCents = priceInCents;
    }

    /**
     * Gets the product name.
     * 
     * @return the product name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the price in cents.
     * 
     * @return the price in cents
     */
    public int getPriceInCents() {
        return priceInCents;
    }

    /**
     * Gets the price formatted as dollars.
     * 
     * @return formatted price string (e.g., "$1.25")
     */
    public String getFormattedPrice() {
        return String.format("$%.2f", priceInCents / 100.0);
    }

    @Override
    public String toString() {
        return name + " (" + getFormattedPrice() + ")";
    }
}

