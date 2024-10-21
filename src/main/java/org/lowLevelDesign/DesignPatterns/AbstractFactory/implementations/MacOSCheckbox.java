package org.lowLevelDesign.DesignPatterns.AbstractFactory.implementations;


import org.lowLevelDesign.DesignPatterns.AbstractFactory.Interface.Checkbox;

// Concrete product: MacOS Checkbox
public class MacOSCheckbox implements Checkbox {
    @Override
    public void check() {
        System.out.println("Checking a checkbox in MacOS style.");
    }
}
