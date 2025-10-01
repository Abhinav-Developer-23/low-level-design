package org.example.strategies.selection;

import org.example.interfaces.ProductSelectionStrategy;
import org.example.model.Product;

import java.util.List;

/**
 * Strategy Pattern: Basic product selection by product code
 * Selects product by exact match of product ID
 */
public class BasicProductSelectionStrategy implements ProductSelectionStrategy {

    @Override
    public Product selectProduct(List<Product> products, String criteria) {
        if (criteria == null || criteria.trim().isEmpty()) {
            return null;
        }

        String productCode = criteria.trim().toUpperCase();

        return products.stream()
                .filter(product -> product.getProductId().equalsIgnoreCase(productCode) && product.isAvailable())
                .findFirst()
                .orElse(null);
    }
}
