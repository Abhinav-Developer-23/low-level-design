package org.example.interfaces;

import org.example.model.Product;
import java.util.Map;

/**
 * Strategy Pattern: Different ways to select products
 */
public interface ProductSelectionStrategy {
    /**
     * Select a product based on strategy
     * @param identifier Product identifier (could be code, name, etc.)
     * @param products Available products
     * @return Selected product or null if not found
     */
    Product selectProduct(String identifier, Map<String, Product> products);
    
    /**
     * Get strategy name
     */
    String getStrategyName();
}

