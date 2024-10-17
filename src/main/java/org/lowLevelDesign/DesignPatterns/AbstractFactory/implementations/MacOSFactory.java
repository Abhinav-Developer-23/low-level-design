package org.lowLevelDesign.DesignPatterns.AbstractFactory.implementations;

import src.LLD.DesignPatterns.AbstractFactory.Interface.Button;
import src.LLD.DesignPatterns.AbstractFactory.Interface.Checkbox;
import src.LLD.DesignPatterns.AbstractFactory.Interface.GUIFactory;

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
