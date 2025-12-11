package org.example.quickcommerce.service;

import org.example.quickcommerce.exception.NoDeliveryPartnerAvailableException;
import org.example.quickcommerce.model.DeliveryPartner;
import org.example.quickcommerce.model.Location;
import org.example.quickcommerce.repository.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Service for managing delivery partners.
 * Uses Repository singleton for data storage.
 */
public class DeliveryPartnerService {

    private ConcurrentHashMap<String, DeliveryPartner> getDeliveryPartnerDb() {
        return Repository.getInstance().getDeliveryPartnerDb();
    }

    public DeliveryPartner registerPartner(String name, String phone, Location location) {
        DeliveryPartner partner = new DeliveryPartner(name, phone, location);
        getDeliveryPartnerDb().put(partner.getPartnerId(), partner);
        return partner;
    }

    public Optional<DeliveryPartner> getPartner(String partnerId) {
        return Optional.ofNullable(getDeliveryPartnerDb().get(partnerId));
    }

    public DeliveryPartner getPartnerOrThrow(String partnerId) {
        return getPartner(partnerId)
                .orElseThrow(() -> new IllegalArgumentException("Partner not found: " + partnerId));
    }

    public List<DeliveryPartner> getAllPartners() {
        return new ArrayList<>(getDeliveryPartnerDb().values());
    }

    public List<DeliveryPartner> getAvailablePartners() {
        return getDeliveryPartnerDb().values().stream()
                .filter(DeliveryPartner::isAvailable)
                .collect(Collectors.toList());
    }

    public DeliveryPartner assignNearestPartner(String orderId, Location warehouseLocation) {
        DeliveryPartner nearestPartner = getDeliveryPartnerDb().values().stream()
                .filter(DeliveryPartner::isAvailable)
                .min(Comparator.comparingDouble(p -> p.distanceTo(warehouseLocation)))
                .orElseThrow(NoDeliveryPartnerAvailableException::new);

        nearestPartner.assignOrder(orderId);
        return nearestPartner;
    }

    public void completeDelivery(String partnerId) {
        getPartnerOrThrow(partnerId).completeDelivery();
    }

    public void setPartnerActive(String partnerId) {
        getPartnerOrThrow(partnerId).setActive();
    }

    public void setPartnerInactive(String partnerId) {
        getPartnerOrThrow(partnerId).setInactive();
    }

    public void updatePartnerLocation(String partnerId, Location newLocation) {
        getPartnerOrThrow(partnerId).setCurrentLocation(newLocation);
    }
}

