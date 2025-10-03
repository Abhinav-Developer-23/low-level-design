package org.example.model;

import org.example.enums.ProductType;
import java.util.Objects;

/**
 * Represents a product in the vending machine.
 * Encapsulates product information and inventory count.
 */
public class Product {
    private final String id;              // Slot ID (e.g., "A1", "B2")
    private final ProductType type;
    private final String name;
    private final int price;              // Price in cents
    private int quantity;

    /**
     * Creates a new Product instance.
     * 
     * @param id the slot ID
     * @param type the product type
     * @param quantity the initial quantity
     */
    public Product(String id, ProductType type, int quantity) {
        this.id = id;
        this.type = type;
        this.name = type.getName();
        this.price = type.getPriceInCents();
        this.quantity = quantity;
    }

    /**
     * Gets the product ID (slot ID).
     * 
     * @return the product ID
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the product type.
     * 
     * @return the product type
     */
    public ProductType getType() {
        return type;
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
     * Gets the product price in cents.
     * 
     * @return the price in cents
     */
    public int getPrice() {
        return price;
    }

    /**
     * Gets the current quantity.
     * 
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the product quantity.
     * 
     * @param quantity the new quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = Math.max(0, quantity);
    }

    /**
     * Checks if the product is available (quantity > 0).
     * 
     * @return true if available, false otherwise
     */
    public boolean isAvailable() {
        return quantity > 0;
    }

    /**
     * Decrements the product quantity by 1.
     * 
     * @return true if successful, false if out of stock
     */
    public boolean decrementQuantity() {
        if (quantity > 0) {
            quantity--;
            return true;
        }
        return false;
    }

    /**
     * Gets formatted price as a dollar string.
     * 
     * @return formatted price (e.g., "$1.25")
     */
    public String getFormattedPrice() {
        return String.format("$%.2f", price / 100.0);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s - %s (Stock: %d)", 
            id, name, getFormattedPrice(), quantity);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Product product = (Product) obj;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

