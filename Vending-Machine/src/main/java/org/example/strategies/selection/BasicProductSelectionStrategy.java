package org.example.strategies.selection;

import org.example.interfaces.ProductSelectionStrategy;
import org.example.model.Product;

import java.util.Map;

/**
 * Basic product selection by product ID (e.g., "A1", "B2")
 */
public class BasicProductSelectionStrategy implements ProductSelectionStrategy {
    
    @Override
    public Product selectProduct(String identifier, Map<String, Product> products) {
        if (identifier == null || identifier.trim().isEmpty()) {
            return null;
        }
        
        // Direct lookup by product ID
        return products.get(identifier.toUpperCase().trim());
    }

    @Override
    public String getStrategyName() {
        return "Basic Selection (Product ID)";
    }
}

