package org.example.quickcommerce.service;

import org.example.quickcommerce.model.Location;
import org.example.quickcommerce.model.Warehouse;
import org.example.quickcommerce.observer.InventoryObserver;
import org.example.quickcommerce.repository.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service for managing warehouses and inventory.
 * Uses Repository singleton for data storage.
 * Implements Observer pattern for inventory change notifications.
 */
public class InventoryService {

    private final List<InventoryObserver> observers = new ArrayList<>();

    private ConcurrentHashMap<String, Warehouse> getWarehouseDb() {
        return Repository.getInstance().getWarehouseDb();
    }

    // Observer pattern methods
    public void addObserver(InventoryObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(InventoryObserver observer) {
        observers.remove(observer);
    }

    private void notifyInventoryChange(String warehouseId, String productId, int newQuantity) {
        for (InventoryObserver observer : observers) {
            observer.onInventoryChange(warehouseId, productId, newQuantity);
        }
    }

    // Warehouse management
    public Warehouse createWarehouse(String name, Location location) {
        Warehouse warehouse = new Warehouse(name, location);
        getWarehouseDb().put(warehouse.getWarehouseId(), warehouse);
        return warehouse;
    }

    public Optional<Warehouse> getWarehouse(String warehouseId) {
        return Optional.ofNullable(getWarehouseDb().get(warehouseId));
    }

    public Warehouse getWarehouseOrThrow(String warehouseId) {
        return getWarehouse(warehouseId)
                .orElseThrow(() -> new IllegalArgumentException("Warehouse not found: " + warehouseId));
    }

    public List<Warehouse> getAllWarehouses() {
        return new ArrayList<>(getWarehouseDb().values());
    }

    public Optional<Warehouse> findNearestWarehouse(Location userLocation) {
        return getWarehouseDb().values().stream()
                .min(Comparator.comparingDouble(w -> w.getLocation().distanceTo(userLocation)));
    }

    public Optional<Warehouse> findNearestWarehouseWithProduct(Location userLocation,
                                                                String productId,
                                                                int requiredQuantity) {
        return getWarehouseDb().values().stream()
                .filter(w -> w.hasStock(productId, requiredQuantity))
                .min(Comparator.comparingDouble(w -> w.getLocation().distanceTo(userLocation)));
    }

    // Inventory management
    public void addInventory(String warehouseId, String productId, int quantity) {
        Warehouse warehouse = getWarehouseOrThrow(warehouseId);
        warehouse.addProduct(productId, quantity);
        notifyInventoryChange(warehouseId, productId, warehouse.getProductQuantity(productId));
    }

    public void setInventory(String warehouseId, String productId, int quantity) {
        Warehouse warehouse = getWarehouseOrThrow(warehouseId);
        warehouse.setProductQuantity(productId, quantity);
        notifyInventoryChange(warehouseId, productId, quantity);
    }

    public int getInventory(String warehouseId, String productId) {
        return getWarehouseOrThrow(warehouseId).getProductQuantity(productId);
    }

    public boolean hasStock(String warehouseId, String productId, int quantity) {
        return getWarehouseOrThrow(warehouseId).hasStock(productId, quantity);
    }

    public boolean reduceInventory(String warehouseId, String productId, int quantity) {
        Warehouse warehouse = getWarehouseOrThrow(warehouseId);
        boolean success = warehouse.reduceInventory(productId, quantity);
        if (success) {
            notifyInventoryChange(warehouseId, productId, warehouse.getProductQuantity(productId));
        }
        return success;
    }

    public void restoreInventory(String warehouseId, String productId, int quantity) {
        Warehouse warehouse = getWarehouseOrThrow(warehouseId);
        warehouse.restoreInventory(productId, quantity);
        notifyInventoryChange(warehouseId, productId, warehouse.getProductQuantity(productId));
    }

    public Set<String> getOutOfStockProducts(String warehouseId) {
        Warehouse warehouse = getWarehouseOrThrow(warehouseId);
        Set<String> outOfStock = new HashSet<>();
        for (Map.Entry<String, Integer> entry : warehouse.getInventory().entrySet()) {
            if (entry.getValue() <= 0) {
                outOfStock.add(entry.getKey());
            }
        }
        return outOfStock;
    }
}

