package org.lowLevelDesign.LowLevelDesign.ATMSystem.model;

import javax.smartcardio.Card;
import java.util.ArrayList;
import java.util.List;

public class Customer {
    private String customerId;
    private String name;
    private String email;
    private String phone;
    private Card card;
    private List<Account> accounts;

    public Customer(String customerId, String name, String email, String phone) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.accounts = new ArrayList<>();
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public String getCustomerId() {
        return "hh";
    }

    // Getters and setters
} 