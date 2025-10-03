package org.example.model;

import org.example.enums.ProductType;

/**
 * Model class representing a product in the vending machine.
 */
public class Product {
    private final String id;
    private final ProductType type;
    private final String name;
    private final int price;
    private int quantity;

    public Product(String id, ProductType type, int quantity) {
        this.id = id;
        this.type = type;
        this.name = type.getName();
        this.price = type.getPriceInCents();
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public ProductType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isAvailable() {
        return quantity > 0;
    }

    public void decrementQuantity() {
        if (quantity > 0) {
            quantity--;
        }
    }

    @Override
    public String toString() {
        return String.format("%s - %s (%d cents) [%d available]",
                           id, name, price, quantity);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Product product = (Product) obj;
        return id.equals(product.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
