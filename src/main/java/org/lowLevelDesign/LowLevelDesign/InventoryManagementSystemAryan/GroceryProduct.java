package org.lowLevelDesign.LowLevelDesign.InventoryManagementSystemAryan;

import java.util.Date;

public class GroceryProduct extends Product {
  // Grocery-specific attributes
  private final Date expiryDate;
  private final boolean refrigerated;
  private final String origin;
  private final boolean organic;
  private final String nutritionalInfo;

  private GroceryProduct(GroceryBuilder builder) {
    super(builder);
    this.expiryDate = builder.expiryDate;
    this.refrigerated = builder.refrigerated;
    this.origin = builder.origin;
    this.organic = builder.organic;
    this.nutritionalInfo = builder.nutritionalInfo;
  }

  // Getters for grocery-specific attributes
  public Date getExpiryDate() {
    return expiryDate;
  }
  
  public boolean isRefrigerated() {
    return refrigerated;
  }
  
  public String getOrigin() {
    return origin;
  }
  
  public boolean isOrganic() {
    return organic;
  }
  
  public String getNutritionalInfo() {
    return nutritionalInfo;
  }

  // Concrete Builder for GroceryProduct
  public static class GroceryBuilder extends Builder<GroceryBuilder> {
    // Required grocery parameters
    private final Date expiryDate;
    
    // Optional grocery parameters with default values
    private boolean refrigerated = false;
    private String origin = "Unknown";
    private boolean organic = false;
    private String nutritionalInfo = "";

    public GroceryBuilder(
        String sku, String name, double price, Date expiryDate) {
      super(sku, name, price, ProductCategory.GROCERY);
      this.expiryDate = expiryDate;
    }

    // Methods to set optional grocery-specific parameters
    public GroceryBuilder refrigerated(boolean refrigerated) {
      this.refrigerated = refrigerated;
      return this;
    }
    
    public GroceryBuilder origin(String origin) {
      this.origin = origin;
      return this;
    }
    
    public GroceryBuilder organic(boolean organic) {
      this.organic = organic;
      return this;
    }
    
    public GroceryBuilder nutritionalInfo(String nutritionalInfo) {
      this.nutritionalInfo = nutritionalInfo;
      return this;
    }

    @Override
    protected GroceryBuilder self() {
      return this;
    }

    @Override
    public GroceryProduct build() {
      return new GroceryProduct(this);
    }
  }
} 