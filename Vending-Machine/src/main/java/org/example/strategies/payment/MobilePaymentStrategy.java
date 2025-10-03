package org.example.strategies.payment;

import org.example.interfaces.PaymentStrategy;

import java.util.Random;

/**
 * Mobile Payment Strategy: Handles mobile wallet payments.
 * Simulates mobile payment processing with QR code or NFC.
 */
public class MobilePaymentStrategy implements PaymentStrategy {
    private final Random random;
    private final double successRate; // 0.0 to 1.0
    private boolean mobilePaymentEnabled;
    private final String[] supportedApps;

    public MobilePaymentStrategy() {
        this(0.85); // 85% success rate by default (slightly lower than card)
    }

    public MobilePaymentStrategy(double successRate) {
        this.random = new Random();
        this.successRate = Math.max(0.0, Math.min(1.0, successRate));
        this.mobilePaymentEnabled = true;
        this.supportedApps = new String[]{"Apple Pay", "Google Pay", "Samsung Pay", "PayPal"};
    }

    @Override
    public boolean processPayment(int amount) {
        if (!isAvailable()) {
            System.out.println("Mobile payment is not available");
            return false;
        }

        System.out.println("Processing mobile payment for $" + String.format("%.2f", amount / 100.0));
        System.out.println("Please scan QR code or tap your device...");

        // Simulate processing time
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            return false;
        }

        // Simulate authorization with configured success rate
        boolean success = random.nextDouble() < successRate;

        if (success) {
            String selectedApp = supportedApps[random.nextInt(supportedApps.length)];
            System.out.println("Mobile payment authorized via " + selectedApp + "!");
            return true;
        } else {
            System.out.println("Mobile payment failed. Please try again or use a different payment method.");
            return false;
        }
    }

    @Override
    public String getPaymentMethodName() {
        return "Mobile";
    }

    @Override
    public boolean isAvailable() {
        return mobilePaymentEnabled;
    }

    /**
     * Sets whether mobile payment is enabled.
     * @param enabled true to enable mobile payments
     */
    public void setMobilePaymentEnabled(boolean enabled) {
        this.mobilePaymentEnabled = enabled;
    }

    /**
     * Gets the configured success rate for mobile payments.
     * @return Success rate between 0.0 and 1.0
     */
    public double getSuccessRate() {
        return successRate;
    }

    /**
     * Gets the list of supported mobile payment apps.
     * @return Array of supported app names
     */
    public String[] getSupportedApps() {
        return supportedApps.clone();
    }

    /**
     * Generates a QR code string for mobile payment.
     * @param amount Payment amount in cents
     * @param transactionId Unique transaction identifier
     * @return QR code data string
     */
    public String generateQRCode(int amount, String transactionId) {
        return String.format("VENDING_PAY:%s:%.2f:%s",
                           transactionId,
                           amount / 100.0,
                           java.time.LocalDateTime.now().toString());
    }

    /**
     * Simulates NFC tap detection.
     * @return true if NFC tap is detected (simulated randomly)
     */
    public boolean detectNFCTap() {
        // Simulate NFC detection with 95% success rate when attempted
        return random.nextDouble() < 0.95;
    }
}
