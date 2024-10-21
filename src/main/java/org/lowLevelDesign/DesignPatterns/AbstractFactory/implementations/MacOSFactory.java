package org.lowLevelDesign.DesignPatterns.AbstractFactory.implementations;


import org.lowLevelDesign.DesignPatterns.AbstractFactory.Interface.Button;
import org.lowLevelDesign.DesignPatterns.AbstractFactory.Interface.Checkbox;
import org.lowLevelDesign.DesignPatterns.AbstractFactory.Interface.GUIFactory;

// Concrete factory for macOS
public class MacOSFactory implements GUIFactory {
    @Override
    public Button createButton() {
        return new MacOSButton();
    }

    @Override
    public Checkbox createCheckbox() {
        return new MacOSCheckbox();
    }
}
