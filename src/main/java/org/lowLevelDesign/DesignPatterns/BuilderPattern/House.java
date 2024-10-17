package org.lowLevelDesign.DesignPatterns.BuilderPattern;

// The Product class (House) that we want to create using the Builder Pattern.
public class House {
    private String foundation;
    private String structure;
    private String roof;
    private int rooms;
    private boolean hasGarden;
    private boolean hasSwimmingPool;

    // Private constructor to force object creation via the Builder

    private House()
    {

    }
    private House(HouseBuilder builder) {
        this.foundation = builder.foundation;
        this.structure = builder.structure;
        this.roof = builder.roof;
        this.rooms = builder.rooms;
        this.hasGarden = builder.hasGarden;
        this.hasSwimmingPool = builder.hasSwimmingPool;
    }

    // Display house properties
    public void showHouseDetails() {
        System.out.println("House with foundation: " + foundation);
        System.out.println("Structure: " + structure);
        System.out.println("Roof: " + roof);
        System.out.println("Rooms: " + rooms);
        System.out.println("Garden: " + (hasGarden ? "Yes" : "No"));
        System.out.println("Swimming Pool: " + (hasSwimmingPool ? "Yes" : "No"));
    }

    // Static nested class - the Builder
    public static class HouseBuilder {
        private String foundation;
        private String structure;
        private String roof;
        private int rooms;
        private boolean hasGarden;
        private boolean hasSwimmingPool;

        // Methods for setting various parameters, returning 'this' for chaining


        // Mandatory parameter: foundation
        public HouseBuilder(String foundation) {
            this.foundation = foundation;
        }

        public HouseBuilder setFoundation(String foundation) {
            this.foundation = foundation;
            return this;
        }

        public HouseBuilder setStructure(String structure) {
            this.structure = structure;
            return this;
        }

        public HouseBuilder setRoof(String roof) {
            this.roof = roof;
            return this;
        }

        public HouseBuilder setRooms(int rooms) {
            this.rooms = rooms;
            return this;
        }

        public HouseBuilder setGarden(boolean hasGarden) {
            this.hasGarden = hasGarden;
            return this;
        }

        public HouseBuilder setSwimmingPool(boolean hasSwimmingPool) {
            this.hasSwimmingPool = hasSwimmingPool;
            return this;
        }

        // Build method that creates the House object
        public House build() {
            return new House(this);
        }
    }


}
