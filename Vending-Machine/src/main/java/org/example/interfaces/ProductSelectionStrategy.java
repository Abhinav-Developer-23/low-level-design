package org.example.interfaces;

import org.example.model.Product;
import java.util.List;

/**
 * Strategy interface for handling different product selection methods in the vending machine.
 */
public interface ProductSelectionStrategy {
    /**
     * Select a product based on the strategy implementation.
     *
     * @param availableProducts List of available products
     * @param criteria Selection criteria (could be product ID, name, etc.)
     * @return The selected product or null if not found
     */
    Product selectProduct(List<Product> availableProducts, String criteria);

    /**
     * Get the selection method name.
     *
     * @return The selection method name
     */
    String getSelectionMethod();
}
