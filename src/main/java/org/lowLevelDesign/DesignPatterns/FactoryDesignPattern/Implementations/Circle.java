package org.lowLevelDesign.DesignPatterns.FactoryDesignPattern.Implementations;

import src.LLD.DesignPatterns.FactoryDesignPattern.Interface.Shape;

public class Circle implements Shape {

    @Override
    public void draw() {
        System.out.println("Inside Circle::draw() method.");
    }
}