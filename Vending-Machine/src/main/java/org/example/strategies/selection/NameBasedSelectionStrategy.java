package org.example.strategies.selection;

import org.example.enums.ProductType;
import org.example.interfaces.ProductSelectionStrategy;
import org.example.model.Inventory;
import org.example.model.Product;

import java.util.Map;
import java.util.Optional;

/**
 * Name-based Product Selection Strategy: Selects products by name or type.
 * Supports partial name matching and category-based selection.
 */
public class NameBasedSelectionStrategy implements ProductSelectionStrategy {

    @Override
    public String selectProduct(Object inventory, String criteria) {
        if (!(inventory instanceof Inventory)) {
            return null;
        }

        Inventory inv = (Inventory) inventory;
        String searchCriteria = criteria.toLowerCase().trim();

        // Try different matching strategies
        String slotId = findByExactName(inv, searchCriteria);
        if (slotId != null) {
            return slotId;
        }

        slotId = findByPartialName(inv, searchCriteria);
        if (slotId != null) {
            return slotId;
        }

        slotId = findByProductType(inv, searchCriteria);
        if (slotId != null) {
            return slotId;
        }

        System.out.println("No product found matching: " + criteria);
        return null;
    }

    @Override
    public String getStrategyName() {
        return "Name-based Selection";
    }

    /**
     * Finds product by exact name match.
     * @param inventory The inventory to search
     * @param name The exact product name
     * @return Slot ID if found and available, null otherwise
     */
    private String findByExactName(Inventory inventory, String name) {
        Map<String, Product> allProducts = inventory.getAllProducts();

        for (Map.Entry<String, Product> entry : allProducts.entrySet()) {
            String slotId = entry.getKey();
            Product product = entry.getValue();

            if (product.getName().equalsIgnoreCase(name) && inventory.isProductAvailable(slotId)) {
                return slotId;
            }
        }
        return null;
    }

    /**
     * Finds product by partial name match.
     * @param inventory The inventory to search
     * @param partialName Partial name to search for
     * @return Slot ID of first matching available product, null if none found
     */
    private String findByPartialName(Inventory inventory, String partialName) {
        Map<String, Product> allProducts = inventory.getAllProducts();

        for (Map.Entry<String, Product> entry : allProducts.entrySet()) {
            String slotId = entry.getKey();
            Product product = entry.getValue();

            if (product.getName().toLowerCase().contains(partialName) && inventory.isProductAvailable(slotId)) {
                return slotId;
            }
        }
        return null;
    }

    /**
     * Finds product by product type/category.
     * @param inventory The inventory to search
     * @param typeString Product type string (e.g., "beverage", "snack")
     * @return Slot ID of first available product of that type, null if none found
     */
    private String findByProductType(Inventory inventory, String typeString) {
        ProductType targetType = parseProductType(typeString);
        if (targetType == null) {
            return null;
        }

        Map<String, Product> allProducts = inventory.getAllProducts();

        for (Map.Entry<String, Product> entry : allProducts.entrySet()) {
            String slotId = entry.getKey();
            Product product = entry.getValue();

            if (product.getProductType() == targetType && inventory.isProductAvailable(slotId)) {
                return slotId;
            }
        }
        return null;
    }

    /**
     * Parses product type from string input.
     * @param typeString Type string (case insensitive)
     * @return ProductType enum value, or null if not recognized
     */
    private ProductType parseProductType(String typeString) {
        switch (typeString.toLowerCase()) {
            case "beverage":
            case "drink":
            case "drinks":
                return ProductType.BEVERAGE;

            case "snack":
            case "snacks":
            case "food":
                return ProductType.SNACK;

            case "candy":
            case "sweets":
            case "chocolate":
                return ProductType.CANDY;

            default:
                return null;
        }
    }

    /**
     * Gets all available products as formatted list for display.
     * @param inventory The inventory to list
     * @return Formatted string of available products
     */
    public String getAvailableProductsList(Inventory inventory) {
        Map<String, Product> available = inventory.getAvailableProducts();
        if (available.isEmpty()) {
            return "No products available";
        }

        StringBuilder sb = new StringBuilder("Available products:\n");
        for (Map.Entry<String, Product> entry : available.entrySet()) {
            String slotId = entry.getKey();
            Product product = entry.getValue();
            sb.append(String.format("  %s: %s (%s)\n",
                                  slotId,
                                  product.getName(),
                                  product.getProductType()));
        }
        return sb.toString();
    }
}
