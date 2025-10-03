package org.example.enums;

/**
 * Categories of products available in vending machine
 */
public enum ProductType {
    BEVERAGE("Beverage"),
    SNACK("Snack"),
    CANDY("Candy"),
    FOOD("Food"),
    OTHER("Other");

    private final String displayName;

    ProductType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

