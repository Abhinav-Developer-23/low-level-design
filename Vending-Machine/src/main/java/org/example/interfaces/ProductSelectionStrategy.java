package org.example.interfaces;

import org.example.model.Product;
import java.util.Map;

/**
 * Strategy interface for different product selection methods.
 * Allows flexibility in how products are selected (by slot ID, by name, etc.).
 * Follows Strategy Pattern and Open/Closed Principle.
 */
public interface ProductSelectionStrategy {
    
    /**
     * Selects a product based on the given criteria.
     * 
     * @param availableProducts map of available products (key: product ID)
     * @param criteria the selection criteria (e.g., slot ID, product name)
     * @return the selected product, or null if not found
     */
    Product selectProduct(Map<String, Product> availableProducts, String criteria);
    
    /**
     * Gets the name of the selection method.
     * 
     * @return the selection method name
     */
    String getSelectionMethod();
}

