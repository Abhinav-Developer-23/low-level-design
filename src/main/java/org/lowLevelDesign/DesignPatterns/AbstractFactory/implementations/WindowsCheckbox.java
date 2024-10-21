package org.lowLevelDesign.DesignPatterns.AbstractFactory.implementations;


import org.lowLevelDesign.DesignPatterns.AbstractFactory.Interface.Checkbox;

// Concrete product: Windows Checkbox
class WindowsCheckbox implements Checkbox {
    @Override
    public void check() {
        System.out.println("Checking a checkbox in Windows style.");
    }
}

