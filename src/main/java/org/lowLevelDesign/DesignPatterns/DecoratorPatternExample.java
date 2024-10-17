package org.lowLevelDesign.DesignPatterns;

// The Component Interface (defines the common
// interface for both concrete components and decorators)
interface Coffee {
    String getDescription();
    double getCost();
}

// The Concrete Component (basic coffee type without any condiments)
class SimpleCoffee implements Coffee {

    @Override
    public String getDescription() {
        return "Simple Coffee";
    }

    @Override
    public double getCost() {
        return 5.0;  // base price for a simple coffee
    }
}

// The Decorator Abstract Class (implements the Coffee interface
// and holds a reference to a Coffee object)
class CoffeeDecorator implements Coffee {
    protected Coffee decoratedCoffee;

    // Constructor to wrap an existing coffee object (for dynamic decoration)
    public CoffeeDecorator(Coffee coffee) {
        this.decoratedCoffee = coffee;
    }

    @Override
    public String getDescription() {
        return decoratedCoffee.getDescription();
    }

    @Override
    public double getCost() {
        return decoratedCoffee.getCost();
    }
}

// Concrete Decorator 1 (adds Milk to the coffee)
class MilkDecorator extends CoffeeDecorator {

    public MilkDecorator(Coffee coffee) {
        super(coffee);
    }

    @Override
    public String getDescription() {
        return decoratedCoffee.getDescription() + ", Milk";
    }

    @Override
    public double getCost() {
        return decoratedCoffee.getCost() + 1.5;  // extra cost for milk
    }
}

// Concrete Decorator 2 (adds Sugar to the coffee)
class SugarDecorator extends CoffeeDecorator {

    public SugarDecorator(Coffee coffee) {
        super(coffee);
    }

    @Override
    public String getDescription() {
        return decoratedCoffee.getDescription() + ", Sugar";
    }

    @Override
    public double getCost() {
        return decoratedCoffee.getCost() + 0.5;  // extra cost for sugar
    }
}

// Concrete Decorator 3 (adds Whipped Cream to the coffee)
class WhippedCreamDecorator extends CoffeeDecorator {

    public WhippedCreamDecorator(Coffee coffee) {
        super(coffee);
    }

    @Override
    public String getDescription() {
        return decoratedCoffee.getDescription() + ", Whipped Cream";
    }

    @Override
    public double getCost() {
        return decoratedCoffee.getCost() + 2.0;  // extra cost for whipped cream
    }
}

// Main class to demonstrate the Decorator Pattern
public class DecoratorPatternExample {

    public static void main(String[] args) {
        // Step 1: Start with a simple coffee
        Coffee myCoffee = new SimpleCoffee();
        System.out.println(myCoffee.getDescription() + " -> Cost: $" + myCoffee.getCost());

        // Step 2: Add Milk to the coffee
        myCoffee = new MilkDecorator(myCoffee);
        System.out.println(myCoffee.getDescription() + " -> Cost: $" + myCoffee.getCost());

        // Step 3: Add Sugar to the coffee
        myCoffee = new SugarDecorator(myCoffee);
        System.out.println(myCoffee.getDescription() + " -> Cost: $" + myCoffee.getCost());

        // Step 4: Add Whipped Cream to the coffee
        myCoffee = new WhippedCreamDecorator(myCoffee);
        System.out.println(myCoffee.getDescription() + " -> Cost: $" + myCoffee.getCost());
    }
}
