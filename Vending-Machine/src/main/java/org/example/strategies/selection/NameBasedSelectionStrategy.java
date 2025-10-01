package org.example.strategies.selection;

import org.example.interfaces.ProductSelectionStrategy;
import org.example.model.Product;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Strategy Pattern: Product selection by name (partial match)
 * Selects products by name containing the search criteria
 */
public class NameBasedSelectionStrategy implements ProductSelectionStrategy {

    @Override
    public Product selectProduct(List<Product> products, String criteria) {
        if (criteria == null || criteria.trim().isEmpty()) {
            return null;
        }

        String searchTerm = criteria.trim().toLowerCase();

        List<Product> matchingProducts = products.stream()
                .filter(product -> product.getName().toLowerCase().contains(searchTerm) && product.isAvailable())
                .collect(Collectors.toList());

        // Return first match, or null if none found
        return matchingProducts.isEmpty() ? null : matchingProducts.get(0);
    }

    /**
     * Get all products matching the criteria
     */
    public List<Product> selectProducts(List<Product> products, String criteria) {
        if (criteria == null || criteria.trim().isEmpty()) {
            return List.of();
        }

        String searchTerm = criteria.trim().toLowerCase();

        return products.stream()
                .filter(product -> product.getName().toLowerCase().contains(searchTerm) && product.isAvailable())
                .collect(Collectors.toList());
    }
}
