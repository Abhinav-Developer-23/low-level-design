package org.example.quickcommerce.exception;

public class PaymentFailedException extends QuickCommerceException {
    public PaymentFailedException(String orderId) {
        super("Payment failed for order: " + orderId);
    }
}

