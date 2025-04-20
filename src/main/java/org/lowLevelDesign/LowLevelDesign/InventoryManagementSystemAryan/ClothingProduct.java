package org.lowLevelDesign.LowLevelDesign.InventoryManagementSystemAryan;

public class ClothingProduct extends Product {
  // Clothing-specific attributes
  private final String size;
  private final String color;
  private final String material;
  private final String brand;
  private final String season;

  private ClothingProduct(ClothingBuilder builder) {
    super(builder);
    this.size = builder.size;
    this.color = builder.color;
    this.material = builder.material;
    this.brand = builder.brand;
    this.season = builder.season;
  }

  // Getters for clothing-specific attributes
  public String getSize() {
    return size;
  }
  
  public String getColor() {
    return color;
  }
  
  public String getMaterial() {
    return material;
  }
  
  public String getBrand() {
    return brand;
  }
  
  public String getSeason() {
    return season;
  }

  // Concrete Builder for ClothingProduct
  public static class ClothingBuilder extends Builder<ClothingBuilder> {
    // Required clothing parameters
    private final String size;
    private final String color;
    
    // Optional clothing parameters with default values
    private String material = "Cotton";
    private String brand = "Generic";
    private String season = "All season";

    public ClothingBuilder(
        String sku, String name, double price, String size, String color) {
      super(sku, name, price, ProductCategory.CLOTHING);
      this.size = size;
      this.color = color;
    }

    // Methods to set optional clothing-specific parameters
    public ClothingBuilder material(String material) {
      this.material = material;
      return this;
    }

    public ClothingBuilder brand(String brand) {
      this.brand = brand;
      return this;
    }

    public ClothingBuilder season(String season) {
      this.season = season;
      return this;
    }

    @Override
    protected ClothingBuilder self() {
      return this;
    }

    @Override
    public ClothingProduct build() {
      return new ClothingProduct(this);
    }
  }
} 