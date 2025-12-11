package org.example.quickcommerce.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a user in the quick commerce system.
 */
public class User {
    private final String userId;
    private String name;
    private String email;
    private String phone;
    private final List<Address> addresses;
    private Address selectedAddress;
    private boolean isLoggedIn;

    public User(String name, String email, String phone) {
        this.userId = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.addresses = new ArrayList<>();
        this.isLoggedIn = false;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public List<Address> getAddresses() {
        return new ArrayList<>(addresses);
    }

    public Address getSelectedAddress() {
        return selectedAddress;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void addAddress(Address address) {
        if (addresses.isEmpty()) {
            address.setDefault(true);
            this.selectedAddress = address;
        }
        addresses.add(address);
    }

    public void removeAddress(String addressId) {
        addresses.removeIf(addr -> addr.getAddressId().equals(addressId));
        if (selectedAddress != null && selectedAddress.getAddressId().equals(addressId)) {
            selectedAddress = addresses.isEmpty() ? null : addresses.get(0);
        }
    }

    public void selectAddress(String addressId) {
        for (Address address : addresses) {
            if (address.getAddressId().equals(addressId)) {
                this.selectedAddress = address;
                return;
            }
        }
        throw new IllegalArgumentException("Address not found: " + addressId);
    }

    public void login() {
        this.isLoggedIn = true;
    }

    public void logout() {
        this.isLoggedIn = false;
    }

    @Override
    public String toString() {
        return "User{userId='" + userId + "', name='" + name + "', isLoggedIn=" + isLoggedIn + "}";
    }
}

