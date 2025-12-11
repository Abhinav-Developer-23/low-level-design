package org.example.quickcommerce.exception;

public class InsufficientInventoryException extends QuickCommerceException {
    public InsufficientInventoryException(String productId, int requested, int available) {
        super(String.format("Insufficient inventory for product %s. Requested: %d, Available: %d",
                productId, requested, available));
    }
}

