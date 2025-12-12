package org.example.Logger;

import org.example.Logger.enums.LogLevel;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            // Initialize logger with configuration file
            Logger logger = Logger.initialize("logger-config.json");
            
            System.out.println("=== Logger System Demo ===\n");
            
            // Example 1: INFO log (goes to Console)
            System.out.println("1. Logging INFO message:");
            logger.info("Application started successfully");
            
            System.out.println("\n2. Logging WARNING message:");
            logger.warning("Memory usage is above 80%");
            
            // Example 2: ERROR log (goes to Web + File)
            System.out.println("\n3. Logging ERROR message (will go to WEB and FILE):");
            logger.error("Database connection failed");
            
            // Example 3: DEBUG log (goes to File)
            System.out.println("\n4. Logging DEBUG message (will go to FILE only):");
            logger.debug("Processing user request with ID: 12345");
            
            // Example 4: DELETE log (goes to File + Web)
            System.out.println("\n5. Logging DELETE message (will go to FILE and WEB):");
            logger.delete("User account deleted: user@example.com");
            
            // Example 5: Using log method directly with class name
            System.out.println("\n6. Logging with class name context:");
            logger.log(LogLevel.ERROR, "Null pointer exception occurred", "UserService");
            
            // Example 6: Multiple logs
            System.out.println("\n7. Logging multiple messages:");
            for (int i = 1; i <= 3; i++) {
                logger.info("Processing batch " + i + " of 3");
            }
            
            System.out.println("\n=== Demo completed ===");
            System.out.println("\nCheck the following locations:");
            System.out.println("- Console output for INFO and WARNING logs");
            System.out.println("- logs/errors.log for ERROR logs");
            System.out.println("- logs/debug.log for DEBUG logs");
            System.out.println("- logs/deletions.log for DELETE logs");
            System.out.println("- Web endpoints (simulated) for ERROR and DELETE logs");
            
            // Graceful shutdown
            logger.shutdown();
            
        } catch (IOException e) {
            System.err.println("Failed to initialize logger: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
