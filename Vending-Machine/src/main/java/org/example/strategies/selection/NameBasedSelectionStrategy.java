package org.example.strategies.selection;

import org.example.interfaces.ProductSelectionStrategy;
import org.example.model.Product;
import java.util.List;

/**
 * Product selection strategy that allows selection by product name (partial matching).
 */
public class NameBasedSelectionStrategy implements ProductSelectionStrategy {

    @Override
    public Product selectProduct(List<Product> availableProducts, String criteria) {
        String searchCriteria = criteria.toLowerCase().trim();

        for (Product product : availableProducts) {
            if (product.getName().toLowerCase().contains(searchCriteria) ||
                product.getType().name().toLowerCase().contains(searchCriteria)) {
                return product;
            }
        }
        return null;
    }

    @Override
    public String getSelectionMethod() {
        return "NAME_SEARCH";
    }
}
