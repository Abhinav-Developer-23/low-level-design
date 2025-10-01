package org.example.interfaces;

import org.example.model.Product;
import java.util.List;

/**
 * Strategy Pattern: Interface for different product selection algorithms
 * Follows Interface Segregation Principle (ISP)
 */
public interface ProductSelectionStrategy {
    /**
     * Select a product based on the given criteria
     * @param products List of available products
     * @param criteria Selection criteria (could be product name, code, etc.)
     * @return The selected product or null if not found
     */
    Product selectProduct(List<Product> products, String criteria);
}
