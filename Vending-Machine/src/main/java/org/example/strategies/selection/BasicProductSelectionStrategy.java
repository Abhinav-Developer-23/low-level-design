package org.example.strategies.selection;

import org.example.interfaces.ProductSelectionStrategy;
import org.example.model.Product;
import java.util.Map;

/**
 * Basic product selection strategy that selects products by exact slot ID match.
 * This is the default selection method (e.g., "A1", "B2").
 * Follows Strategy Pattern and Single Responsibility Principle.
 */
public class BasicProductSelectionStrategy implements ProductSelectionStrategy {

    @Override
    public Product selectProduct(Map<String, Product> availableProducts, String criteria) {
        // Direct lookup by slot ID (case-insensitive)
        String slotId = criteria.toUpperCase();
        return availableProducts.get(slotId);
    }

    @Override
    public String getSelectionMethod() {
        return "SLOT_ID";
    }
}

