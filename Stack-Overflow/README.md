# Stack Overflow System - Low Level Design

A comprehensive Stack Overflow-like system implementation demonstrating proper design patterns and SOLID principles.

## Features

✅ **User Management**
- Users can post questions, answers, and comments
- Reputation system based on user activity and contribution quality
- Multiple user roles (Admin, Moderator, Member, Guest)

✅ **Question & Answer System**
- Post questions with tags
- Answer questions
- Accept answers (by question author)
- Comment on questions and answers
- Voting system (upvote/downvote)
- Question status management (Open, Closed, Deleted, Flagged)

✅ **Search Functionality**
- Search by keywords
- Search by tags
- Search by user
- Composite search (combining multiple strategies)

✅ **Notifications**
- Observer pattern for real-time notifications
- Console and Email notification implementations
- Events: new answers, accepted answers, comments, votes

✅ **Thread Safety**
- Concurrent access handling
- Thread-safe collections (ConcurrentHashMap, CopyOnWriteArrayList)
- Atomic counters (AtomicInteger, AtomicLong)
- Synchronized critical sections
- Thread-safe Singleton pattern

## Design Patterns Used

### 1. Singleton Pattern
**Class:** `StackOverflowSystem`

Ensures only one instance of the system exists. Uses double-checked locking for thread safety.

```java
StackOverflowSystem system = StackOverflowSystem.getInstance();
```

### 2. Strategy Pattern
**Interfaces:** `ReputationStrategy`, `SearchStrategy`

**Implementations:**
- Reputation strategies: `DefaultReputationStrategy`, `GenerousReputationStrategy`
- Search strategies: `KeywordSearchStrategy`, `TagSearchStrategy`, `UserSearchStrategy`

Allows runtime selection of algorithms for reputation calculation and search.

```java
system.setReputationStrategy(new GenerousReputationStrategy());
List<Question> results = system.searchQuestions("java", new TagSearchStrategy());
```

### 3. Observer Pattern
**Interface:** `NotificationObserver`

**Implementations:** `ConsoleNotificationObserver`, `EmailNotificationObserver`

Notifies interested parties about system events (answers, votes, comments).

```java
system.registerObserver(new ConsoleNotificationObserver(user));
```

### 4. Composite Pattern
**Class:** `CompositeSearchStrategy`

Combines multiple search strategies to provide comprehensive search results.

```java
CompositeSearchStrategy search = new CompositeSearchStrategy(
    Arrays.asList(new KeywordSearchStrategy(), new TagSearchStrategy())
);
```

## SOLID Principles

### Single Responsibility Principle (SRP)
Each class has one clear responsibility:
- `User` - manages user data
- `Question` - manages question data
- `Answer` - manages answer data
- `Vote` - represents a vote
- `StackOverflowSystem` - orchestrates the system

### Open/Closed Principle (OCP)
System is open for extension but closed for modification:
- New reputation strategies can be added without modifying existing code
- New search strategies can be added without changing the search mechanism
- New notification observers can be added without modifying the notification system

### Liskov Substitution Principle (LSP)
All strategy implementations are interchangeable:
- Any `ReputationStrategy` can replace another
- Any `SearchStrategy` can replace another
- Any `NotificationObserver` can replace another

### Interface Segregation Principle (ISP)
Small, focused interfaces:
- `Votable` - only voting-related methods
- `Commentable` - only commenting-related methods
- `NotificationObserver` - only notification methods
- `SearchStrategy` - only search method
- `ReputationStrategy` - only reputation calculation methods

### Dependency Inversion Principle (DIP)
High-level modules depend on abstractions:
- `StackOverflowSystem` depends on `ReputationStrategy` interface, not concrete implementations
- Search functionality depends on `SearchStrategy` interface
- Notification system depends on `NotificationObserver` interface

## Thread Safety Features

1. **Thread-safe Singleton:** Double-checked locking with volatile keyword
2. **Concurrent Collections:**
   - `ConcurrentHashMap` for users, questions, answers, tags
   - `CopyOnWriteArrayList` for lists with read-heavy workloads
3. **Atomic Counters:**
   - `AtomicInteger` for vote counts and view counts
   - `AtomicLong` for ID generation
4. **Synchronized Methods:**
   - Critical sections for reputation updates
   - Vote management
   - Content updates

## Project Structure

```
Stack-Overflow/
├── src/main/java/org/example/
│   ├── enums/
│   │   ├── CommentType.java
│   │   ├── QuestionStatus.java
│   │   ├── UserRole.java
│   │   └── VoteType.java
│   ├── interfaces/
│   │   ├── Commentable.java
│   │   ├── NotificationObserver.java
│   │   ├── ReputationStrategy.java
│   │   ├── SearchStrategy.java
│   │   └── Votable.java
│   ├── model/
│   │   ├── Answer.java
│   │   ├── Comment.java
│   │   ├── Question.java
│   │   ├── Tag.java
│   │   ├── User.java
│   │   └── Vote.java
│   ├── observers/
│   │   ├── ConsoleNotificationObserver.java
│   │   └── EmailNotificationObserver.java
│   ├── strategies/
│   │   ├── reputation/
│   │   │   ├── DefaultReputationStrategy.java
│   │   │   └── GenerousReputationStrategy.java
│   │   └── search/
│   │       ├── CompositeSearchStrategy.java
│   │       ├── KeywordSearchStrategy.java
│   │       ├── TagSearchStrategy.java
│   │       └── UserSearchStrategy.java
│   ├── system/
│   │   └── StackOverflowSystem.java
│   └── Main.java
└── pom.xml
```

## Usage Example

```java
// Get system instance (Singleton)
StackOverflowSystem system = StackOverflowSystem.getInstance();

// Create users
User alice = system.createUser("Alice", "alice@example.com", UserRole.MEMBER);
User bob = system.createUser("Bob", "bob@example.com", UserRole.MEMBER);

// Register observers
system.registerObserver(new ConsoleNotificationObserver(alice));

// Create tags
Tag javaTag = system.createTag("java", "Java programming language");
Tag designPatternTag = system.createTag("design-patterns", "Software design patterns");

// Post a question
Question question = system.postQuestion(
    alice,
    "What is the Singleton pattern?",
    "Can someone explain the Singleton pattern with an example?",
    new HashSet<>(Arrays.asList(javaTag, designPatternTag))
);

// Post an answer
Answer answer = system.postAnswer(
    bob,
    question,
    "The Singleton pattern ensures a class has only one instance..."
);

// Vote on the answer
system.voteOnAnswer(alice, answer, VoteType.UPVOTE);

// Accept the answer
system.acceptAnswer(answer, alice);

// Add a comment
system.commentOnAnswer(alice, answer, "Thanks! Very helpful!");

// Search questions
List<Question> results = system.searchQuestions("singleton", new KeywordSearchStrategy());

// Display question details
system.displayQuestionDetails(question);

// Check user reputation
System.out.println("Alice's reputation: " + alice.getReputation());
```

## Running the Demo

```bash
cd Stack-Overflow
mvn clean compile
mvn exec:java -Dexec.mainClass="org.example.Main"
```

## Requirements Met

✅ Users can post questions, answer questions, and comment on questions and answers  
✅ Users can vote on questions and answers  
✅ Questions have tags associated with them  
✅ Users can search for questions based on keywords, tags, or user profiles  
✅ System assigns reputation scores based on activity and quality of contributions  
✅ System handles concurrent access and ensures data consistency  
✅ Proper design patterns implemented (Singleton, Strategy, Observer, Composite)  
✅ SOLID principles followed throughout the design  

## Key Design Decisions

1. **Immutability where possible:** User IDs, question IDs, etc. are final
2. **Defensive copying:** Collections returned from getters are copies, not direct references
3. **Thread safety first:** All shared data structures are thread-safe
4. **Extensibility:** Easy to add new strategies, observers, and features
5. **Separation of concerns:** Clear boundaries between different components
6. **Testability:** Dependencies are injected through interfaces

## Future Enhancements

- Add bounty system for questions
- Implement badges and achievements
- Add user profiles with activity history
- Implement question editing history
- Add duplicate question detection
- Implement content moderation features
- Add ranking/sorting algorithms for search results
- Implement caching for frequently accessed questions

## License

This is a demonstration project for educational purposes.

