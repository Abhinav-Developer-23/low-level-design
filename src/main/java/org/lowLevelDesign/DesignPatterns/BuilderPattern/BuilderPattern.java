package org.lowLevelDesign.DesignPatterns.BuilderPattern;

public class BuilderPattern {
    public static void main(String[] args) {
        // Step-by-step construction of a House using the Builder
        House house = new House.HouseBuilder("Concrete Foundation") // Mandatory field
                .setStructure("Brick Walls")           // Optional fields
                .setRoof("Concrete Roof")
                .setRooms(4)
                .setGarden(true)
                .setSwimmingPool(false)
                .build();

        // Display the details of the built house
        house.showHouseDetails();

        // Example of building a different house configuration
        House luxuryHouse = new House.HouseBuilder("Steel Foundation")
                .setStructure("Glass and Steel Structure")
                .setRoof("Luxury Glass Roof")
                .setRooms(10)
                .setGarden(true)
                .setSwimmingPool(true)
                .build();

        // Display the details of the luxury house
        luxuryHouse.showHouseDetails();
    }
}
