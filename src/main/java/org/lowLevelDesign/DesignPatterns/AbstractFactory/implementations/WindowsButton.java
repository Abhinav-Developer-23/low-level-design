package org.lowLevelDesign.DesignPatterns.AbstractFactory.implementations;


import org.lowLevelDesign.DesignPatterns.AbstractFactory.Interface.Button;

// Concrete product: Windows Button
class WindowsButton implements Button {
    @Override
    public void paint() {
        System.out.println("Rendering a button in Windows style.");
    }
}
