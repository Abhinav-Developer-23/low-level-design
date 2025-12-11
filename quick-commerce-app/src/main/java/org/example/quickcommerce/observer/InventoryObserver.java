package org.example.quickcommerce.observer;

/**
 * Observer interface for inventory changes.
 */
public interface InventoryObserver {
    void onInventoryChange(String warehouseId, String productId, int newQuantity);
}

