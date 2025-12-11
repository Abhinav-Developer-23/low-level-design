package org.example.quickcommerce.service;

import org.example.quickcommerce.enums.ProductCategory;
import org.example.quickcommerce.model.Product;
import org.example.quickcommerce.repository.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Service for managing products.
 * Uses Repository singleton for data storage.
 */
public class ProductService {

    private ConcurrentHashMap<String, Product> getProductDb() {
        return Repository.getInstance().getProductDb();
    }

    public Product addProduct(String name, double price, ProductCategory category, String unit) {
        Product product = new Product(name, price, category, unit);
        getProductDb().put(product.getProductId(), product);
        return product;
    }

    public Product addProduct(String name, double price, ProductCategory category,
                              String unit, String description) {
        Product product = new Product(name, price, category, unit, description);
        getProductDb().put(product.getProductId(), product);
        return product;
    }

    public Optional<Product> getProduct(String productId) {
        return Optional.ofNullable(getProductDb().get(productId));
    }

    public Product getProductOrThrow(String productId) {
        return getProduct(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(getProductDb().values());
    }

    public List<Product> getProductsByCategory(ProductCategory category) {
        return getProductDb().values().stream()
                .filter(p -> p.getCategory() == category)
                .collect(Collectors.toList());
    }

    public List<Product> searchProducts(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return getProductDb().values().stream()
                .filter(p -> p.getName().toLowerCase().contains(lowerKeyword) ||
                        (p.getDescription() != null &&
                                p.getDescription().toLowerCase().contains(lowerKeyword)))
                .collect(Collectors.toList());
    }

    public void updatePrice(String productId, double newPrice) {
        getProductOrThrow(productId).setPrice(newPrice);
    }

    public void removeProduct(String productId) {
        getProductDb().remove(productId);
    }
}

