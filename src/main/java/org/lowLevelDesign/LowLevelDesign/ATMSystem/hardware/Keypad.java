package org.lowLevelDesign.LowLevelDesign.ATMSystem.hardware;

import java.util.Scanner;

public class Keypad {
    private Scanner scanner;

    public Keypad() {
        scanner = new Scanner(System.in);
    }

    public String getInput() {
        return scanner.nextLine();
    }

    public int getNumericInput() {
        try {
            return Integer.parseInt(getInput());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public double getAmountInput() {
        try {
            return Double.parseDouble(getInput());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
} 