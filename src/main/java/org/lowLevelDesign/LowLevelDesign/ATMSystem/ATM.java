package org.lowLevelDesign.LowLevelDesign.ATMSystem;

import lombok.Getter;
import org.lowLevelDesign.LowLevelDesign.ATMSystem.hardware.*;
import org.lowLevelDesign.LowLevelDesign.ATMSystem.model.Bank;
import org.lowLevelDesign.LowLevelDesign.ATMSystem.model.Card;
import org.lowLevelDesign.LowLevelDesign.ATMSystem.transaction.Transaction;

public class ATM {
    private String atmId;
    private String location;
    // Getters for components
    @Getter
    private CashDispenser cashDispenser;
    @Getter
    private CardReader cardReader;
    private Screen screen;
    private Keypad keypad;
    private DepositSlot depositSlot;
    private Printer printer;
    private Bank bank;
    private boolean isOn;

    public ATM(String atmId, String location, Bank bank) {
        this.atmId = atmId;
        this.location = location;
        this.bank = bank;
        this.cashDispenser = new CashDispenser();
        this.cardReader = new CardReader();
        this.screen = new Screen();
        this.keypad = new Keypad();
        this.depositSlot = new DepositSlot();
        this.printer = new Printer();
        this.isOn = false;
    }

    public boolean authenticateUser(String pin) {
        Card card = cardReader.getCurrentCard();
        if (card != null) {
            return card.validatePin(pin);
        }
        return false;
    }

    public boolean performTransaction(Transaction transaction) {
        if (!isOn || cardReader.getCurrentCard() == null) {
            return false;
        }
        return transaction.execute();
    }

    // Other getters and operational methods
} 