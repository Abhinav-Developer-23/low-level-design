package org.lowLevelDesign.LowLevelDesign.ATMSystem.src.transaction;

import org.lowLevelDesign.LowLevelDesign.ATMSystem.src.ATM;
import org.lowLevelDesign.LowLevelDesign.ATMSystem.src.enums.TransactionType;
import org.lowLevelDesign.LowLevelDesign.ATMSystem.src.model.Account;

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