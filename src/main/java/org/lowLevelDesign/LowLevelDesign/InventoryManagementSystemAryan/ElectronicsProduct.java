package org.lowLevelDesign.LowLevelDesign.InventoryManagementSystemAryan;

// Electronics Product with Builder
public class ElectronicsProduct extends Product {
  // Electronics-specific attributes
  private final String brand;
  private final int warrantyPeriod;
  private final String modelNumber;
  private final boolean wirelessConnectivity;
  private final int powerConsumption;

  private ElectronicsProduct(ElectronicsBuilder builder) {
    super(builder);
    this.brand = builder.brand;
    this.warrantyPeriod = builder.warrantyPeriod;
    this.modelNumber = builder.modelNumber;
    this.wirelessConnectivity = builder.wirelessConnectivity;
    this.powerConsumption = builder.powerConsumption;
  }

  // Getters for electronics-specific attributes
  public String getBrand() {
    return brand;
  }
  public int getWarrantyPeriod() {
    return warrantyPeriod;
  }
  public String getModelNumber() {
    return modelNumber;
  }
  public boolean hasWirelessConnectivity() {
    return wirelessConnectivity;
  }
  public int getPowerConsumption() {
    return powerConsumption;
  }

  // Concrete Builder for ElectronicsProduct
  public static class ElectronicsBuilder extends Builder<ElectronicsBuilder> {
    // Required electronics parameters
    private final String brand;

    // Optional electronics parameters with default values
    private int warrantyPeriod = 12; // 12 months default
    private String modelNumber = "";
    private boolean wirelessConnectivity = false;
    private int powerConsumption = 0;

    public ElectronicsBuilder(
        String sku, String name, double price, String brand) {
      super(sku, name, price, ProductCategory.ELECTRONICS);
      this.brand = brand;
    }

    // Methods to set optional electronics-specific parameters
    public ElectronicsBuilder warrantyPeriod(int warrantyPeriod) {
      this.warrantyPeriod = warrantyPeriod;
      return this;
    }

    public ElectronicsBuilder modelNumber(String modelNumber) {
      this.modelNumber = modelNumber;
      return this;
    }

    public ElectronicsBuilder wirelessConnectivity(
        boolean wirelessConnectivity) {
      this.wirelessConnectivity = wirelessConnectivity;
      return this;
    }

    public ElectronicsBuilder powerConsumption(int powerConsumption) {
      this.powerConsumption = powerConsumption;
      return this;
    }

    @Override
    protected ElectronicsBuilder self() {
      return this;
    }

    @Override
    public ElectronicsProduct build() {
      return new ElectronicsProduct(this);
    }
  }
} 