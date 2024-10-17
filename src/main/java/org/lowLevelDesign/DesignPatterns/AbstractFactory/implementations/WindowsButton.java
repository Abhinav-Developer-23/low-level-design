package org.lowLevelDesign.DesignPatterns.AbstractFactory.implementations;

import src.LLD.DesignPatterns.AbstractFactory.Interface.Button;

// Concrete product: Windows Button
class WindowsButton implements Button {
    @Override
    public void paint() {
        System.out.println("Rendering a button in Windows style.");
    }
}
