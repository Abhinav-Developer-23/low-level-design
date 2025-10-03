package org.example.system;

import org.example.enums.ProductType;
import org.example.interfaces.ProductSelectionStrategy;
import org.example.interfaces.VendingMachineObserver;
import org.example.model.Inventory;
import org.example.model.Product;
import org.example.states.IdleState;
import org.example.strategies.selection.BasicProductSelectionStrategy;

/**
 * Singleton Pattern: Single instance of the vending machine system
 * Thread-safe implementation using double-checked locking
 */
public class VendingMachineSystem {
    private static volatile VendingMachineSystem instance;
    
    private final VendingMachineContext context;
    private final Inventory inventory;
    private ProductSelectionStrategy selectionStrategy;

    private VendingMachineSystem() {
        this.inventory = new Inventory(5); // Low stock threshold: 5
        this.context = new VendingMachineContext(new IdleState(null), inventory);
        
        // Set the context reference in the initial state
        this.context.setState(new IdleState(context));
        
        // Default selection strategy
        this.selectionStrategy = new BasicProductSelectionStrategy();
    }

    /**
     * Thread-safe singleton implementation using double-checked locking
     */
    public static VendingMachineSystem getInstance() {
        if (instance == null) {
            synchronized (VendingMachineSystem.class) {
                if (instance == null) {
                    instance = new VendingMachineSystem();
                }
            }
        }
        return instance;
    }

    // Product Management
    public void addProduct(String productId, String name, double price, 
                          ProductType type, int calories, int stock) {
        Product product = new Product(productId, name, price, type, calories);
        inventory.addProduct(product, stock);
        System.out.println("✓ Product added: " + product);
    }

    public void restockProduct(String productId, int quantity) {
        inventory.restockProduct(productId, quantity);
        Product product = inventory.getProduct(productId);
        if (product != null) {
            System.out.println("✓ Restocked: " + product.getName() + " + " + quantity + " units");
        }
    }

    // Selection Strategy
    public void setSelectionStrategy(ProductSelectionStrategy strategy) {
        this.selectionStrategy = strategy;
        System.out.println("✓ Selection strategy changed to: " + strategy.getStrategyName());
    }

    // Machine Operations (delegated to context/states)
    public void selectProduct(String productId) {
        Product product = selectionStrategy.selectProduct(productId, inventory.getAllProducts());
        
        if (product == null) {
            System.out.println("❌ Product not found: " + productId);
            return;
        }
        
        context.selectProduct(product);
    }

    public void insertCoin(double amount) {
        context.insertCoin(amount);
    }

    public void insertCard(String cardNumber, double amount) {
        context.insertCard(cardNumber, amount);
    }

    public void insertMobilePayment(String paymentId, double amount) {
        context.insertMobilePayment(paymentId, amount);
    }

    public void cancelTransaction() {
        context.cancel();
    }

    // Observer management
    public void registerObserver(VendingMachineObserver observer) {
        context.registerObserver(observer);
        System.out.println("✓ Observer registered: " + observer.getClass().getSimpleName());
    }

    public void removeObserver(VendingMachineObserver observer) {
        context.removeObserver(observer);
    }

    // Display methods
    public void displayInventory() {
        System.out.println(inventory.getInventorySummary());
    }

    public void displayStatus() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("🤖 VENDING MACHINE STATUS");
        System.out.println("=".repeat(50));
        System.out.println("Current State: " + context.getCurrentStateName());
        
        if (context.getSelectedProduct() != null) {
            System.out.println("Selected Product: " + context.getSelectedProduct().getName());
            System.out.println("Price: $" + context.getSelectedProduct().getPrice());
            System.out.println("Amount Paid: $" + String.format("%.2f", context.getTotalPaid()));
            double remaining = context.getSelectedProduct().getPrice() - context.getTotalPaid();
            if (remaining > 0) {
                System.out.println("Remaining: $" + String.format("%.2f", remaining));
            }
        }
        
        System.out.println("=".repeat(50) + "\n");
    }

    public String getCurrentState() {
        return context.getCurrentStateName();
    }

    public Inventory getInventory() {
        return inventory;
    }

    public VendingMachineContext getContext() {
        return context;
    }
}

