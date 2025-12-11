package org.example.quickcommerce.model;

import java.util.UUID;

/**
 * Represents a delivery address for a user.
 */
public class Address {
    private final String addressId;
    private String houseNumber;
    private String street;
    private String city;
    private String state;
    private String pincode;
    private Location location;
    private boolean isDefault;

    public Address(String houseNumber, String street, String city, String state,
                   String pincode, Location location) {
        this.addressId = UUID.randomUUID().toString();
        this.houseNumber = houseNumber;
        this.street = street;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
        this.location = location;
        this.isDefault = false;
    }

    public String getAddressId() {
        return addressId;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getPincode() {
        return pincode;
    }

    public Location getLocation() {
        return location;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public String getFullAddress() {
        return String.format("%s, %s, %s, %s - %s",
                houseNumber, street, city, state, pincode);
    }

    @Override
    public String toString() {
        return "Address{addressId='" + addressId + "', fullAddress='" + getFullAddress() + "'}";
    }
}

