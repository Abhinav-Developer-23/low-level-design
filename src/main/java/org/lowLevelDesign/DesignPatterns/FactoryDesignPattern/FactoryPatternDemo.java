package org.lowLevelDesign.DesignPatterns.FactoryDesignPattern;


import org.lowLevelDesign.DesignPatterns.FactoryDesignPattern.Implementations.ShapeFactory;
import org.lowLevelDesign.DesignPatterns.FactoryDesignPattern.Interface.Shape;

public class FactoryPatternDemo {

    /**
     * https://www.digitalocean.com/community/tutorials/factory-design-pattern-in-java
     **/



    public static void main(String[] args) {
        ShapeFactory shapeFactory = new ShapeFactory();

        //get an object of Circle and call its draw method.
        Shape shape1 = shapeFactory.getShape("CIRCLE");

        //call draw method of Circle
        shape1.draw();

        //get an object of Rectangle and call its draw method.
        Shape shape2 = shapeFactory.getShape("RECTANGLE");

        //call draw method of Rectangle
        shape2.draw();

        //get an object of Square and call its draw method.
        Shape shape3 = shapeFactory.getShape("SQUARE");

        //call draw method of square
        shape3.draw();
    }
}