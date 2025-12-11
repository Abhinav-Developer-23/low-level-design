package org.example.quickcommerce.repository;

import org.example.quickcommerce.model.*;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Singleton Repository class that holds all data stores.
 * Uses ConcurrentHashMap for thread-safe operations.
 */
public class Repository {

    private static volatile Repository instance;

    // Data stores using ConcurrentHashMap
    private final ConcurrentHashMap<String, User> userDb;
    private final ConcurrentHashMap<String, Product> productDb;
    private final ConcurrentHashMap<String, Warehouse> warehouseDb;
    private final ConcurrentHashMap<String, Cart> cartDb;              // userId -> Cart
    private final ConcurrentHashMap<String, Order> orderDb;
    private final ConcurrentHashMap<String, Payment> paymentDb;        // orderId -> Payment
    private final ConcurrentHashMap<String, DeliveryPartner> deliveryPartnerDb;
    private final ConcurrentHashMap<String, List<String>> userOrdersDb; // userId -> List<orderId>

    private Repository() {
        this.userDb = new ConcurrentHashMap<>();
        this.productDb = new ConcurrentHashMap<>();
        this.warehouseDb = new ConcurrentHashMap<>();
        this.cartDb = new ConcurrentHashMap<>();
        this.orderDb = new ConcurrentHashMap<>();
        this.paymentDb = new ConcurrentHashMap<>();
        this.deliveryPartnerDb = new ConcurrentHashMap<>();
        this.userOrdersDb = new ConcurrentHashMap<>();
    }

    public static Repository getInstance() {
        if (instance == null) {
            synchronized (Repository.class) {
                if (instance == null) {
                    instance = new Repository();
                }
            }
        }
        return instance;
    }

    // ==================== Getters for all DBs ====================

    public ConcurrentHashMap<String, User> getUserDb() {
        return userDb;
    }

    public ConcurrentHashMap<String, Product> getProductDb() {
        return productDb;
    }

    public ConcurrentHashMap<String, Warehouse> getWarehouseDb() {
        return warehouseDb;
    }

    public ConcurrentHashMap<String, Cart> getCartDb() {
        return cartDb;
    }

    public ConcurrentHashMap<String, Order> getOrderDb() {
        return orderDb;
    }

    public ConcurrentHashMap<String, Payment> getPaymentDb() {
        return paymentDb;
    }

    public ConcurrentHashMap<String, DeliveryPartner> getDeliveryPartnerDb() {
        return deliveryPartnerDb;
    }

    public ConcurrentHashMap<String, List<String>> getUserOrdersDb() {
        return userOrdersDb;
    }

    // ==================== Utility method to clear all data ====================

    public void clearAll() {
        userDb.clear();
        productDb.clear();
        warehouseDb.clear();
        cartDb.clear();
        orderDb.clear();
        paymentDb.clear();
        deliveryPartnerDb.clear();
        userOrdersDb.clear();
    }
}

