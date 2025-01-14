package org.lowLevelDesign.LowLevelDesign.ATMSystem.src.model;

import lombok.Getter;
import org.lowLevelDesign.LowLevelDesign.ATMSystem.src.ATM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bank {
    // Getters
    @Getter
    private String name;
    @Getter
    private String code;
    private Map<String, Customer> customers;
    private Map<String, Account> accounts;
    private List<ATM> atms;

    public Bank(String name, String code) {
        this.name = name;
        this.code = code;
        this.customers = new HashMap<>();
        this.accounts = new HashMap<>();
        this.atms = new ArrayList<>();
    }

    public boolean authenticateCard(Card card, String pin) {
        return card != null && card.validatePin(pin);
    }

    public void addCustomer(Customer customer) {
        customers.put(customer.getCustomerId(), customer);
    }

    public void addAccount(Account account) {
        accounts.put(account.getAccountNumber(), account);
    }

    public void addATM(ATM atm) {
        atms.add(atm);
    }

    public Account getAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public Customer getCustomer(String customerId) {
        return customers.get(customerId);
    }

}