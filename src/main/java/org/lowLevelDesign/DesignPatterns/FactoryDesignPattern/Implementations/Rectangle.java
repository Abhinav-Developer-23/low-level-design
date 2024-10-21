package org.lowLevelDesign.DesignPatterns.FactoryDesignPattern.Implementations;


import org.lowLevelDesign.DesignPatterns.FactoryDesignPattern.Interface.Shape;

public class Rectangle implements Shape {

    @Override
    public void draw() {
        System.out.println("Inside Rectangle::draw() method.");
    }
}
