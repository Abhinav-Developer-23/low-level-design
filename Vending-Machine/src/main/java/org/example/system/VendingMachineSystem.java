package org.example.system;

import org.example.enums.PaymentMethod;
import org.example.enums.ProductType;
import org.example.interfaces.VendingMachineObserver;
import org.example.model.Product;
import org.example.states.OutOfServiceState;
import org.example.strategies.payment.*;
import org.example.strategies.selection.*;

/**
 * VendingMachineSystem is a Facade that provides a simplified interface
 * to the complex vending machine system.
 * Follows Facade Pattern and provides high-level operations.
 */
public class VendingMachineSystem {
    private final VendingMachineContext context;

    /**
     * Creates a new VendingMachineSystem with a pre-populated inventory.
     */
    public VendingMachineSystem() {
        this.context = new VendingMachineContext();
        initializeInventory();
    }

    /**
     * Initializes the inventory with default products.
     */
    private void initializeInventory() {
        // Add products to inventory (SlotID, ProductType, Quantity)
        context.getInventory().addProduct("A1", ProductType.CHIPS, 10);
        context.getInventory().addProduct("A2", ProductType.CHOCOLATE, 8);
        context.getInventory().addProduct("A3", ProductType.SODA, 12);
        context.getInventory().addProduct("B1", ProductType.CANDY, 15);
        context.getInventory().addProduct("B2", ProductType.GUM, 20);
        context.getInventory().addProduct("B3", ProductType.WATER, 10);
        context.getInventory().addProduct("C1", ProductType.CHIPS, 5);
        context.getInventory().addProduct("C2", ProductType.CHOCOLATE, 3); // Low stock
        context.getInventory().addProduct("C3", ProductType.SODA, 7);
        
        System.out.println("Inventory initialized with products.");
    }

    /**
     * Displays the current inventory.
     */
    public void displayInventory() {
        context.displayInventory();
    }

    /**
     * Selects a product by its slot ID.
     * 
     * @param slotId the slot ID (e.g., "A1")
     */
    public void selectProductBySlot(String slotId) {
        // Use basic slot ID selection
        context.setProductSelectionStrategy(new BasicProductSelectionStrategy());
        context.selectProduct(slotId);
    }

    /**
     * Selects a product by its name or type.
     * 
     * @param productName the product name (e.g., "chocolate")
     */
    public void selectProductByName(String productName) {
        // Use name-based selection
        context.setProductSelectionStrategy(new NameBasedSelectionStrategy());
        Product product = context.getProductSelectionStrategy()
            .selectProduct(context.getInventory().getAllProducts(), productName);
        
        if (product != null) {
            context.selectProduct(product.getId());
        } else {
            System.out.println("ERROR: No product found matching '" + productName + "'");
        }
    }

    /**
     * Inserts a coin into the machine.
     * 
     * @param coinValue the coin value in cents
     */
    public void insertCoin(int coinValue) {
        context.insertCoin(coinValue);
    }

    /**
     * Sets the payment method.
     * 
     * @param paymentMethod the payment method to use
     */
    public void setPaymentMethod(PaymentMethod paymentMethod) {
        switch (paymentMethod) {
            case CASH:
                context.setPaymentStrategy(new CashPaymentStrategy());
                break;
            case CARD:
                context.setPaymentStrategy(new CardPaymentStrategy());
                break;
            case MOBILE:
                context.setPaymentStrategy(new MobilePaymentStrategy());
                break;
        }
    }

    /**
     * Processes the payment for the current transaction.
     */
    public void processPayment() {
        context.processPayment();
    }

    /**
     * Cancels the current transaction.
     */
    public void cancelTransaction() {
        context.cancelTransaction();
    }

    /**
     * Gets the current machine status.
     * 
     * @return the machine status string
     */
    public String getMachineStatus() {
        return context.getMachineStatus();
    }

    /**
     * Adds an observer to the vending machine.
     * 
     * @param observer the observer to add
     */
    public void addObserver(VendingMachineObserver observer) {
        context.addObserver(observer);
    }

    /**
     * Gets the context (for advanced operations).
     * 
     * @return the vending machine context
     */
    public VendingMachineContext getContext() {
        return context;
    }

    /**
     * Puts the machine into maintenance mode.
     */
    public void performMaintenance() {
        System.out.println("\n=== MAINTENANCE MODE ===");
        context.setOperational(false);
        context.setCurrentState(new OutOfServiceState(context));
        
        // Display inventory status
        System.out.println("\nCurrent Inventory Status:");
        context.displayInventory();
        
        // Display low stock items
        var lowStock = context.getInventory().getLowStockProducts();
        if (!lowStock.isEmpty()) {
            System.out.println("\n[WARNING] LOW STOCK ITEMS:");
            lowStock.values().forEach(p -> 
                System.out.println("  - " + p.toString())
            );
        }
        
        System.out.println("\nMaintenance complete. Call returnToService() to resume operations.");
    }

    /**
     * Returns the machine to service from maintenance mode.
     */
    public void returnToService() {
        if (context.getCurrentState() instanceof OutOfServiceState) {
            ((OutOfServiceState) context.getCurrentState()).returnToService();
        } else {
            System.out.println("Machine is not in maintenance mode.");
        }
    }

    /**
     * Restocks a product.
     * 
     * @param slotId the slot ID
     * @param quantity the quantity to add
     */
    public void restockProduct(String slotId, int quantity) {
        if (context.getInventory().restockProduct(slotId, quantity)) {
            System.out.println("Restocked " + slotId + " with " + quantity + " items.");
        } else {
            System.out.println("ERROR: Could not restock " + slotId + " (product not found).");
        }
    }
}

