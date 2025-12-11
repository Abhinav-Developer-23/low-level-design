package org.example.quickcommerce;

import org.example.quickcommerce.enums.PaymentMethod;
import org.example.quickcommerce.enums.ProductCategory;
import org.example.quickcommerce.model.*;

import java.util.List;

/**
 * Main class demonstrating the Quick Commerce application flow.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("       QUICK COMMERCE APPLICATION - DEMO");
        System.out.println("=".repeat(60));

        QuickCommerceService qcService = new QuickCommerceService();

        // ==================== SETUP PHASE ====================
        System.out.println("\n" + "=".repeat(60));
        System.out.println("PHASE 1: SYSTEM SETUP");
        System.out.println("=".repeat(60));

        // Create warehouses
        System.out.println("\n📦 Creating Warehouses...");
        Warehouse warehouse1 = qcService.createWarehouse("Mumbai Central Dark Store",
                new Location(19.0760, 72.8777));
        Warehouse warehouse2 = qcService.createWarehouse("Andheri Dark Store",
                new Location(19.1136, 72.8697));
        System.out.println("Created: " + warehouse1.getName());
        System.out.println("Created: " + warehouse2.getName());

        // Add products
        System.out.println("\n🛒 Adding Products...");
        Product milk = qcService.addProduct("Amul Toned Milk", 28.0, ProductCategory.DAIRY, "500ml");
        Product bread = qcService.addProduct("Britannia Bread", 45.0, ProductCategory.GROCERIES, "400g");
        Product apple = qcService.addProduct("Fresh Apples", 180.0, ProductCategory.FRUITS, "1kg");
        Product chips = qcService.addProduct("Lays Classic Chips", 20.0, ProductCategory.SNACKS, "52g");
        Product coke = qcService.addProduct("Coca Cola", 40.0, ProductCategory.BEVERAGES, "750ml");
        Product soap = qcService.addProduct("Dove Soap", 55.0, ProductCategory.PERSONAL_CARE, "100g");

        System.out.println("Added " + qcService.getAllProducts().size() + " products");

        // Add inventory
        System.out.println("\n📊 Stocking Inventory...");
        qcService.addInventory(warehouse1.getWarehouseId(), milk.getProductId(), 100);
        qcService.addInventory(warehouse1.getWarehouseId(), bread.getProductId(), 50);
        qcService.addInventory(warehouse1.getWarehouseId(), apple.getProductId(), 30);
        qcService.addInventory(warehouse1.getWarehouseId(), chips.getProductId(), 200);
        qcService.addInventory(warehouse1.getWarehouseId(), coke.getProductId(), 150);
        qcService.addInventory(warehouse1.getWarehouseId(), soap.getProductId(), 75);

        qcService.addInventory(warehouse2.getWarehouseId(), milk.getProductId(), 80);
        qcService.addInventory(warehouse2.getWarehouseId(), bread.getProductId(), 40);
        System.out.println("Inventory stocked in both warehouses");

        // Register delivery partners
        System.out.println("\n🚴 Registering Delivery Partners...");
        DeliveryPartner partner1 = qcService.registerDeliveryPartner("Rahul Kumar",
                "9876543210", new Location(19.0800, 72.8800));
        DeliveryPartner partner2 = qcService.registerDeliveryPartner("Amit Singh",
                "9876543211", new Location(19.1100, 72.8700));
        System.out.println("Registered: " + partner1.getName() + ", " + partner2.getName());

        // ==================== USER FLOW ====================
        System.out.println("\n" + "=".repeat(60));
        System.out.println("PHASE 2: USER REGISTRATION & LOGIN");
        System.out.println("=".repeat(60));

        System.out.println("\n👤 Registering User...");
        User user = qcService.registerUser("Priya Patel", "priya@email.com", "9988776655");
        System.out.println("User registered: " + user.getName());

        System.out.println("\n🔐 User logging in...");
        qcService.loginUser(user.getUserId());
        System.out.println("User logged in: " + qcService.getUser(user.getUserId()).isLoggedIn());

        System.out.println("\n📍 Adding Delivery Addresses...");
        Address homeAddress = new Address("A-101", "Sunshine Apartments, Dadar",
                "Mumbai", "Maharashtra", "400014", new Location(19.0178, 72.8478));
        Address officeAddress = new Address("5th Floor", "Tech Park, BKC",
                "Mumbai", "Maharashtra", "400051", new Location(19.0596, 72.8656));

        qcService.addUserAddress(user.getUserId(), homeAddress);
        qcService.addUserAddress(user.getUserId(), officeAddress);
        System.out.println("Added " + qcService.getUser(user.getUserId()).getAddresses().size() + " addresses");

        System.out.println("\n🏠 Selecting Home Address...");
        qcService.selectUserAddress(user.getUserId(), homeAddress.getAddressId());
        System.out.println("Selected: " + homeAddress.getFullAddress());

        // ==================== SHOPPING FLOW ====================
        System.out.println("\n" + "=".repeat(60));
        System.out.println("PHASE 3: BROWSING & SHOPPING");
        System.out.println("=".repeat(60));

        System.out.println("\n🔍 Browsing Products by Category (DAIRY)...");
        List<Product> dairyProducts = qcService.getProductsByCategory(ProductCategory.DAIRY);
        for (Product p : dairyProducts) {
            System.out.println("  - " + p.getName() + " @ Rs." + p.getPrice() + "/" + p.getUnit());
        }

        System.out.println("\n🛒 Adding Items to Cart...");
        qcService.addToCart(user.getUserId(), milk.getProductId(), 2);
        System.out.println("Added: 2x " + milk.getName());

        qcService.addToCart(user.getUserId(), bread.getProductId(), 1);
        System.out.println("Added: 1x " + bread.getName());

        qcService.addToCart(user.getUserId(), apple.getProductId(), 1);
        System.out.println("Added: 1x " + apple.getName());

        qcService.addToCart(user.getUserId(), chips.getProductId(), 3);
        System.out.println("Added: 3x " + chips.getName());

        System.out.println("\n📋 Cart Summary:");
        List<CartItem> cartItems = qcService.getCartItems(user.getUserId());
        for (CartItem item : cartItems) {
            System.out.println("  - " + item.getProduct().getName() +
                    " x" + item.getQuantity() +
                    " = Rs." + item.getTotalPrice());
        }
        System.out.println("  " + "-".repeat(30));
        System.out.println("  TOTAL: Rs." + qcService.getCartTotal(user.getUserId()));

        // ==================== ORDER FLOW ====================
        System.out.println("\n" + "=".repeat(60));
        System.out.println("PHASE 4: PLACING ORDER");
        System.out.println("=".repeat(60));

        System.out.println("\n💳 Placing Order with UPI Payment...");
        Order order = qcService.placeOrder(user.getUserId(), PaymentMethod.UPI);

        System.out.println("\n✅ Order Placed Successfully!");
        System.out.println("  Order ID: " + order.getOrderId().substring(0, 8) + "...");
        System.out.println("  Total: Rs." + order.getTotalPrice());
        System.out.println("  Status: " + order.getStatus());
        System.out.println("  Delivery Address: " + order.getDeliveryAddress().getFullAddress());

        System.out.println("\n🛒 Cart after order: " +
                (qcService.getCartItems(user.getUserId()).isEmpty() ? "Empty [OK]" : "Not Empty [FAIL]"));

        System.out.println("\n📊 Inventory Check (Warehouse 1):");
        System.out.println("  Milk: " + qcService.getInventory(warehouse1.getWarehouseId(), milk.getProductId()) + " units");
        System.out.println("  Bread: " + qcService.getInventory(warehouse1.getWarehouseId(), bread.getProductId()) + " units");

        // ==================== DELIVERY FLOW ====================
        System.out.println("\n" + "=".repeat(60));
        System.out.println("PHASE 5: ORDER DELIVERY");
        System.out.println("=".repeat(60));

        System.out.println("\n📦 Order Status Progression:");
        System.out.println("  Current: " + order.getStatus());

        System.out.println("\n  Advancing to PACKED...");
        qcService.advanceOrderStatus(order.getOrderId());
        System.out.println("  Status: " + qcService.getOrder(order.getOrderId()).getStatus());

        System.out.println("\n  Advancing to OUT_FOR_DELIVERY...");
        qcService.advanceOrderStatus(order.getOrderId());
        System.out.println("  Status: " + qcService.getOrder(order.getOrderId()).getStatus());

        System.out.println("\n  Marking as DELIVERED...");
        qcService.markOrderDelivered(order.getOrderId());
        System.out.println("  Status: " + qcService.getOrder(order.getOrderId()).getStatus());

        // ==================== ADDITIONAL DEMO ====================
        System.out.println("\n" + "=".repeat(60));
        System.out.println("PHASE 6: COD ORDER & CANCELLATION");
        System.out.println("=".repeat(60));

        System.out.println("\n💵 Placing COD Order...");
        qcService.addToCart(user.getUserId(), coke.getProductId(), 2);
        qcService.addToCart(user.getUserId(), soap.getProductId(), 1);
        Order codOrder = qcService.placeOrder(user.getUserId(), PaymentMethod.CASH_ON_DELIVERY);
        System.out.println("COD Order placed! Total: Rs." + codOrder.getTotalPrice());

        System.out.println("\n❌ Cancelling COD Order...");
        boolean cancelled = qcService.cancelOrder(codOrder.getOrderId());
        System.out.println("Order cancelled: " + cancelled);
        System.out.println("Order status: " + qcService.getOrder(codOrder.getOrderId()).getStatus());

        System.out.println("\n📜 User Order History:");
        List<Order> userOrders = qcService.getUserOrders(user.getUserId());
        for (Order o : userOrders) {
            System.out.println("  - Order " + o.getOrderId().substring(0, 8) + "... | " +
                    "Rs." + o.getTotalPrice() + " | " + o.getStatus());
        }

        System.out.println("\n🚴 Available Delivery Partners:");
        List<DeliveryPartner> availablePartners = qcService.getAvailableDeliveryPartners();
        for (DeliveryPartner dp : availablePartners) {
            System.out.println("  - " + dp.getName() + " (" + dp.getStatus() + ")");
        }

        System.out.println("\n" + "=".repeat(60));
        System.out.println("       DEMO COMPLETED SUCCESSFULLY!");
        System.out.println("=".repeat(60));
    }
}
