package org.example.strategies.selection;

import org.example.interfaces.ProductSelectionStrategy;
import org.example.model.Product;

import java.util.Map;

/**
 * Product selection by name (case-insensitive)
 */
public class NameBasedSelectionStrategy implements ProductSelectionStrategy {
    
    @Override
    public Product selectProduct(String identifier, Map<String, Product> products) {
        if (identifier == null || identifier.trim().isEmpty()) {
            return null;
        }
        
        String searchName = identifier.toLowerCase().trim();
        
        // Search by name (case-insensitive, partial match)
        return products.values().stream()
                .filter(p -> p.getName().toLowerCase().contains(searchName))
                .findFirst()
                .orElse(null);
    }

    @Override
    public String getStrategyName() {
        return "Name-Based Selection";
    }
}

