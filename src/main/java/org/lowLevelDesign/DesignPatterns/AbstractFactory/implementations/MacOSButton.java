package org.lowLevelDesign.DesignPatterns.AbstractFactory.implementations;


import src.LLD.DesignPatterns.AbstractFactory.Interface.Button;

// Concrete product: macOS Button
public class MacOSButton implements Button {
    @Override
    public void paint() {
        System.out.println("Rendering a button in MacOS style.");
    }
}

