package org.lowLevelDesign.DesignPatterns.AbstractFactory;

import src.LLD.DesignPatterns.AbstractFactory.Interface.Button;
import src.LLD.DesignPatterns.AbstractFactory.Interface.Checkbox;
import src.LLD.DesignPatterns.AbstractFactory.Interface.GUIFactory;
import src.LLD.DesignPatterns.AbstractFactory.implementations.MacOSFactory;
import src.LLD.DesignPatterns.AbstractFactory.implementations.WindowsFactory;

// Client code
public class Application {
    private Button button;
    private Checkbox checkbox;

    public Application(GUIFactory factory) {
        button = factory.createButton();
        checkbox = factory.createCheckbox();
    }

    public void render() {
        button.paint();
        checkbox.check();
    }

    public static void main(String[] args) {
        GUIFactory factory;

        // Configure the factory based on some logic (here based on OS type)
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            factory = new WindowsFactory();
        } else {
            factory = new MacOSFactory();
        }

        Application app = new Application(factory);
        app.render();
    }
}

