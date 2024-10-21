package org.lowLevelDesign.DesignPatterns.FactoryDesignPattern.Implementations;

import org.lowLevelDesign.DesignPatterns.FactoryDesignPattern.Interface.Shape;

;

public class Circle implements Shape {

    @Override
    public void draw() {
        System.out.println("Inside Circle::draw() method.");
    }
}