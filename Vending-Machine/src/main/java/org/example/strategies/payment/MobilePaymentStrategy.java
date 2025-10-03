package org.example.strategies.payment;

import org.example.enums.PaymentMethod;
import org.example.interfaces.PaymentStrategy;

/**
 * Mobile payment strategy - NFC, QR codes, mobile wallets
 */
public class MobilePaymentStrategy implements PaymentStrategy {
    
    @Override
    public boolean processPayment(double amount) {
        if (amount <= 0) {
            return false;
        }
        
        System.out.println("📱 Processing mobile payment: $" + String.format("%.2f", amount));
        
        // Simulate mobile payment processing
        try {
            Thread.sleep(200); // Simulate NFC/QR scan
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
        
        // In real implementation:
        // 1. Read NFC/QR code
        // 2. Connect to mobile payment service (Apple Pay, Google Pay, etc.)
        // 3. Validate user authentication
        // 4. Process transaction
        
        // For demo, we simulate 98% success rate (mobile payments are typically reliable)
        boolean success = Math.random() < 0.98;
        
        if (success) {
            System.out.println("✓ Mobile payment approved");
        } else {
            System.out.println("❌ Mobile payment failed");
        }
        
        return success;
    }

    @Override
    public boolean refundPayment(double amount) {
        if (amount <= 0) {
            return true;
        }
        
        System.out.println("📱 Processing mobile payment refund: $" + String.format("%.2f", amount));
        
        // In real implementation, this would reverse the mobile payment
        return true;
    }

    @Override
    public PaymentMethod getPaymentMethod() {
        return PaymentMethod.MOBILE;
    }

    @Override
    public String getPaymentMethodName() {
        return "Mobile Payment (NFC/QR)";
    }
}

