package org.example.enums;

/**
 * Represents different types of products available in vending machine
 */
public enum ProductType {
    SNACK("Snack"),
    BEVERAGE("Beverage"),
    CANDY("Candy");

    private final String displayName;

    ProductType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
