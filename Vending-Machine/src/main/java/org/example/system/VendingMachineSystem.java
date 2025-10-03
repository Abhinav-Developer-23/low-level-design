package org.example.system;

import org.example.enums.PaymentMethod;
import org.example.interfaces.PaymentStrategy;
import org.example.interfaces.ProductSelectionStrategy;
import org.example.interfaces.VendingMachineObserver;
import org.example.model.Product;
import org.example.strategies.payment.CardPaymentStrategy;
import org.example.strategies.payment.CashPaymentStrategy;
import org.example.strategies.payment.MobilePaymentStrategy;
import org.example.strategies.selection.NameBasedSelectionStrategy;

/**
 * High-level facade class for the Vending Machine System.
 * Provides a simplified interface for interacting with the vending machine.
 */
public class VendingMachineSystem {
    private final VendingMachineContext context;

    public VendingMachineSystem() {
        this.context = new VendingMachineContext();
    }

    /**
     * Display the current inventory of the vending machine.
     */
    public void displayInventory() {
        context.displayInventory();
    }

    /**
     * Select a product by slot ID.
     *
     * @param slotId The slot ID of the product to select
     */
    public void selectProductBySlot(String slotId) {
        System.out.println("Selecting product by slot: " + slotId);
        context.selectProduct(slotId);
    }

    /**
     * Select a product by name or type.
     *
     * @param productName The name or type of the product to select
     */
    public void selectProductByName(String productName) {
        System.out.println("Searching for product: " + productName);

        // Temporarily switch to name-based selection strategy
        ProductSelectionStrategy originalStrategy = context.getProductSelectionStrategy();
        context.setProductSelectionStrategy(new NameBasedSelectionStrategy());

        // Try to find and select the product
        for (Product product : context.getInventory().getAllProducts().values()) {
            if (product.getName().toLowerCase().contains(productName.toLowerCase()) ||
                product.getType().name().toLowerCase().contains(productName.toLowerCase())) {
                if (product.isAvailable()) {
                    context.selectProduct(product.getId());
                    context.setProductSelectionStrategy(originalStrategy);
                    return;
                }
            }
        }

        System.out.println("Product not found or not available: " + productName);
        context.setProductSelectionStrategy(originalStrategy);
    }

    /**
     * Insert a coin into the vending machine.
     *
     * @param coinValue The value of the coin in cents
     */
    public void insertCoin(int coinValue) {
        System.out.println("Inserting coin: " + coinValue + " cents");
        context.insertCoin(coinValue);
    }

    /**
     * Set the payment method for the current transaction.
     *
     * @param paymentMethod The payment method to use
     */
    public void setPaymentMethod(PaymentMethod paymentMethod) {
        PaymentStrategy strategy = switch (paymentMethod) {
            case CASH -> new CashPaymentStrategy();
            case CARD -> new CardPaymentStrategy();
            case MOBILE -> new MobilePaymentStrategy();
        };

        context.setPaymentStrategy(strategy);
        System.out.println("Payment method set to: " + paymentMethod);
    }

    /**
     * Process the current payment.
     */
    public void processPayment() {
        System.out.println("Processing payment...");
        context.processPayment();
    }

    /**
     * Cancel the current transaction.
     */
    public void cancelTransaction() {
        System.out.println("Cancelling transaction...");
        context.cancelTransaction();
    }

    /**
     * Get the current machine status.
     *
     * @return String representation of the machine status
     */
    public String getMachineStatus() {
        return context.getMachineStatus();
    }

    /**
     * Add a custom observer to the vending machine.
     *
     * @param observer The observer to add
     */
    public void addObserver(VendingMachineObserver observer) {
        context.addObserver(observer);
    }

    /**
     * Get the vending machine context for advanced operations.
     *
     * @return The vending machine context
     */
    public VendingMachineContext getContext() {
        return context;
    }

    /**
     * Perform maintenance operations on the vending machine.
     */
    public void performMaintenance() {
        System.out.println("Performing maintenance on " + context.getMachineId());
        System.out.println("Checking inventory levels...");

        int totalItems = 0;
        int lowStockItems = 0;

        for (Product product : context.getInventory().getAllProducts().values()) {
            totalItems += product.getQuantity();
            if (product.getQuantity() <= 2) {
                lowStockItems++;
                context.notifyObservers("onMaintenanceRequired",
                    "Low stock for " + product.getName() + " - " + product.getQuantity() + " remaining");
            }
        }

        System.out.println("Total items in inventory: " + totalItems);
        System.out.println("Low stock items: " + lowStockItems);
        System.out.println("Maintenance check complete.");
    }
}
