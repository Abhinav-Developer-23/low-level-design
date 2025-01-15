package org.lowLevelDesign.DesignPatterns;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

// Subject Interface (Observable)
interface WeatherSubject {
    void addObserver(WeatherObserver observer);
    void removeObserver(WeatherObserver observer);
    void notifyObservers();
}

// Concrete Subject (Weather Station)
class WeatherStation implements WeatherSubject {
    private final List<WeatherObserver> observers;
    private float temperature;
    private float humidity;

    public WeatherStation() {
        observers = new ArrayList<>();
    }

    public void setWeatherConditions(float temperature, float humidity) {
        this.temperature = temperature;
        this.humidity = humidity;
        notifyObservers();
    }

    @Override
    public void addObserver(WeatherObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(WeatherObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (WeatherObserver observer : observers) {
            observer.update(temperature, humidity);
        }
    }
}

// Observer Interface
interface WeatherObserver {
    void update(float temperature, float humidity);
}

// Concrete Observer 1 (MobileDevice)
class MobileDevice implements WeatherObserver {
    private String name;

    public MobileDevice(String name) {
        this.name = name;
    }

    @Override
    public void update(float temperature, float humidity) {
        System.out.println("[" + name + " - Mobile] received weather update -> Temperature: " + temperature + "°C, Humidity: " + humidity + "%");
    }
}

// Concrete Observer 2 (WindowsDesktop)
class WindowsDesktop implements WeatherObserver {
    @Override
    public void update(float temperature, float humidity) {
        System.out.println("[Windows Desktop] Weather update -> Temperature: " + temperature + "°C, Humidity: " + humidity + "%");
    }
}

// Concrete Observer 3 (MacBookLaptop)
class MacBookLaptop implements WeatherObserver {
    @Override
    public void update(float temperature, float humidity) {
        System.out.println("[MacBook Laptop] Weather update -> Temperature: " + temperature + "°C, Humidity: " + humidity + "%");
    }
}

/**
 * https://www.geeksforgeeks.org/observer-pattern-set-1-introduction/
 */

// Main Class (Client)
 public class ObserverPatternExample {
    public static void main(String[] args) {
        // Create a WeatherStation (Subject)
        WeatherStation weatherStation = new WeatherStation();
          final Logger logger = Logger.getAnonymousLogger();
        // Create observers
        MobileDevice phone = new MobileDevice("Phone");
        WindowsDesktop windowsDesktop = new WindowsDesktop();
        MacBookLaptop macBook = new MacBookLaptop();

        // Register observers with the subject
        weatherStation.addObserver(phone);
        weatherStation.addObserver(windowsDesktop);
        weatherStation.addObserver(macBook);
        logger.info("this is info message {} ");
        // Change weather conditions (this will notify all observers)
        weatherStation.setWeatherConditions(28.3f, 65.0f);
        weatherStation.setWeatherConditions(32.0f, 50.0f);

        // Remove an observer and update weather conditions again
        weatherStation.removeObserver(macBook);
        weatherStation.setWeatherConditions(20.0f, 75.0f);
    }
}
