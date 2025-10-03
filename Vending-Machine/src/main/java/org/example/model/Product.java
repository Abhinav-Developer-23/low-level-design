package org.example.model;

import org.example.enums.ProductType;

/**
 * Represents a product available in the vending machine.
 * Contains product details and pricing information.
 */
public class Product {
    private final String slotId;
    private final String name;
    private final int price; // Price in cents
    private final ProductType productType;

    public Product(String slotId, String name, int price, ProductType productType) {
        this.slotId = slotId;
        this.name = name;
        this.price = price;
        this.productType = productType;
    }

    public String getSlotId() {
        return slotId;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public ProductType getProductType() {
        return productType;
    }

    /**
     * Returns the price in dollar format for display.
     * @return Formatted price string (e.g., "$1.25")
     */
    public String getFormattedPrice() {
        return String.format("$%.2f", price / 100.0);
    }

    @Override
    public String toString() {
        return String.format("%s (%s) - %s", name, slotId, getFormattedPrice());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Product product = (Product) obj;
        return slotId.equals(product.slotId);
    }

    @Override
    public int hashCode() {
        return slotId.hashCode();
    }
}
