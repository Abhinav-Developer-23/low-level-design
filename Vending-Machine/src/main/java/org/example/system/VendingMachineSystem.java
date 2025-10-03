package org.example.system;

import org.example.enums.CoinType;
import org.example.enums.MachineState;
import org.example.enums.PaymentMethod;
import org.example.enums.ProductType;
import org.example.interfaces.ProductSelectionStrategy;
import org.example.model.Product;
import org.example.observers.MaintenanceObserver;
import org.example.strategies.selection.NameBasedSelectionStrategy;

import java.util.Map;

/**
 * Vending Machine System: Facade class providing high-level API.
 * Simplifies interaction with the complex vending machine system.
 */
public class VendingMachineSystem {
    private final VendingMachineContext context;
    private final NameBasedSelectionStrategy nameBasedStrategy;

    public VendingMachineSystem() {
        this.context = new VendingMachineContext();
        this.nameBasedStrategy = new NameBasedSelectionStrategy();
    }

    /**
     * Displays all available products in the vending machine.
     */
    public void displayInventory() {
        System.out.println("\n=== VENDING MACHINE INVENTORY ===");
        Map<String, Product> available = context.getInventory().getAvailableProducts();

        if (available.isEmpty()) {
            System.out.println("No products available");
            return;
        }

        // Group by product type
        for (ProductType type : ProductType.values()) {
            boolean hasProducts = available.values().stream()
                    .anyMatch(p -> p.getProductType() == type);

            if (hasProducts) {
                System.out.println("\n" + type + ":");
                available.entrySet().stream()
                        .filter(entry -> entry.getValue().getProductType() == type)
                        .forEach(entry -> {
                            String slotId = entry.getKey();
                            Product product = entry.getValue();
                            int stock = context.getInventory().getStockLevel(slotId);
                            System.out.println(String.format("  %s: %s - %s (Stock: %d)",
                                                          slotId,
                                                          product.getName(),
                                                          product.getFormattedPrice(),
                                                          stock));
                        });
            }
        }
        System.out.println();
    }

    /**
     * Selects a product by slot ID (e.g., "A1", "B2").
     * @param slotId The slot identifier
     */
    public void selectProductBySlot(String slotId) {
        System.out.println("Selecting product from slot: " + slotId);
        context.selectProduct(slotId);
    }

    /**
     * Selects a product by name or type.
     * @param nameOrType Product name or type (e.g., "coke", "beverage")
     */
    public void selectProductByName(String nameOrType) {
        System.out.println("Searching for product: " + nameOrType);
        context.setSelectionStrategy(nameBasedStrategy);
        String slotId = nameBasedStrategy.selectProduct(context.getInventory(), nameOrType);

        if (slotId != null) {
            context.selectProduct(slotId);
        } else {
            System.out.println("Product not found: " + nameOrType);
            // Reset to default strategy
            context.setSelectionStrategy(new org.example.strategies.selection.BasicProductSelectionStrategy());
        }
    }

    /**
     * Inserts a coin into the vending machine.
     * @param coinType The type of coin to insert
     */
    public void insertCoin(CoinType coinType) {
        System.out.println("Inserting " + coinType.toString());
        context.insertCoin(coinType);
    }

    /**
     * Sets the payment method for the transaction.
     * @param paymentMethod The payment method to use
     */
    public void setPaymentMethod(PaymentMethod paymentMethod) {
        System.out.println("Setting payment method to: " + paymentMethod);
        context.setPaymentMethod(paymentMethod);
    }

    /**
     * Processes the payment for the selected product.
     * @return true if payment was successful
     */
    public boolean processPayment() {
        System.out.println("Processing payment...");
        return context.processPayment();
    }

    /**
     * Cancels the current transaction.
     */
    public void cancelTransaction() {
        System.out.println("Cancelling transaction...");
        context.cancelTransaction();
    }

    /**
     * Sets the machine to service/maintenance mode.
     * @param inService true to enter service mode, false to exit
     */
    public void setServiceMode(boolean inService) {
        String mode = inService ? "maintenance" : "normal operation";
        System.out.println("Setting machine to " + mode + " mode");
        context.setServiceMode(inService);
    }

    /**
     * Gets the current machine state.
     * @return Current machine state
     */
    public MachineState getCurrentState() {
        return context.getCurrentMachineState();
    }

    /**
     * Checks if the machine is currently out of service.
     * @return true if machine is in maintenance mode
     */
    public boolean isOutOfService() {
        return context.isOutOfService();
    }

    /**
     * Gets the current transaction details.
     * @return Current transaction, or null if no active transaction
     */
    public org.example.model.Transaction getCurrentTransaction() {
        return context.getCurrentTransaction();
    }

    /**
     * Gets a maintenance report from the maintenance observer.
     * @return Formatted maintenance report
     */
    public String getMaintenanceReport() {
        MaintenanceObserver maintenanceObserver = context.getObservers().stream()
                .filter(obs -> obs instanceof MaintenanceObserver)
                .map(obs -> (MaintenanceObserver) obs)
                .findFirst()
                .orElse(null);

        if (maintenanceObserver != null) {
            return maintenanceObserver.generateMaintenanceReport();
        }
        return "Maintenance observer not found";
    }

    /**
     * Restocks a product in the inventory.
     * @param slotId The slot to restock
     * @param quantity The quantity to add
     */
    public void restockProduct(String slotId, int quantity) {
        System.out.println("Restocking " + slotId + " with " + quantity + " items");
        context.getInventory().restockProduct(slotId, quantity);
    }

    /**
     * Shows help information about available commands.
     */
    public void showHelp() {
        System.out.println("\n=== VENDING MACHINE HELP ===");
        System.out.println("Available commands:");
        System.out.println("  displayInventory()     - Show all available products");
        System.out.println("  selectProductBySlot(id) - Select product by slot (e.g., 'A1')");
        System.out.println("  selectProductByName(name) - Select product by name (e.g., 'coke')");
        System.out.println("  insertCoin(CoinType)   - Insert a coin");
        System.out.println("  setPaymentMethod(method) - Set payment method");
        System.out.println("  processPayment()        - Process the payment");
        System.out.println("  cancelTransaction()     - Cancel current transaction");
        System.out.println("  getCurrentState()       - Get current machine state");
        System.out.println("  getMaintenanceReport()  - Get maintenance status");
        System.out.println();
        System.out.println("Coin types: PENNY, NICKEL, DIME, QUARTER, HALF_DOLLAR, DOLLAR");
        System.out.println("Payment methods: CASH, CARD, MOBILE");
        System.out.println();
    }

    /**
     * Performs a complete purchase flow demonstration.
     */
    public void demonstratePurchaseFlow() {
        System.out.println("\n=== PURCHASE FLOW DEMONSTRATION ===");

        // Display inventory
        displayInventory();

        // Select product
        System.out.println("Step 1: Selecting product...");
        selectProductBySlot("A1");

        // Insert coins
        System.out.println("Step 2: Inserting coins...");
        insertCoin(CoinType.QUARTER);
        insertCoin(CoinType.QUARTER);
        insertCoin(CoinType.QUARTER);
        insertCoin(CoinType.QUARTER);
        insertCoin(CoinType.QUARTER);

        // Process payment
        System.out.println("Step 3: Processing payment...");
        if (processPayment()) {
            System.out.println("Purchase completed successfully!");
        } else {
            System.out.println("Purchase failed!");
        }
    }
}
