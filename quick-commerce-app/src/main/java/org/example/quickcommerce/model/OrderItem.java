package org.example.quickcommerce.model;

/**
 * Represents an item in an order.
 */
public class OrderItem {
    private final String productId;
    private final String productName;
    private final double pricePerUnit;
    private final int quantity;
    private final String unit;

    public OrderItem(Product product, int quantity) {
        this.productId = product.getProductId();
        this.productName = product.getName();
        this.pricePerUnit = product.getPrice();
        this.quantity = quantity;
        this.unit = product.getUnit();
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public double getTotalPrice() {
        return pricePerUnit * quantity;
    }

    @Override
    public String toString() {
        return "OrderItem{productName='" + productName + "', quantity=" + quantity + ", totalPrice=" + getTotalPrice() + "}";
    }
}

