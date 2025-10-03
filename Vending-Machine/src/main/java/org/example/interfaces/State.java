package org.example.interfaces;

import org.example.enums.Coin;

public interface State {
    void insertCoin(Coin coin);
    void selectItem(String itemCode);
    void dispense();
    void refund();
    void service(boolean inService); // true to put in service (out of service), false to resume
}

