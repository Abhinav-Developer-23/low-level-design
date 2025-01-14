package org.lowLevelDesign.LowLevelDesign.ATMSystem.transaction;

import org.lowLevelDesign.LowLevelDesign.ATMSystem.ATM;
import org.lowLevelDesign.LowLevelDesign.ATMSystem.enums.TransactionType;
import org.lowLevelDesign.LowLevelDesign.ATMSystem.model.Account;

public abstract class Transaction {
    protected ATM atm;
    protected Account account;
    protected TransactionType type;

    public Transaction(ATM atm, Account account, TransactionType type) {
        this.atm = atm;
        this.account = account;
        this.type = type;
    }

    public abstract boolean execute();
} 