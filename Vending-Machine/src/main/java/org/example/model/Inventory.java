package org.example.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Manages the inventory of products in the vending machine.
 * Tracks stock levels and provides inventory operations.
 */
public class Inventory {
    private final Map<String, Product> products; // slotId -> Product
    private final Map<String, Integer> stockLevels; // slotId -> quantity

    public Inventory() {
        this.products = new HashMap<>();
        this.stockLevels = new HashMap<>();
    }

    /**
     * Adds a product to the inventory with initial stock.
     * @param product The product to add
     * @param initialStock The initial stock quantity
     */
    public void addProduct(Product product, int initialStock) {
        products.put(product.getSlotId(), product);
        stockLevels.put(product.getSlotId(), initialStock);
    }

    /**
     * Gets a product by its slot ID.
     * @param slotId The slot identifier
     * @return Optional containing the product if found
     */
    public Optional<Product> getProduct(String slotId) {
        return Optional.ofNullable(products.get(slotId));
    }

    /**
     * Gets the current stock level for a product.
     * @param slotId The slot identifier
     * @return The current stock quantity, or 0 if product not found
     */
    public int getStockLevel(String slotId) {
        return stockLevels.getOrDefault(slotId, 0);
    }

    /**
     * Checks if a product is available (in stock).
     * @param slotId The slot identifier
     * @return true if product exists and has stock > 0
     */
    public boolean isProductAvailable(String slotId) {
        return getStockLevel(slotId) > 0;
    }

    /**
     * Reduces the stock level for a product by 1.
     * @param slotId The slot identifier
     * @return true if stock was reduced successfully, false if out of stock
     */
    public boolean dispenseProduct(String slotId) {
        int currentStock = getStockLevel(slotId);
        if (currentStock > 0) {
            stockLevels.put(slotId, currentStock - 1);
            return true;
        }
        return false;
    }

    /**
     * Restocks a product to the specified quantity.
     * @param slotId The slot identifier
     * @param quantity The new stock quantity
     */
    public void restockProduct(String slotId, int quantity) {
        if (products.containsKey(slotId)) {
            stockLevels.put(slotId, quantity);
        }
    }

    /**
     * Gets all available products (those with stock > 0).
     * @return Map of slotId to Product for available items
     */
    public Map<String, Product> getAvailableProducts() {
        Map<String, Product> available = new HashMap<>();
        for (Map.Entry<String, Product> entry : products.entrySet()) {
            if (isProductAvailable(entry.getKey())) {
                available.put(entry.getKey(), entry.getValue());
            }
        }
        return available;
    }

    /**
     * Gets the total number of products in inventory.
     * @return Total count of all products
     */
    public int getTotalProductCount() {
        return stockLevels.values().stream().mapToInt(Integer::intValue).sum();
    }

    /**
     * Gets the total value of all products in inventory.
     * @return Total value in cents
     */
    public int getTotalInventoryValue() {
        int totalValue = 0;
        for (Map.Entry<String, Product> entry : products.entrySet()) {
            String slotId = entry.getKey();
            Product product = entry.getValue();
            int stock = getStockLevel(slotId);
            totalValue += product.getPrice() * stock;
        }
        return totalValue;
    }

    /**
     * Checks if any products are low on stock (less than or equal to threshold).
     * @param threshold The stock threshold
     * @return true if any products are low on stock
     */
    public boolean hasLowStockItems(int threshold) {
        return stockLevels.values().stream().anyMatch(stock -> stock <= threshold);
    }

    /**
     * Gets all products (including out-of-stock ones).
     * @return Map of all products
     */
    public Map<String, Product> getAllProducts() {
        return new HashMap<>(products);
    }

    /**
     * Gets the current stock levels for all products.
     * @return Map of slotId to stock quantity
     */
    public Map<String, Integer> getAllStockLevels() {
        return new HashMap<>(stockLevels);
    }
}
