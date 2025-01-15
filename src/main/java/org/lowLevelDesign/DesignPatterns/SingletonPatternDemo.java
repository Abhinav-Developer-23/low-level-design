package org.lowLevelDesign.DesignPatterns;

// Singleton Class

// for lazy loading see other examples
 class Singleton {
    // Private static instance of the same class, initialized to null
    private static Singleton instance = null;

    // Private constructor to prevent instantiation from other classes
    private Singleton() {
        System.out.println("Singleton Instance Created");
    }

    // Public static method to provide access to the instance
    public static synchronized Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton(); // Create instance if not already created
        }
        return instance;
    }

    // Example method for Singleton class
    public void showMessage() {
        System.out.println("Hello from the Singleton instance!");
    }
}

/**
 *  https://www.geeksforgeeks.org/singleton-design-pattern/
 */

// Example usage
 public class SingletonPatternDemo {
    public static void main(String[] args) {
        // Try to create the Singleton object
        Singleton singleton1 = Singleton.getInstance();
        singleton1.showMessage();

        // Try to get another instance (should return the same instance)
        Singleton singleton2 = Singleton.getInstance();
        singleton2.showMessage();

        // Checking if both references point to the same instance
        if (singleton1 == singleton2) {
            System.out.println("Both instances are the same.");
        }
    }
}
