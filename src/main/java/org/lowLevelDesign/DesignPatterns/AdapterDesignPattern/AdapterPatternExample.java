package org.lowLevelDesign.DesignPatterns.AdapterDesignPattern;

// Step 1: Define the Target Interface (Charger)
// This is the expected interface for all types of chargers.
interface Charger {
    void chargeDevice(String deviceType);
}

// Step 2: Create Adaptee Classes (Existing Chargers)
// These classes have their own methods for charging devices, but they do not implement the Charger interface.

// Adaptee 1: Lenovo Charger
class LenovoCharger {
    public void plugInLenovoLaptop() {
        System.out.println("Charging Lenovo Laptop...");
    }
}

// Adaptee 2: Apple MacBook Charger
class MacBookCharger {
    public void connectToMacBook() {
        System.out.println("Charging MacBook...");
    }
}

// Adaptee 3: Phone Charger
class PhoneCharger {
    public void connectToPhone() {
        System.out.println("Charging Phone...");
    }
}

// Step 3: Create Adapter Classes
// These adapters implement the Charger interface and adapt the behavior of the specific chargers.

// Adapter for Lenovo Charger
class LenovoChargerAdapter implements Charger {
    private LenovoCharger lenovoCharger;

    public LenovoChargerAdapter(LenovoCharger lenovoCharger) {
        this.lenovoCharger = lenovoCharger;
    }

    @Override
    public void chargeDevice(String deviceType) {
        if (deviceType.equalsIgnoreCase("lenovo laptop")) {
            lenovoCharger.plugInLenovoLaptop();
        } else {
            System.out.println("Invalid device for Lenovo charger.");
        }
    }
}

// Adapter for MacBook Charger
class MacBookChargerAdapter implements Charger {
    private MacBookCharger macBookCharger;

    public MacBookChargerAdapter(MacBookCharger macBookCharger) {
        this.macBookCharger = macBookCharger;
    }

    @Override
    public void chargeDevice(String deviceType) {
        if (deviceType.equalsIgnoreCase("macbook")) {
            macBookCharger.connectToMacBook();
        } else {
            System.out.println("Invalid device for MacBook charger.");
        }
    }
}

// Adapter for Phone Charger
class PhoneChargerAdapter implements Charger {
    private PhoneCharger phoneCharger;

    public PhoneChargerAdapter(PhoneCharger phoneCharger) {
        this.phoneCharger = phoneCharger;
    }

    @Override
    public void chargeDevice(String deviceType) {
        if (deviceType.equalsIgnoreCase("phone")) {
            phoneCharger.connectToPhone();
        } else {
            System.out.println("Invalid device for phone charger.");
        }
    }
}

// Step 4: Create the Universal Charging Station (Client)
// The charging station can use any charger as long as it conforms to the Charger interface.
class ChargingStation {
    private Charger charger;

    public ChargingStation(Charger charger) {
        this.charger = charger;
    }

    public void charge(String deviceType) {
        charger.chargeDevice(deviceType);
    }

    public void setCharger(Charger charger) {
        this.charger = charger;
    }
}

// Step 5: Demonstrate the Adapter Pattern in the Main Class
public class AdapterPatternExample {
    public static void main(String[] args) {
        // Create adapters for each type of charger
        LenovoCharger lenovoCharger = new LenovoCharger();
        MacBookCharger macBookCharger = new MacBookCharger();
        PhoneCharger phoneCharger = new PhoneCharger();

        // Use Lenovo Charger with an adapter
        Charger lenovoAdapter = new LenovoChargerAdapter(lenovoCharger);
        ChargingStation station = new ChargingStation(lenovoAdapter);
        station.charge("lenovo laptop");  // Charging Lenovo Laptop

        // Switch to MacBook Charger
        Charger macBookAdapter = new MacBookChargerAdapter(macBookCharger);
        station.setCharger(macBookAdapter);
        station.charge("macbook");  // Charging MacBook

        // Switch to Phone Charger
        Charger phoneAdapter = new PhoneChargerAdapter(phoneCharger);
        station.setCharger(phoneAdapter);
        station.charge("phone");  // Charging Phone

        // Attempt to charge an invalid device
        station.charge("tablet");  // Invalid device for phone charger
    }
}
