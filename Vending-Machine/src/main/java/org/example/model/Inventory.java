package org.example.model;

import org.example.enums.ProductType;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages the inventory of products in the vending machine.
 * Handles product storage, retrieval, and stock management.
 * Follows Single Responsibility Principle.
 */
public class Inventory {
    private final Map<String, Product> products;
    private static final int LOW_STOCK_THRESHOLD = 3;

    /**
     * Creates a new Inventory instance with an empty product map.
     */
    public Inventory() {
        this.products = new HashMap<>();
    }

    /**
     * Adds a product to the inventory.
     * 
     * @param slotId the slot ID (e.g., "A1")
     * @param type the product type
     * @param quantity the initial quantity
     */
    public void addProduct(String slotId, ProductType type, int quantity) {
        products.put(slotId, new Product(slotId, type, quantity));
    }

    /**
     * Gets a product by its slot ID.
     * 
     * @param slotId the slot ID
     * @return the product, or null if not found
     */
    public Product getProduct(String slotId) {
        return products.get(slotId);
    }

    /**
     * Gets all products in the inventory.
     * 
     * @return map of all products (key: slot ID, value: Product)
     */
    public Map<String, Product> getAllProducts() {
        return new HashMap<>(products);
    }

    /**
     * Checks if a product is available at the given slot.
     * 
     * @param slotId the slot ID
     * @return true if available, false otherwise
     */
    public boolean isProductAvailable(String slotId) {
        Product product = products.get(slotId);
        return product != null && product.isAvailable();
    }

    /**
     * Dispenses a product (decrements quantity).
     * 
     * @param slotId the slot ID
     * @return the dispensed product, or null if not available
     */
    public Product dispenseProduct(String slotId) {
        Product product = products.get(slotId);
        if (product != null && product.decrementQuantity()) {
            return product;
        }
        return null;
    }

    /**
     * Restocks a product by adding to its quantity.
     * 
     * @param slotId the slot ID
     * @param quantity the quantity to add
     * @return true if successful, false if product doesn't exist
     */
    public boolean restockProduct(String slotId, int quantity) {
        Product product = products.get(slotId);
        if (product != null) {
            product.setQuantity(product.getQuantity() + quantity);
            return true;
        }
        return false;
    }

    /**
     * Gets the total value of all products in inventory.
     * 
     * @return total value in cents
     */
    public int getTotalValue() {
        return products.values().stream()
            .mapToInt(p -> p.getPrice() * p.getQuantity())
            .sum();
    }

    /**
     * Gets the total number of items in inventory.
     * 
     * @return total item count
     */
    public int getTotalItems() {
        return products.values().stream()
            .mapToInt(Product::getQuantity)
            .sum();
    }

    /**
     * Checks if a product has low stock.
     * 
     * @param slotId the slot ID
     * @return true if stock is low, false otherwise
     */
    public boolean isLowStock(String slotId) {
        Product product = products.get(slotId);
        return product != null && product.getQuantity() <= LOW_STOCK_THRESHOLD;
    }

    /**
     * Gets all products with low stock.
     * 
     * @return map of low stock products
     */
    public Map<String, Product> getLowStockProducts() {
        Map<String, Product> lowStock = new HashMap<>();
        for (Map.Entry<String, Product> entry : products.entrySet()) {
            if (isLowStock(entry.getKey())) {
                lowStock.put(entry.getKey(), entry.getValue());
            }
        }
        return lowStock;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Inventory ===\n");
        products.values().forEach(p -> sb.append(p.toString()).append("\n"));
        sb.append("Total Items: ").append(getTotalItems()).append("\n");
        sb.append("Total Value: $").append(String.format("%.2f", getTotalValue() / 100.0));
        return sb.toString();
    }
}

