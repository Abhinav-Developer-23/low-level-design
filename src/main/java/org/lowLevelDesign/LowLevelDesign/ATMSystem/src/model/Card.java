package org.lowLevelDesign.LowLevelDesign.ATMSystem.src.model;

public class Card {
    private String cardNumber;
    private String pin;
    private String expiryDate;
    private int cvv;
    private boolean active;

    public Card(String cardNumber, String pin, String expiryDate, int cvv) {
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
        this.active = true;
    }

    public boolean validatePin(String pin) {
        return this.pin.equals(pin);
    }

    public boolean isActive() {
        return true;
    }

    // Getters and setters
} 