package org.lowLevelDesign.LowLevelDesign.InventoryManagementSystemAryan;

public class FurnitureProduct extends Product {
  // Furniture-specific attributes
  private final String material;
  private final String dimensions;
  private final int warrantyPeriod;
  private final String brand;
  private final boolean requiresAssembly;

  private FurnitureProduct(FurnitureBuilder builder) {
    super(builder);
    this.material = builder.material;
    this.dimensions = builder.dimensions;
    this.warrantyPeriod = builder.warrantyPeriod;
    this.brand = builder.brand;
    this.requiresAssembly = builder.requiresAssembly;
  }

  // Getters for furniture-specific attributes
  public String getMaterial() {
    return material;
  }
  
  public String getDimensions() {
    return dimensions;
  }
  
  public int getWarrantyPeriod() {
    return warrantyPeriod;
  }
  
  public String getBrand() {
    return brand;
  }
  
  public boolean requiresAssembly() {
    return requiresAssembly;
  }

  // Concrete Builder for FurnitureProduct
  public static class FurnitureBuilder extends Builder<FurnitureBuilder> {
    // Required furniture parameters
    private final String material;
    private final String dimensions;
    
    // Optional furniture parameters with default values
    private int warrantyPeriod = 12; // 12 months default warranty
    private String brand = "Generic";
    private boolean requiresAssembly = true;

    public FurnitureBuilder(
        String sku, String name, double price, String material, String dimensions) {
      super(sku, name, price, ProductCategory.FURNITURE);
      this.material = material;
      this.dimensions = dimensions;
    }

    // Methods to set optional furniture-specific parameters
    public FurnitureBuilder warrantyPeriod(int warrantyPeriod) {
      this.warrantyPeriod = warrantyPeriod;
      return this;
    }
    
    public FurnitureBuilder brand(String brand) {
      this.brand = brand;
      return this;
    }
    
    public FurnitureBuilder requiresAssembly(boolean requiresAssembly) {
      this.requiresAssembly = requiresAssembly;
      return this;
    }

    @Override
    protected FurnitureBuilder self() {
      return this;
    }

    @Override
    public FurnitureProduct build() {
      return new FurnitureProduct(this);
    }
  }
} 