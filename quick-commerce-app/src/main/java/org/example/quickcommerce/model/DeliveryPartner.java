package org.example.quickcommerce.model;

import org.example.quickcommerce.enums.DeliveryPartnerStatus;

import java.util.UUID;

/**
 * Represents a delivery partner who delivers orders.
 */
public class DeliveryPartner {
    private final String partnerId;
    private String name;
    private String phone;
    private Location currentLocation;
    private DeliveryPartnerStatus status;
    private String currentOrderId;

    public DeliveryPartner(String name, String phone, Location currentLocation) {
        this.partnerId = UUID.randomUUID().toString();
        this.name = name;
        this.phone = phone;
        this.currentLocation = currentLocation;
        this.status = DeliveryPartnerStatus.ACTIVE;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public DeliveryPartnerStatus getStatus() {
        return status;
    }

    public String getCurrentOrderId() {
        return currentOrderId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public void setActive() {
        this.status = DeliveryPartnerStatus.ACTIVE;
        this.currentOrderId = null;
    }

    public void setInactive() {
        this.status = DeliveryPartnerStatus.INACTIVE;
    }

    public void assignOrder(String orderId) {
        this.status = DeliveryPartnerStatus.ON_DELIVERY;
        this.currentOrderId = orderId;
    }

    public void completeDelivery() {
        this.status = DeliveryPartnerStatus.ACTIVE;
        this.currentOrderId = null;
    }

    public boolean isAvailable() {
        return status == DeliveryPartnerStatus.ACTIVE;
    }

    public double distanceTo(Location location) {
        return currentLocation.distanceTo(location);
    }

    @Override
    public String toString() {
        return "DeliveryPartner{partnerId='" + partnerId + "', name='" + name + "', status=" + status + "}";
    }
}

