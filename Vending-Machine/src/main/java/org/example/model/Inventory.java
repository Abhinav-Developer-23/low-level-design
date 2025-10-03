package org.example.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Thread-safe inventory management
 */
public class Inventory {
    private final Map<String, Product> products;
    private final Map<String, AtomicInteger> stock;
    private final int lowStockThreshold;

    public Inventory() {
        this(5); // Default low stock threshold
    }

    public Inventory(int lowStockThreshold) {
        this.products = new ConcurrentHashMap<>();
        this.stock = new ConcurrentHashMap<>();
        this.lowStockThreshold = lowStockThreshold;
    }

    /**
     * Add a new product to inventory
     */
    public void addProduct(Product product, int initialStock) {
        products.put(product.getProductId(), product);
        stock.put(product.getProductId(), new AtomicInteger(initialStock));
    }

    /**
     * Restock a product
     */
    public void restockProduct(String productId, int quantity) {
        AtomicInteger stockCount = stock.get(productId);
        if (stockCount != null) {
            stockCount.addAndGet(quantity);
        }
    }

    /**
     * Check if product is available
     */
    public boolean isAvailable(String productId) {
        AtomicInteger stockCount = stock.get(productId);
        return stockCount != null && stockCount.get() > 0;
    }

    /**
     * Get stock count for a product
     */
    public int getStockCount(String productId) {
        AtomicInteger stockCount = stock.get(productId);
        return stockCount != null ? stockCount.get() : 0;
    }

    /**
     * Check if product is low in stock
     */
    public boolean isLowStock(String productId) {
        return getStockCount(productId) <= lowStockThreshold && getStockCount(productId) > 0;
    }

    /**
     * Decrement stock (thread-safe)
     */
    public synchronized boolean decrementStock(String productId) {
        AtomicInteger stockCount = stock.get(productId);
        if (stockCount != null && stockCount.get() > 0) {
            stockCount.decrementAndGet();
            return true;
        }
        return false;
    }

    /**
     * Get product by ID
     */
    public Product getProduct(String productId) {
        return products.get(productId);
    }

    /**
     * Get all products
     */
    public Map<String, Product> getAllProducts() {
        return new ConcurrentHashMap<>(products);
    }

    /**
     * Get inventory summary
     */
    public String getInventorySummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n=== INVENTORY ===\n");
        for (Map.Entry<String, Product> entry : products.entrySet()) {
            Product product = entry.getValue();
            int stockCount = getStockCount(product.getProductId());
            String status = stockCount == 0 ? " [OUT OF STOCK]" : 
                           isLowStock(product.getProductId()) ? " [LOW STOCK]" : "";
            sb.append(String.format("%-15s %-20s $%-6.2f Stock: %-5d%s\n",
                    product.getProductId(), product.getName(), product.getPrice(), 
                    stockCount, status));
        }
        sb.append("=================\n");
        return sb.toString();
    }
}

