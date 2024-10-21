package org.lowLevelDesign.DesignPatterns.AbstractFactory.implementations;


import org.lowLevelDesign.DesignPatterns.AbstractFactory.Interface.Button;

// Concrete product: macOS Button
public class MacOSButton implements Button {
    @Override
    public void paint() {
        System.out.println("Rendering a button in MacOS style.");
    }
}

