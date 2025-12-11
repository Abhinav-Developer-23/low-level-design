package org.example.quickcommerce.exception;

public class NoDeliveryPartnerAvailableException extends QuickCommerceException {
    public NoDeliveryPartnerAvailableException() {
        super("No delivery partner available for assignment");
    }
}

