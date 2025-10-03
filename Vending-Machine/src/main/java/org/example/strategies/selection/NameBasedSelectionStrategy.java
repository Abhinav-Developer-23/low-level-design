package org.example.strategies.selection;

import org.example.interfaces.ProductSelectionStrategy;
import org.example.model.Product;
import java.util.Map;

/**
 * Name-based product selection strategy that searches products by name or type.
 * Allows users to select products by typing product names instead of slot IDs.
 * Follows Strategy Pattern and Single Responsibility Principle.
 */
public class NameBasedSelectionStrategy implements ProductSelectionStrategy {

    @Override
    public Product selectProduct(Map<String, Product> availableProducts, String criteria) {
        String searchTerm = criteria.toLowerCase().trim();
        
        // Search for product by name (case-insensitive, partial match)
        for (Product product : availableProducts.values()) {
            if (product.getName().toLowerCase().contains(searchTerm)) {
                return product;
            }
        }
        
        // Search by product type
        for (Product product : availableProducts.values()) {
            if (product.getType().name().toLowerCase().contains(searchTerm)) {
                return product;
            }
        }
        
        return null; // No match found
    }

    @Override
    public String getSelectionMethod() {
        return "NAME_BASED";
    }
}

