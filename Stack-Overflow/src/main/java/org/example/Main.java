package org.example;

import org.example.enums.UserRole;
import org.example.enums.VoteType;
import org.example.model.*;
import org.example.observers.ConsoleNotificationObserver;
import org.example.observers.EmailNotificationObserver;
import org.example.strategies.reputation.GenerousReputationStrategy;
import org.example.strategies.search.*;
import org.example.system.StackOverflowSystem;

import java.util.*;

/**
 * Demo class showcasing the Stack Overflow system
 * 
 * Design Patterns Used:
 * 1. Singleton Pattern - StackOverflowSystem
 * 2. Strategy Pattern - ReputationStrategy, SearchStrategy
 * 3. Observer Pattern - NotificationObserver
 * 4. Composite Pattern - CompositeSearchStrategy
 * 
 * SOLID Principles:
 * 1. Single Responsibility - Each class has one responsibility
 * 2. Open/Closed - Extensible through strategies and observers
 * 3. Liskov Substitution - All strategies are interchangeable
 * 4. Interface Segregation - Small, focused interfaces
 * 5. Dependency Inversion - Depends on abstractions (interfaces)
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== Stack Overflow System Demo ===\n");
        
        // Get singleton instance
        StackOverflowSystem system = StackOverflowSystem.getInstance();
        
        // Create users
        System.out.println("Creating users...");
        User alice = system.createUser("Alice", "alice@example.com", UserRole.MEMBER);
        User bob = system.createUser("Bob", "bob@example.com", UserRole.MEMBER);
        User charlie = system.createUser("Charlie", "charlie@example.com", UserRole.MEMBER);
        User diana = system.createUser("Diana", "diana@example.com", UserRole.MODERATOR);
        System.out.println("Created users: " + alice.getUsername() + ", " + bob.getUsername() + 
                         ", " + charlie.getUsername() + ", " + diana.getUsername() + "\n");
        
        // Register observers (Observer Pattern)
        System.out.println("Registering notification observers...");
        system.registerObserver(new ConsoleNotificationObserver(alice));
        system.registerObserver(new EmailNotificationObserver(bob));
        System.out.println("Observers registered\n");
        
        // Create tags
        System.out.println("Creating tags...");
        Tag javaTag = system.createTag("java", "Java programming language");
        Tag pythonTag = system.createTag("python", "Python programming language");
        Tag designPatternTag = system.createTag("design-patterns", "Software design patterns");
        Tag concurrencyTag = system.createTag("concurrency", "Concurrent programming");
        System.out.println("Created tags: java, python, design-patterns, concurrency\n");
        
        // Post questions
        System.out.println("Posting questions...");
        Question q1 = system.postQuestion(
            alice,
            "What is the Singleton pattern in Java?",
            "I'm learning about design patterns and I'm confused about the Singleton pattern. " +
            "Can someone explain it with an example?",
            new HashSet<>(Arrays.asList(javaTag, designPatternTag))
        );
        
        Question q2 = system.postQuestion(
            bob,
            "How to handle concurrent access in Java?",
            "I have a multi-threaded application and I'm getting race conditions. " +
            "What's the best way to handle concurrent access to shared resources?",
            new HashSet<>(Arrays.asList(javaTag, concurrencyTag))
        );
        
        Question q3 = system.postQuestion(
            charlie,
            "Python vs Java for web development?",
            "I'm starting a new web project. Should I use Python or Java? " +
            "What are the pros and cons of each?",
            new HashSet<>(Arrays.asList(javaTag, pythonTag))
        );
        
        System.out.println("Posted 3 questions\n");
        
        // Post answers
        System.out.println("Posting answers...");
        Answer a1 = system.postAnswer(
            bob,
            q1,
            "The Singleton pattern ensures a class has only one instance and provides " +
            "a global point of access to it. Here's a thread-safe example:\n\n" +
            "public class Singleton {\n" +
            "    private static volatile Singleton instance;\n" +
            "    private Singleton() {}\n" +
            "    public static Singleton getInstance() {\n" +
            "        if (instance == null) {\n" +
            "            synchronized (Singleton.class) {\n" +
            "                if (instance == null) {\n" +
            "                    instance = new Singleton();\n" +
            "                }\n" +
            "            }\n" +
            "        }\n" +
            "        return instance;\n" +
            "    }\n" +
            "}"
        );
        
        Answer a2 = system.postAnswer(
            diana,
            q1,
            "Another approach is to use enum for singleton:\n\n" +
            "public enum Singleton {\n" +
            "    INSTANCE;\n" +
            "    // your methods here\n" +
            "}\n\n" +
            "This is thread-safe by default and prevents reflection attacks!"
        );
        
        Answer a3 = system.postAnswer(
            charlie,
            q2,
            "Use synchronized blocks or methods for simple cases:\n\n" +
            "synchronized(lock) {\n" +
            "    // critical section\n" +
            "}\n\n" +
            "For better performance, consider using java.util.concurrent classes like:\n" +
            "- ConcurrentHashMap\n" +
            "- AtomicInteger\n" +
            "- ReentrantLock"
        );
        
        Answer a4 = system.postAnswer(
            alice,
            q2,
            "Don't forget about the volatile keyword for visibility! " +
            "And use CopyOnWriteArrayList for thread-safe lists where reads are more common than writes."
        );
        
        System.out.println("Posted 4 answers\n");
        
        // Add comments
        System.out.println("Adding comments...");
        system.commentOnQuestion(charlie, q1, "Great question! I'm learning design patterns too.");
        system.commentOnAnswer(alice, a1, "Thanks! This is very clear and helpful.");
        system.commentOnAnswer(alice, a2, "Wow, I didn't know about the enum approach!");
        system.commentOnAnswer(bob, a3, "What about ReadWriteLock for read-heavy scenarios?");
        System.out.println("Added 4 comments\n");
        
        // Voting
        System.out.println("Casting votes...");
        system.voteOnQuestion(bob, q1, VoteType.UPVOTE);
        system.voteOnQuestion(charlie, q1, VoteType.UPVOTE);
        system.voteOnQuestion(diana, q1, VoteType.UPVOTE);
        
        system.voteOnQuestion(alice, q2, VoteType.UPVOTE);
        system.voteOnQuestion(diana, q2, VoteType.UPVOTE);
        
        system.voteOnAnswer(alice, a1, VoteType.UPVOTE);
        system.voteOnAnswer(charlie, a1, VoteType.UPVOTE);
        system.voteOnAnswer(diana, a1, VoteType.UPVOTE);
        
        system.voteOnAnswer(alice, a2, VoteType.UPVOTE);
        system.voteOnAnswer(bob, a2, VoteType.UPVOTE);
        system.voteOnAnswer(charlie, a2, VoteType.UPVOTE);
        system.voteOnAnswer(diana, a2, VoteType.UPVOTE);
        
        system.voteOnAnswer(bob, a3, VoteType.UPVOTE);
        system.voteOnAnswer(diana, a3, VoteType.UPVOTE);
        
        system.voteOnAnswer(bob, a4, VoteType.DOWNVOTE); // One downvote
        System.out.println("Votes cast\n");
        
        // Accept answer
        System.out.println("Accepting best answer...");
        system.acceptAnswer(a2, alice); // Alice accepts Diana's answer
        System.out.println("Answer accepted\n");
        
        // Display question details
        system.displayQuestionDetails(q1);
        
        // User reputation
        System.out.println("\n=== User Reputations ===");
        System.out.println("Alice: " + alice.getReputation() + " points");
        System.out.println("Bob: " + bob.getReputation() + " points");
        System.out.println("Charlie: " + charlie.getReputation() + " points");
        System.out.println("Diana: " + diana.getReputation() + " points\n");
        
        // Search demonstrations (Strategy Pattern)
        System.out.println("\n=== Search Demonstrations ===\n");
        
        // Search by keyword
        System.out.println("1. Keyword Search for 'singleton':");
        List<Question> keywordResults = system.searchQuestions("singleton", new KeywordSearchStrategy());
        keywordResults.forEach(q -> System.out.println("   - " + q.getTitle()));
        
        // Search by tag
        System.out.println("\n2. Tag Search for 'java':");
        List<Question> tagResults = system.searchQuestions("java", new TagSearchStrategy());
        tagResults.forEach(q -> System.out.println("   - " + q.getTitle()));
        
        // Search by user
        System.out.println("\n3. User Search for 'bob':");
        List<Question> userResults = system.searchQuestions("bob", new UserSearchStrategy());
        userResults.forEach(q -> System.out.println("   - " + q.getTitle()));
        
        // Composite search
        System.out.println("\n4. Composite Search for 'java':");
        CompositeSearchStrategy compositeSearch = new CompositeSearchStrategy(
            Arrays.asList(new KeywordSearchStrategy(), new TagSearchStrategy())
        );
        List<Question> compositeResults = system.searchQuestions("java", compositeSearch);
        compositeResults.forEach(q -> System.out.println("   - " + q.getTitle()));
        
        // Demonstrate strategy change (Strategy Pattern)
        System.out.println("\n\n=== Changing Reputation Strategy ===");
        System.out.println("Switching to Generous Reputation Strategy...");
        system.setReputationStrategy(new GenerousReputationStrategy());
        
        User newUser = system.createUser("Eve", "eve@example.com", UserRole.MEMBER);
        Question q4 = system.postQuestion(
            newUser,
            "What is the Observer pattern?",
            "Can someone explain the Observer pattern?",
            new HashSet<>(Arrays.asList(designPatternTag))
        );
        
        system.voteOnQuestion(alice, q4, VoteType.UPVOTE);
        
        System.out.println("Eve's reputation with generous strategy: " + newUser.getReputation() + " points");
        System.out.println("(Compare to the default strategy which would give less)\n");
        
        // Statistics
        System.out.println("\n=== System Statistics ===");
        System.out.println("Total Users: " + system.getAllUsers().size());
        System.out.println("Total Questions: " + system.getAllQuestions().size());
        System.out.println("Total Tags: " + system.getAllTags().size());
        
        System.out.println("\n=== Demo Complete ===");
        System.out.println("\nDesign Patterns Demonstrated:");
        System.out.println("✓ Singleton Pattern - StackOverflowSystem has only one instance");
        System.out.println("✓ Strategy Pattern - Pluggable reputation and search strategies");
        System.out.println("✓ Observer Pattern - Notification system for events");
        System.out.println("✓ Composite Pattern - Combining multiple search strategies");
        
        System.out.println("\nSOLID Principles Applied:");
        System.out.println("✓ Single Responsibility - Each class has one clear purpose");
        System.out.println("✓ Open/Closed - Extensible through interfaces (strategies, observers)");
        System.out.println("✓ Liskov Substitution - All strategies are interchangeable");
        System.out.println("✓ Interface Segregation - Small, focused interfaces (Votable, Commentable)");
        System.out.println("✓ Dependency Inversion - System depends on abstractions, not concrete classes");
        
        System.out.println("\nConcurrency Features:");
        System.out.println("✓ Thread-safe Singleton with double-checked locking");
        System.out.println("✓ ConcurrentHashMap for thread-safe collections");
        System.out.println("✓ AtomicInteger for thread-safe counters");
        System.out.println("✓ Synchronized methods for critical sections");
        System.out.println("✓ CopyOnWriteArrayList for concurrent read-heavy operations");
    }
}
