package org.example.PubSub;

import org.example.PubSub.enums.ConsumerState;
import org.example.PubSub.impl.Consumer;
import org.example.PubSub.impl.Producer;
import org.example.PubSub.model.Topic;
import org.example.PubSub.system.PubSubSystem;

/**
 * Main class demonstrating the In-Memory Pub-Sub System
 * 
 * Features demonstrated:
 * 1. Topic creation and management
 * 2. Producer publishing messages
 * 3. Consumer subscription and consumption
 * 4. Offset management and tracking
 * 5. Consumer state management
 * 6. Multiple producers and consumers
 * 7. Message delivery and processing
 * 8. System statistics
 * 
 * Design Patterns Used:
 * - Singleton Pattern: PubSubSystem
 * - Strategy Pattern: Consumer.MessageProcessor
 * - Observer Pattern: Pub-Sub mechanism itself
 * - Factory Pattern: Topic and Subscription creation
 * 
 * SOLID Principles Applied:
 * - Single Responsibility Principle: Each class has one clear responsibility
 * - Open/Closed Principle: System is open for extension (new message processors, queue implementations)
 * - Liskov Substitution Principle: Interfaces can be substituted with different implementations
 * - Interface Segregation Principle: Focused interfaces (IProducer, IConsumer, IMessageQueue, etc.)
 * - Dependency Inversion Principle: Depend on abstractions (interfaces) not concretions
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("In-Memory Pub-Sub System Demo");
        System.out.println("========================================\n");
        
        // Get the singleton instance of PubSubSystem
        PubSubSystem pubSubSystem = PubSubSystem.getInstance();
        
        try {
            // Demo 1: Basic Pub-Sub with Single Topic
            System.out.println("\n----- Demo 1: Basic Pub-Sub -----");
            demonstrateBasicPubSub(pubSubSystem);
            
            Thread.sleep(2000);
            
            // Demo 2: Multiple Topics and Consumers
            System.out.println("\n----- Demo 2: Multiple Topics and Consumers -----");
            demonstrateMultipleTopicsAndConsumers(pubSubSystem);
            
            Thread.sleep(2000);
            
            // Demo 3: Offset Management
            System.out.println("\n----- Demo 3: Offset Management -----");
            demonstrateOffsetManagement(pubSubSystem);
            
            Thread.sleep(2000);
            
            // Demo 4: Consumer State Management
            System.out.println("\n----- Demo 4: Consumer State Management -----");
            demonstrateConsumerStateManagement(pubSubSystem);
            
            Thread.sleep(2000);
            
            // Demo 5: Polling-based Message Consumption
            System.out.println("\n----- Demo 5: Polling-based Consumption -----");
            demonstratePollingConsumption(pubSubSystem);
            
            Thread.sleep(3000);
            
            // Print final system statistics
            pubSubSystem.printSystemStats();
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Demo interrupted: " + e.getMessage());
        } finally {
            // Graceful shutdown
            System.out.println("\n----- Shutting Down System -----");
            pubSubSystem.shutdown();
            System.out.println("Demo completed successfully!");
        }
    }
    
    /**
     * Demonstrates basic pub-sub functionality with one producer and one consumer
     */
    private static void demonstrateBasicPubSub(PubSubSystem pubSubSystem) {
        // Create a topic
        Topic ordersTopic = pubSubSystem.createTopic("orders");
        System.out.println("Created topic: " + ordersTopic.getTopicName());
        
        // Create a producer
        Producer orderProducer = new Producer("OrderProducer", pubSubSystem);
        System.out.println("Created producer: " + orderProducer.getProducerName());
        
        // Create a consumer
        Consumer orderConsumer = new Consumer("OrderConsumer", pubSubSystem);
        pubSubSystem.registerConsumer(orderConsumer);
        
        // Subscribe consumer to topic
        orderConsumer.subscribe(ordersTopic.getTopicId());
        
        // Publish messages
        System.out.println("\nPublishing messages...");
        orderProducer.publish(ordersTopic.getTopicId(), "Order #1001: 2x Laptop");
        orderProducer.publish(ordersTopic.getTopicId(), "Order #1002: 1x Mouse");
        orderProducer.publish(ordersTopic.getTopicId(), "Order #1003: 3x Keyboard");
        
        // Allow time for message processing
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Demonstrates multiple topics with multiple producers and consumers
     */
    private static void demonstrateMultipleTopicsAndConsumers(PubSubSystem pubSubSystem) {
        // Create multiple topics
        Topic paymentsTopic = pubSubSystem.createTopic("payments");
        Topic notificationsTopic = pubSubSystem.createTopic("notifications");
        
        // Create producers
        Producer paymentProducer = new Producer("PaymentProducer", pubSubSystem);
        Producer notificationProducer = new Producer("NotificationProducer", pubSubSystem);
        
        // Create consumers
        Consumer paymentConsumer1 = new Consumer("PaymentConsumer1", pubSubSystem);
        Consumer paymentConsumer2 = new Consumer("PaymentConsumer2", pubSubSystem);
        Consumer notificationConsumer = new Consumer("NotificationConsumer", pubSubSystem);
        
        pubSubSystem.registerConsumer(paymentConsumer1);
        pubSubSystem.registerConsumer(paymentConsumer2);
        pubSubSystem.registerConsumer(notificationConsumer);
        
        // Subscribe consumers to topics
        paymentConsumer1.subscribe(paymentsTopic.getTopicId());
        paymentConsumer2.subscribe(paymentsTopic.getTopicId());
        notificationConsumer.subscribe(notificationsTopic.getTopicId());
        
        // Publish messages to different topics
        System.out.println("\nPublishing to multiple topics...");
        paymentProducer.publish(paymentsTopic.getTopicId(), "Payment $100 from User A");
        paymentProducer.publish(paymentsTopic.getTopicId(), "Payment $50 from User B");
        
        notificationProducer.publish(notificationsTopic.getTopicId(), "Welcome email to User A");
        notificationProducer.publish(notificationsTopic.getTopicId(), "Order confirmation to User B");
        
        // Allow time for message processing
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Demonstrates offset management and seeking
     */
    private static void demonstrateOffsetManagement(PubSubSystem pubSubSystem) {
        // Create topic
        Topic eventsTopic = pubSubSystem.createTopic("events");
        
        // Create producer
        Producer eventProducer = new Producer("EventProducer", pubSubSystem);
        
        // Create consumer
        Consumer eventConsumer = new Consumer("EventConsumer", pubSubSystem);
        pubSubSystem.registerConsumer(eventConsumer);
        eventConsumer.subscribe(eventsTopic.getTopicId());
        
        // Publish multiple messages
        System.out.println("\nPublishing events...");
        for (int i = 1; i <= 5; i++) {
            eventProducer.publish(eventsTopic.getTopicId(), "Event #" + i);
        }
        
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Check current offset
        int currentOffset = pubSubSystem.getOffset(eventConsumer.getConsumerId(), eventsTopic.getTopicId());
        System.out.println("\nCurrent offset for EventConsumer: " + currentOffset);
        
        // Seek to a specific offset
        System.out.println("Seeking to offset 2...");
        pubSubSystem.seekToOffset(eventConsumer.getConsumerId(), eventsTopic.getTopicId(), 2);
        
        int newOffset = pubSubSystem.getOffset(eventConsumer.getConsumerId(), eventsTopic.getTopicId());
        System.out.println("New offset for EventConsumer: " + newOffset);
        
        // Publish more messages
        System.out.println("\nPublishing more events...");
        eventProducer.publish(eventsTopic.getTopicId(), "Event #6");
        eventProducer.publish(eventsTopic.getTopicId(), "Event #7");
        
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Demonstrates consumer state management (pause, resume)
     */
    private static void demonstrateConsumerStateManagement(PubSubSystem pubSubSystem) {
        // Create topic
        Topic logsTopic = pubSubSystem.createTopic("logs");
        
        // Create producer
        Producer logProducer = new Producer("LogProducer", pubSubSystem);
        
        // Create consumer
        Consumer logConsumer = new Consumer("LogConsumer", pubSubSystem);
        pubSubSystem.registerConsumer(logConsumer);
        logConsumer.subscribe(logsTopic.getTopicId());
        
        // Publish messages while consumer is active
        System.out.println("\nPublishing logs while consumer is ACTIVE...");
        logProducer.publish(logsTopic.getTopicId(), "INFO: Application started");
        logProducer.publish(logsTopic.getTopicId(), "DEBUG: Loading configuration");
        
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Pause consumer
        System.out.println("\nPausing consumer...");
        logConsumer.setState(ConsumerState.PAUSED);
        
        // Publish messages while consumer is paused
        System.out.println("Publishing logs while consumer is PAUSED...");
        logProducer.publish(logsTopic.getTopicId(), "WARN: Connection timeout");
        logProducer.publish(logsTopic.getTopicId(), "ERROR: Database connection failed");
        
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Resume consumer
        System.out.println("\nResuming consumer...");
        logConsumer.setState(ConsumerState.ACTIVE);
        
        // Publish more messages
        System.out.println("Publishing logs after consumer is ACTIVE again...");
        logProducer.publish(logsTopic.getTopicId(), "INFO: System recovered");
        
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Demonstrates polling-based message consumption
     */
    private static void demonstratePollingConsumption(PubSubSystem pubSubSystem) {
        // Create topic
        Topic streamTopic = pubSubSystem.createTopic("stream");
        
        // Create producer
        Producer streamProducer = new Producer("StreamProducer", pubSubSystem);
        
        // Create consumer
        Consumer streamConsumer = new Consumer("StreamConsumer", pubSubSystem);
        pubSubSystem.registerConsumer(streamConsumer);
        streamConsumer.subscribe(streamTopic.getTopicId());
        
        // Start polling mechanism
        System.out.println("\nStarting polling mechanism...");
        pubSubSystem.startPolling();
        
        // Publish messages that will be polled
        System.out.println("Publishing stream data...");
        for (int i = 1; i <= 3; i++) {
            streamProducer.publish(streamTopic.getTopicId(), "Stream Data #" + i);
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        // Stop polling
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        pubSubSystem.stopPolling();
        System.out.println("Polling mechanism stopped");
    }
}
