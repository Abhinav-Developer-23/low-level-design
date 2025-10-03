package org.example;

import org.example.enums.Coin;
import org.example.model.Item;
import org.example.system.VendingMachine;

public class Main {
    public static void main(String[] args) {
        VendingMachine vm = new VendingMachine();
        vm.addItem(new Item("A1", "Coke", 125), 5);  // $1.25
        vm.addItem(new Item("B2", "Chips", 100), 2); // $1.00
        vm.addItem(new Item("C3", "Snickers", 65), 0); // out of stock

        vm.printStatus();

        // Normal purchase
        vm.insertCoin(Coin.QUARTER);
        vm.insertCoin(Coin.QUARTER);
        vm.insertCoin(Coin.QUARTER);
        vm.insertCoin(Coin.QUARTER);
        vm.insertCoin(Coin.NICKEL); // total $1.05
        vm.selectItem("A1"); // needs $1.25 -> insufficient
        vm.insertCoin(Coin.DIME);
        vm.insertCoin(Coin.DIME); // now $1.25
        vm.selectItem("A1"); // will dispense

        vm.printStatus();

        // Try selecting out of stock
        vm.insertCoin(Coin.QUARTER);
        vm.insertCoin(Coin.QUARTER);
        vm.selectItem("C3"); // out of stock
        vm.refund();

        // Put machine out of service
        vm.service(true);
        vm.insertCoin(Coin.QUARTER); // rejected
        vm.service(false); // back to service
        vm.printStatus();
    }
}

