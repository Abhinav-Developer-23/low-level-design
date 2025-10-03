package org.example.strategies.selection;

import org.example.interfaces.ProductSelectionStrategy;
import org.example.model.Product;
import java.util.List;

/**
 * Basic product selection strategy that selects products by exact slot ID match.
 */
public class BasicProductSelectionStrategy implements ProductSelectionStrategy {

    @Override
    public Product selectProduct(List<Product> availableProducts, String criteria) {
        for (Product product : availableProducts) {
            if (product.getId().equalsIgnoreCase(criteria.trim())) {
                return product;
            }
        }
        return null;
    }

    @Override
    public String getSelectionMethod() {
        return "SLOT_ID";
    }
}
