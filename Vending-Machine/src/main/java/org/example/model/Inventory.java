package org.example.model;

import org.example.enums.ProductType;
import java.util.HashMap;
import java.util.Map;

/**
 * Model class representing the inventory of products in the vending machine.
 */
public class Inventory {
    private final Map<String, Product> products;

    public Inventory() {
        this.products = new HashMap<>();
        initializeInventory();
    }

    private void initializeInventory() {
        // Initialize with some default products
        addProduct("A1", ProductType.CHIPS, 10);
        addProduct("A2", ProductType.CHOCOLATE, 8);
        addProduct("B1", ProductType.SODA, 12);
        addProduct("B2", ProductType.CANDY, 15);
        addProduct("C1", ProductType.GUM, 20);
        addProduct("C2", ProductType.WATER, 6);
    }

    public void addProduct(String slotId, ProductType type, int quantity) {
        Product product = new Product(slotId, type, quantity);
        products.put(slotId, product);
    }

    public Product getProduct(String slotId) {
        return products.get(slotId);
    }

    public Map<String, Product> getAllProducts() {
        return new HashMap<>(products);
    }

    public boolean isProductAvailable(String slotId) {
        Product product = products.get(slotId);
        return product != null && product.isAvailable();
    }

    public Product dispenseProduct(String slotId) {
        Product product = products.get(slotId);
        if (product != null && product.isAvailable()) {
            product.decrementQuantity();
            return new Product(product.getId(), product.getType(), 1);
        }
        return null;
    }

    public void restockProduct(String slotId, int quantity) {
        Product product = products.get(slotId);
        if (product != null) {
            product.setQuantity(product.getQuantity() + quantity);
        }
    }

    public int getTotalValue() {
        return products.values().stream()
                     .mapToInt(product -> product.getPrice() * product.getQuantity())
                     .sum();
    }

    public int getTotalItems() {
        return products.values().stream()
                     .mapToInt(Product::getQuantity)
                     .sum();
    }
}
