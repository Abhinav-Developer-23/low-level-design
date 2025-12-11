package org.example.quickcommerce.model;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Represents a warehouse (dark store) that stores product inventory.
 */
public class Warehouse {
    private final String warehouseId;
    private String name;
    private Location location;
    private final Map<String, Integer> inventory; // productId -> quantity

    public Warehouse(String name, Location location) {
        this.warehouseId = UUID.randomUUID().toString();
        this.name = name;
        this.location = location;
        this.inventory = new HashMap<>();
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public Map<String, Integer> getInventory() {
        return new HashMap<>(inventory);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void addProduct(String productId, int quantity) {
        inventory.put(productId, inventory.getOrDefault(productId, 0) + quantity);
    }

    public void setProductQuantity(String productId, int quantity) {
        if (quantity <= 0) {
            inventory.remove(productId);
        } else {
            inventory.put(productId, quantity);
        }
    }

    public int getProductQuantity(String productId) {
        return inventory.getOrDefault(productId, 0);
    }

    public boolean hasProduct(String productId) {
        return inventory.containsKey(productId) && inventory.get(productId) > 0;
    }

    public boolean hasStock(String productId, int requiredQuantity) {
        return getProductQuantity(productId) >= requiredQuantity;
    }

    public boolean reduceInventory(String productId, int quantity) {
        int currentQuantity = getProductQuantity(productId);
        if (currentQuantity >= quantity) {
            setProductQuantity(productId, currentQuantity - quantity);
            return true;
        }
        return false;
    }

    public void restoreInventory(String productId, int quantity) {
        addProduct(productId, quantity);
    }

    @Override
    public String toString() {
        return "Warehouse{warehouseId='" + warehouseId + "', name='" + name + "'}";
    }
}

