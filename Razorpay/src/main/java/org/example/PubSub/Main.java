package org.example.PubSub;

import org.example.PubSub.handler.DefaultMessageHandler;
import org.example.PubSub.model.*;
import org.example.PubSub.service.PubSubService;

/**
 * Main class demonstrating the Pub-Sub system with consumer groups.
 * This demo showcases:
 * 1. Creating topics
 * 2. Creating consumer groups and consumers
 * 3. Adding consumers to groups
 * 4. Subscribing consumer groups to topics
 * 5. Publishing messages
 * 6. Message distribution using round-robin within consumer groups
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== Pub-Sub System Demo ===\n");
        
        // Initialize the pub-sub service
        PubSubService pubSubService = new PubSubService();
        
        // ========== Step 1: Create Topics ==========
        System.out.println("--- Step 1: Creating Topics ---");
        Topic newsTopic = pubSubService.createTopic("News");
        Topic sportsTopic = pubSubService.createTopic("Sports");
        Topic techTopic = pubSubService.createTopic("Technology");
        
        System.out.println("Created topic: " + newsTopic);
        System.out.println("Created topic: " + sportsTopic);
        System.out.println("Created topic: " + techTopic);
        System.out.println();
        
        // ========== Step 2: Create Consumer Groups ==========
        System.out.println("--- Step 2: Creating Consumer Groups ---");
        ConsumerGroup newsGroup = pubSubService.createConsumerGroup("News-Processing-Group");
        ConsumerGroup sportsGroup = pubSubService.createConsumerGroup("Sports-Processing-Group");
        ConsumerGroup techGroup = pubSubService.createConsumerGroup("Tech-Processing-Group");
        
        System.out.println("Created consumer group: " + newsGroup);
        System.out.println("Created consumer group: " + sportsGroup);
        System.out.println("Created consumer group: " + techGroup);
        System.out.println();
        
        // ========== Step 3: Create Consumers ==========
        System.out.println("--- Step 3: Creating Consumers ---");
        Consumer consumer1 = pubSubService.createConsumer("Consumer-1");
        Consumer consumer2 = pubSubService.createConsumer("Consumer-2");
        Consumer consumer3 = pubSubService.createConsumer("Consumer-3");
        Consumer consumer4 = pubSubService.createConsumer("Consumer-4");
        Consumer consumer5 = pubSubService.createConsumer("Consumer-5");
        Consumer consumer6 = pubSubService.createConsumer("Consumer-6");
        
        System.out.println("Created consumer: " + consumer1);
        System.out.println("Created consumer: " + consumer2);
        System.out.println("Created consumer: " + consumer3);
        System.out.println("Created consumer: " + consumer4);
        System.out.println("Created consumer: " + consumer5);
        System.out.println("Created consumer: " + consumer6);
        System.out.println();
        
        // ========== Step 4: Add Consumers to Groups ==========
        System.out.println("--- Step 4: Adding Consumers to Groups ---");
        
        // Add consumers to News group
        pubSubService.addConsumerToGroup(newsGroup.getGroupId(), consumer1.getConsumerId());
        pubSubService.addConsumerToGroup(newsGroup.getGroupId(), consumer2.getConsumerId());
        
        // Add consumers to Sports group
        pubSubService.addConsumerToGroup(sportsGroup.getGroupId(), consumer3.getConsumerId());
        pubSubService.addConsumerToGroup(sportsGroup.getGroupId(), consumer4.getConsumerId());
        
        // Add consumers to Tech group
        pubSubService.addConsumerToGroup(techGroup.getGroupId(), consumer5.getConsumerId());
        pubSubService.addConsumerToGroup(techGroup.getGroupId(), consumer6.getConsumerId());
        
        System.out.println("Added Consumer-1 and Consumer-2 to News-Processing-Group");
        System.out.println("Added Consumer-3 and Consumer-4 to Sports-Processing-Group");
        System.out.println("Added Consumer-5 and Consumer-6 to Tech-Processing-Group");
        System.out.println("Active consumers in News-Processing-Group: " + 
                         newsGroup.getActiveConsumerCount());
        System.out.println("Active consumers in Sports-Processing-Group: " + 
                         sportsGroup.getActiveConsumerCount());
        System.out.println("Active consumers in Tech-Processing-Group: " + 
                         techGroup.getActiveConsumerCount());
        System.out.println();
        
        // ========== Step 5: Register Message Handlers ==========
        System.out.println("--- Step 5: Registering Message Handlers ---");
        pubSubService.registerMessageHandler(consumer1.getConsumerId(), 
                                           new DefaultMessageHandler("Consumer-1"));
        pubSubService.registerMessageHandler(consumer2.getConsumerId(), 
                                           new DefaultMessageHandler("Consumer-2"));
        pubSubService.registerMessageHandler(consumer3.getConsumerId(), 
                                           new DefaultMessageHandler("Consumer-3"));
        pubSubService.registerMessageHandler(consumer4.getConsumerId(), 
                                           new DefaultMessageHandler("Consumer-4"));
        pubSubService.registerMessageHandler(consumer5.getConsumerId(), 
                                           new DefaultMessageHandler("Consumer-5"));
        pubSubService.registerMessageHandler(consumer6.getConsumerId(), 
                                           new DefaultMessageHandler("Consumer-6"));
        
        System.out.println("Registered message handlers for all consumers");
        System.out.println();
        
        // ========== Step 6: Subscribe Consumer Groups to Topics ==========
        System.out.println("--- Step 6: Subscribing Consumer Groups to Topics ---");
        
        // Subscribe consumer groups to topics
        Subscription sub1 = pubSubService.subscribe(newsTopic.getTopicId(), newsGroup.getGroupId());
        Subscription sub2 = pubSubService.subscribe(sportsTopic.getTopicId(), sportsGroup.getGroupId());
        Subscription sub3 = pubSubService.subscribe(techTopic.getTopicId(), techGroup.getGroupId());
        
        System.out.println("News-Processing-Group subscribed to News topic");
        System.out.println("Sports-Processing-Group subscribed to Sports topic");
        System.out.println("Tech-Processing-Group subscribed to Technology topic");
        System.out.println();
        
        // ========== Step 7: Publish Messages ==========
        System.out.println("--- Step 7: Publishing Messages ---");
        
        // Publish to News topic - should be received by one consumer from News-Processing-Group
        System.out.println("\nPublishing message to News topic:");
        Message msg1 = pubSubService.publish(newsTopic.getTopicId(), "Breaking: Major news update!");
        System.out.println("Published: " + msg1.getPayload());
        System.out.println("Status: " + msg1.getStatus());
        System.out.println("Processed by: " + msg1.getProcessedBy());
        
        // Publish to Sports topic - should be received by one consumer from Sports-Processing-Group
        System.out.println("\nPublishing message to Sports topic:");
        Message msg2 = pubSubService.publish(sportsTopic.getTopicId(), "Match result: Team A wins!");
        System.out.println("Published: " + msg2.getPayload());
        System.out.println("Status: " + msg2.getStatus());
        System.out.println("Processed by: " + msg2.getProcessedBy());
        
        // Publish to Technology topic - should be received by one consumer from Tech-Processing-Group
        System.out.println("\nPublishing message to Technology topic:");
        Message msg3 = pubSubService.publish(techTopic.getTopicId(), "New AI breakthrough announced!");
        System.out.println("Published: " + msg3.getPayload());
        System.out.println("Status: " + msg3.getStatus());
        System.out.println("Processed by: " + msg3.getProcessedBy());
        
        System.out.println();
        
        // ========== Step 8: Demonstrate Consumer Group Round-Robin ==========
        System.out.println("--- Step 8: Demonstrating Consumer Group Round-Robin Distribution ---");
        System.out.println("Publishing 4 messages to News topic (subscribed by News-Processing-Group with 2 consumers):");
        
        for (int i = 1; i <= 4; i++) {
            Message msg = pubSubService.publish(newsTopic.getTopicId(), 
                                              "News message #" + i + " for group processing");
            System.out.println("Message #" + i + " processed by: " + msg.getProcessedBy());
        }
        
        System.out.println("\nPublishing 4 messages to Sports topic (subscribed by Sports-Processing-Group with 2 consumers):");
        
        for (int i = 1; i <= 4; i++) {
            Message msg = pubSubService.publish(sportsTopic.getTopicId(), 
                                              "Sports message #" + i + " for group processing");
            System.out.println("Message #" + i + " processed by: " + msg.getProcessedBy());
        }
        
        System.out.println();
        
        // ========== Step 9: Demonstrate Unsubscription ==========
        System.out.println("--- Step 9: Demonstrating Unsubscription ---");
        boolean unsubscribed = pubSubService.unsubscribe(sub1.getSubscriptionId());
        System.out.println("Unsubscribed News-Processing-Group from News topic: " + unsubscribed);
        
        // Publish after unsubscription - News-Processing-Group should not receive this
        System.out.println("\nPublishing message after News-Processing-Group unsubscribed:");
        Message msg4 = pubSubService.publish(newsTopic.getTopicId(), 
                                           "This message should not reach News-Processing-Group");
        System.out.println("Published: " + msg4.getPayload());
        System.out.println("Status: " + msg4.getStatus());
        System.out.println("Processed by: " + msg4.getProcessedBy());
        
        System.out.println();
        System.out.println("=== Demo Complete ===");
    }
}
