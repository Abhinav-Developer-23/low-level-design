package org.lowLevelDesign.DesignPatterns.AbstractFactory.Interface;

// Abstract factory interface
public interface GUIFactory {
    Button createButton();
    Checkbox createCheckbox();
}
