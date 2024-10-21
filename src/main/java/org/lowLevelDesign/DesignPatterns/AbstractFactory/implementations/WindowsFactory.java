package org.lowLevelDesign.DesignPatterns.AbstractFactory.implementations;


import org.lowLevelDesign.DesignPatterns.AbstractFactory.Interface.Button;
import org.lowLevelDesign.DesignPatterns.AbstractFactory.Interface.Checkbox;
import org.lowLevelDesign.DesignPatterns.AbstractFactory.Interface.GUIFactory;

// Concrete factory for Windows
public class WindowsFactory implements GUIFactory {
    @Override
    public Button createButton() {
        return new WindowsButton();
    }

    @Override
    public Checkbox createCheckbox() {
        return new WindowsCheckbox();
    }
}