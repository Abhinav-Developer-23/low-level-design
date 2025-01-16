package org.lowLevelDesign.DesignPatterns;// Step 1: Define the Subsystem Classes
// These classes handle the specific operations but are complex to use individually.

// Subsystem Class 1: Television
class Television {
    public void on() {
        System.out.println("TV is turned ON.");
    }

    public void setInputChannel(String channel) {
        System.out.println("TV is set to " + channel + " input.");
    }

    public void off() {
        System.out.println("TV is turned OFF.");
    }
}

// Subsystem Class 2: Sound System
class SoundSystem {
    public void on() {
        System.out.println("Sound System is turned ON.");
    }

    public void setVolume(int level) {
        System.out.println("Sound System volume set to " + level + ".");
    }

    public void off() {
        System.out.println("Sound System is turned OFF.");
    }
}

// Subsystem Class 3: DVD Player
class DVDPlayer {
    public void on() {
        System.out.println("DVD Player is turned ON.");
    }

    public void play(String movie) {
        System.out.println("Playing movie: " + movie);
    }

    public void stop() {
        System.out.println("DVD Player stopped playing.");
    }

    public void off() {
        System.out.println("DVD Player is turned OFF.");
    }
}

// Step 2: Create the Facade Class
// This class simplifies the interaction with the subsystem classes.

class HomeTheaterFacade {
    private Television tv;
    private SoundSystem soundSystem;
    private DVDPlayer dvdPlayer;

    // Constructor initializing the subsystem components
    public HomeTheaterFacade(Television tv, SoundSystem soundSystem, DVDPlayer dvdPlayer) {
        this.tv = tv;
        this.soundSystem = soundSystem;
        this.dvdPlayer = dvdPlayer;
    }

    // Simplified method to watch a movie
    public void watchMovie(String movie) {
        System.out.println("Setting up the home theater to watch a movie...");

        tv.on();
        tv.setInputChannel("HDMI1");

        soundSystem.on();
        soundSystem.setVolume(15);

        dvdPlayer.on();
        dvdPlayer.play(movie);

        System.out.println("Movie is ready to watch!");
    }

    // Simplified method to end the movie
    public void endMovie() {
        System.out.println("Shutting down the home theater...");

        dvdPlayer.stop();
        dvdPlayer.off();

        soundSystem.off();
        tv.off();

        System.out.println("Home theater is turned off.");
    }
}


/**
 * The Facade Design Pattern is a structural design pattern that provides a simplified interface to a complex subsystem.
 * It acts as a "front-facing" interface that hides the complexities of the underlying system, making it easier for clients
 * to interact with it.
 * https://www.geeksforgeeks.org/facade-design-pattern-introduction/
 */

// Step 3: Demonstrate the Facade Pattern in Action
public class FacadePatternExample {
    public static void main(String[] args) {
        // Creating the subsystem objects
        Television tv = new Television();
        SoundSystem soundSystem = new SoundSystem();
        DVDPlayer dvdPlayer = new DVDPlayer();

        // Creating the facade and simplifying interaction
        HomeTheaterFacade homeTheater = new HomeTheaterFacade(tv, soundSystem, dvdPlayer);

        // Using the facade to watch a movie
        homeTheater.watchMovie("Inception");

        // Using the facade to end the movie
        homeTheater.endMovie();
    }
}
