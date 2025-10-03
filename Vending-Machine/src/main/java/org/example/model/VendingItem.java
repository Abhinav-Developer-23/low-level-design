package org.example.model;

import org.example.interfaces.Dispensable;

/**
 * Wrapper for products with stock information
 */
public class VendingItem implements Dispensable {
    private final Product product;
    private int stockCount;

    public VendingItem(Product product, int initialStock) {
        this.product = product;
        this.stockCount = initialStock;
    }

    @Override
    public String getId() {
        return product.getId();
    }

    @Override
    public String getName() {
        return product.getName();
    }

    @Override
    public double getPrice() {
        return product.getPrice();
    }

    @Override
    public boolean isAvailable() {
        return stockCount > 0;
    }

    public Product getProduct() {
        return product;
    }

    public synchronized int getStockCount() {
        return stockCount;
    }

    public synchronized boolean decrementStock() {
        if (stockCount > 0) {
            stockCount--;
            return true;
        }
        return false;
    }

    public synchronized void incrementStock(int count) {
        stockCount += count;
    }

    @Override
    public String toString() {
        return product.toString() + " - Stock: " + stockCount;
    }
}

