package org.lowLevelDesign.LowLevelDesign.ATMSystem.model;

import org.lowLevelDesign.LowLevelDesign.ATMSystem.enums.AccountType;

public class Account {
    private String accountNumber;
    private AccountType accountType;
    private double balance;
    private Customer customer;

    public Account(String accountNumber, AccountType accountType, Customer customer) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.customer = customer;
        this.balance = 0.0;
    }

    public boolean withdraw(double amount) {
        if (amount <= 0 || amount > balance) {
            return false;
        }
        balance -= amount;
        return true;
    }

    public boolean deposit(double amount) {
        if (amount <= 0) {
            return false;
        }
        balance += amount;
        return true;
    }

    public String getAccountNumber() {
        return this.accountNumber;
    }

    // Getters and setters
} 