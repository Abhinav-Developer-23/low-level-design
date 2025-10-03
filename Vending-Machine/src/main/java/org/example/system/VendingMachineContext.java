package org.example.system;

import org.example.enums.PaymentMethod;
import org.example.enums.ProductType;
import org.example.interfaces.PaymentStrategy;
import org.example.interfaces.ProductSelectionStrategy;
import org.example.interfaces.State;
import org.example.interfaces.VendingMachineObserver;
import org.example.model.Inventory;
import org.example.model.Product;
import org.example.model.Transaction;
import org.example.observers.ConsoleVendingObserver;
import org.example.observers.MaintenanceObserver;
import org.example.states.IdleState;
import org.example.strategies.payment.CashPaymentStrategy;
import org.example.strategies.selection.BasicProductSelectionStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Context class for the Vending Machine that manages the state pattern implementation
 * and coordinates all the components of the vending machine system.
 */
public class VendingMachineContext {
    private State currentState;
    private Transaction currentTransaction;
    private final Inventory inventory;
    private final List<VendingMachineObserver> observers;

    // Strategy components
    private PaymentStrategy paymentStrategy;
    private ProductSelectionStrategy productSelectionStrategy;

    // Machine configuration
    private final String machineId;
    private boolean isOperational;

    public VendingMachineContext() {
        this.machineId = "VM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.inventory = new Inventory();
        this.observers = new ArrayList<>();
        this.isOperational = true;

        // Initialize with default strategies
        this.paymentStrategy = new CashPaymentStrategy();
        this.productSelectionStrategy = new BasicProductSelectionStrategy();

        // Initialize with idle state
        this.currentState = new IdleState(this);

        // Add default observers
        addObserver(new ConsoleVendingObserver());
        addObserver(new MaintenanceObserver());

        System.out.println("Vending Machine " + machineId + " initialized and ready!");
    }

    // State management methods
    public void setCurrentState(State state) {
        this.currentState = state;
        System.out.println("Machine state changed to: " + state.getStateName());
    }

    public State getCurrentState() {
        return currentState;
    }

    // Transaction management methods
    public Transaction getCurrentTransaction() {
        return currentTransaction;
    }

    public void setCurrentTransaction(Transaction transaction) {
        this.currentTransaction = transaction;
    }

    public Transaction createTransaction(String productId) {
        Product product = inventory.getProduct(productId);
        if (product != null) {
            Transaction transaction = new Transaction(
                UUID.randomUUID().toString(),
                productId,
                product.getPrice(),
                PaymentMethod.CASH // Default payment method
            );
            this.currentTransaction = transaction;
            notifyObservers("onProductSelected", productId);
            return transaction;
        }
        return null;
    }

    public void cancelCurrentTransaction() {
        if (currentTransaction != null) {
            // Return any inserted coins
            if (!currentTransaction.getInsertedCoins().isEmpty()) {
                int refundAmount = currentTransaction.getTotalInsertedAmount();
                System.out.println("Refunding " + refundAmount + " cents");
                returnChange(refundAmount);
            }
            this.currentTransaction = null;
        }
    }

    // Inventory management methods
    public Inventory getInventory() {
        return inventory;
    }

    public void displayInventory() {
        System.out.println("\n=== VENDING MACHINE INVENTORY ===");
        for (Product product : inventory.getAllProducts().values()) {
            String status = product.isAvailable() ? "Available" : "Out of Stock";
            System.out.printf("%s: %s - %d cents [%s]\n",
                            product.getId(),
                            product.getName(),
                            product.getPrice(),
                            status);
        }
        System.out.println("=============================\n");
    }

    // Observer management methods
    public void addObserver(VendingMachineObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(VendingMachineObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(String event, Object... args) {
        for (VendingMachineObserver observer : observers) {
            switch (event) {
                case "onProductSelected":
                    observer.onProductSelected((String) args[0]);
                    break;
                case "onCoinInserted":
                    observer.onCoinInserted((Integer) args[0]);
                    break;
                case "onPaymentProcessed":
                    observer.onPaymentProcessed((Integer) args[0], (String) args[1]);
                    break;
                case "onProductDispensed":
                    observer.onProductDispensed((String) args[0]);
                    break;
                case "onTransactionCompleted":
                    observer.onTransactionCompleted((String) args[0]);
                    break;
                case "onTransactionFailed":
                    observer.onTransactionFailed((String) args[0], (String) args[1]);
                    break;
                case "onMaintenanceRequired":
                    observer.onMaintenanceRequired((String) args[0]);
                    break;
            }
        }
    }

    // Strategy management methods
    public PaymentStrategy getPaymentStrategy() {
        return paymentStrategy;
    }

    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    public ProductSelectionStrategy getProductSelectionStrategy() {
        return productSelectionStrategy;
    }

    public void setProductSelectionStrategy(ProductSelectionStrategy productSelectionStrategy) {
        this.productSelectionStrategy = productSelectionStrategy;
    }

    // Coin/change handling methods
    public void returnChange(int amount) {
        if (amount > 0) {
            System.out.println("Returning " + amount + " cents in change:");
            // In a real system, this would control the coin return mechanism
            // For demo purposes, we'll just log it
            System.out.println("Change dispensed: " + amount + " cents");
        }
    }

    // State action methods (delegate to current state)
    public void selectProduct(String productId) {
        currentState.selectProduct(productId);
    }

    public void insertCoin(int coinValue) {
        notifyObservers("onCoinInserted", coinValue);
        currentState.insertCoin(coinValue);
    }

    public void processPayment() {
        currentState.processPayment();
    }

    public void dispenseProduct() {
        currentState.dispenseProduct();
    }

    public void cancelTransaction() {
        currentState.cancelTransaction();
    }

    // Machine status methods
    public String getMachineId() {
        return machineId;
    }

    public boolean isOperational() {
        return isOperational;
    }

    public void setOperational(boolean operational) {
        this.isOperational = operational;
        if (!operational) {
            System.out.println("Vending machine is now out of service");
        } else {
            System.out.println("Vending machine is now operational");
        }
    }

    public String getMachineStatus() {
        return String.format("Machine %s - State: %s - Operational: %s",
                           machineId, currentState.getStateName(), isOperational);
    }
}
