package org.example.system;

import org.example.interfaces.State;
import org.example.interfaces.PaymentStrategy;
import org.example.interfaces.ProductSelectionStrategy;
import org.example.interfaces.VendingMachineObserver;
import org.example.model.Inventory;
import org.example.model.Transaction;
import org.example.model.Product;
import org.example.enums.PaymentMethod;
import org.example.enums.CoinType;
import org.example.states.IdleState;
import org.example.strategies.payment.CashPaymentStrategy;
import org.example.strategies.selection.BasicProductSelectionStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * VendingMachineContext is the context class for the State pattern.
 * It maintains the current state and coordinates all components of the vending machine.
 * Follows State Pattern, Strategy Pattern, and Observer Pattern.
 * Also follows Single Responsibility and Dependency Inversion principles.
 */
public class VendingMachineContext {
    private State currentState;
    private Transaction currentTransaction;
    private final Inventory inventory;
    private final List<VendingMachineObserver> observers;
    private PaymentStrategy paymentStrategy;
    private ProductSelectionStrategy productSelectionStrategy;
    private final String machineId;
    private boolean isOperational;

    /**
     * Creates a new VendingMachineContext with default settings.
     */
    public VendingMachineContext() {
        this.machineId = "VM-" + UUID.randomUUID().toString().substring(0, 8);
        this.inventory = new Inventory();
        this.observers = new ArrayList<>();
        this.currentState = new IdleState(this);
        this.paymentStrategy = new CashPaymentStrategy(); // Default to cash
        this.productSelectionStrategy = new BasicProductSelectionStrategy(); // Default to slot ID
        this.isOperational = true;
        
        System.out.println("Vending Machine " + machineId + " initialized.");
    }

    // ==================== State Management ====================

    /**
     * Sets the current state of the vending machine.
     * 
     * @param state the new state
     */
    public void setCurrentState(State state) {
        System.out.println("State transition: " + 
            (currentState != null ? currentState.getStateName() : "NONE") + 
            " -> " + state.getStateName());
        this.currentState = state;
    }

    /**
     * Gets the current state.
     * 
     * @return the current state
     */
    public State getCurrentState() {
        return currentState;
    }

    // ==================== Transaction Management ====================

    /**
     * Gets the current transaction.
     * 
     * @return the current transaction
     */
    public Transaction getCurrentTransaction() {
        return currentTransaction;
    }

    /**
     * Sets the current transaction.
     * 
     * @param transaction the transaction to set
     */
    public void setCurrentTransaction(Transaction transaction) {
        this.currentTransaction = transaction;
    }

    /**
     * Creates a new transaction for the given product.
     * 
     * @param productId the product ID
     * @return the created transaction
     */
    public Transaction createTransaction(String productId) {
        Product product = inventory.getProduct(productId);
        if (product != null) {
            this.currentTransaction = new Transaction(
                productId, 
                product.getPrice(), 
                paymentStrategy instanceof CashPaymentStrategy ? 
                    PaymentMethod.CASH : PaymentMethod.CARD
            );
            return currentTransaction;
        }
        return null;
    }

    /**
     * Cancels the current transaction.
     */
    public void cancelCurrentTransaction() {
        this.currentTransaction = null;
    }

    // ==================== Inventory Management ====================

    /**
     * Gets the inventory.
     * 
     * @return the inventory
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Displays the current inventory.
     */
    public void displayInventory() {
        System.out.println("\n" + inventory.toString());
    }

    // ==================== Observer Management ====================

    /**
     * Adds an observer to the vending machine.
     * 
     * @param observer the observer to add
     */
    public void addObserver(VendingMachineObserver observer) {
        observers.add(observer);
        System.out.println("Observer added: " + observer.getClass().getSimpleName());
    }

    /**
     * Removes an observer from the vending machine.
     * 
     * @param observer the observer to remove
     */
    public void removeObserver(VendingMachineObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notifies all observers of an event.
     * 
     * @param event the event type
     * @param args the event arguments
     */
    public void notifyObservers(String event, Object... args) {
        for (VendingMachineObserver observer : observers) {
            switch (event) {
                case "PRODUCT_SELECTED":
                    observer.onProductSelected((String) args[0]);
                    break;
                case "COIN_INSERTED":
                    observer.onCoinInserted((Integer) args[0]);
                    break;
                case "PAYMENT_PROCESSED":
                    observer.onPaymentProcessed((Integer) args[0], (String) args[1]);
                    break;
                case "PRODUCT_DISPENSED":
                    observer.onProductDispensed((String) args[0]);
                    break;
                case "TRANSACTION_COMPLETED":
                    observer.onTransactionCompleted((String) args[0]);
                    break;
                case "TRANSACTION_FAILED":
                    observer.onTransactionFailed((String) args[0], (String) args[1]);
                    break;
                case "MAINTENANCE_REQUIRED":
                    observer.onMaintenanceRequired((String) args[0]);
                    break;
            }
        }
    }

    // ==================== Strategy Management ====================

    /**
     * Gets the current payment strategy.
     * 
     * @return the payment strategy
     */
    public PaymentStrategy getPaymentStrategy() {
        return paymentStrategy;
    }

    /**
     * Sets the payment strategy.
     * 
     * @param strategy the payment strategy
     */
    public void setPaymentStrategy(PaymentStrategy strategy) {
        this.paymentStrategy = strategy;
        System.out.println("Payment method changed to: " + strategy.getPaymentMethod());
    }

    /**
     * Gets the current product selection strategy.
     * 
     * @return the product selection strategy
     */
    public ProductSelectionStrategy getProductSelectionStrategy() {
        return productSelectionStrategy;
    }

    /**
     * Sets the product selection strategy.
     * 
     * @param strategy the product selection strategy
     */
    public void setProductSelectionStrategy(ProductSelectionStrategy strategy) {
        this.productSelectionStrategy = strategy;
    }

    // ==================== Machine Operations ====================

    /**
     * Returns change to the user.
     * 
     * @param amount the amount to return in cents
     */
    public void returnChange(int amount) {
        if (amount <= 0) {
            return;
        }

        System.out.println("\n[CHANGE] Returning $" + String.format("%.2f", amount / 100.0));
        
        // Calculate change in coins (greedy algorithm)
        int remaining = amount;
        List<String> changeCoins = new ArrayList<>();
        
        CoinType[] coins = {CoinType.DOLLAR, CoinType.QUARTER, CoinType.DIME, 
                           CoinType.NICKEL, CoinType.PENNY};
        
        for (CoinType coin : coins) {
            int count = remaining / coin.getValue();
            if (count > 0) {
                changeCoins.add(count + "x " + coin.name());
                remaining -= count * coin.getValue();
            }
        }
        
        System.out.println("[CHANGE] " + String.join(", ", changeCoins));
    }

    /**
     * Delegates product selection to the current state.
     * 
     * @param productId the product ID
     */
    public void selectProduct(String productId) {
        currentState.selectProduct(productId);
    }

    /**
     * Delegates coin insertion to the current state.
     * 
     * @param coinValue the coin value in cents
     */
    public void insertCoin(int coinValue) {
        currentState.insertCoin(coinValue);
    }

    /**
     * Delegates payment processing to the current state.
     */
    public void processPayment() {
        currentState.processPayment();
    }

    /**
     * Delegates product dispensing to the current state.
     */
    public void dispenseProduct() {
        currentState.dispenseProduct();
    }

    /**
     * Delegates transaction cancellation to the current state.
     */
    public void cancelTransaction() {
        currentState.cancelTransaction();
    }

    // ==================== Machine Status ====================

    /**
     * Gets the machine ID.
     * 
     * @return the machine ID
     */
    public String getMachineId() {
        return machineId;
    }

    /**
     * Checks if the machine is operational.
     * 
     * @return true if operational, false otherwise
     */
    public boolean isOperational() {
        return isOperational;
    }

    /**
     * Sets the operational status of the machine.
     * 
     * @param operational the operational status
     */
    public void setOperational(boolean operational) {
        this.isOperational = operational;
    }

    /**
     * Gets the current machine status as a string.
     * 
     * @return the machine status
     */
    public String getMachineStatus() {
        StringBuilder status = new StringBuilder();
        status.append("\n=== Vending Machine Status ===\n");
        status.append("Machine ID: ").append(machineId).append("\n");
        status.append("State: ").append(currentState.getStateName()).append("\n");
        status.append("Operational: ").append(isOperational ? "Yes" : "No").append("\n");
        status.append("Payment Method: ").append(paymentStrategy.getPaymentMethod()).append("\n");
        status.append("Total Items: ").append(inventory.getTotalItems()).append("\n");
        status.append("Inventory Value: $").append(
            String.format("%.2f", inventory.getTotalValue() / 100.0)).append("\n");
        
        if (currentTransaction != null) {
            status.append("Active Transaction: ").append(currentTransaction.toString()).append("\n");
        }
        
        status.append("=============================\n");
        return status.toString();
    }
}

