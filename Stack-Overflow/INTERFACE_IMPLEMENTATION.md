# Interface Implementation Summary

This document describes how the `Question` and `Answer` classes implement the `Votable` and `Commentable` interfaces following the **Interface Segregation Principle (ISP)**.

## Interface Segregation Principle (ISP)

Instead of creating one large interface with all methods, we've segregated functionalities into smaller, focused interfaces:

### Votable Interface

```java
public interface Votable {
    Vote addVote(Vote vote);
    boolean removeVote(String userId);
    int getVoteCount();
}
```

**Purpose:** Defines behavior for entities that can be voted on.

### Commentable Interface

```java
public interface Commentable {
    void addComment(Comment comment);
    List<Comment> getComments();
}
```

**Purpose:** Defines behavior for entities that can receive comments.

## Implementation

### Question Class

```java
public class Question implements Votable, Commentable {
    // ... fields ...
    
    // Votable interface implementation
    @Override
    public Vote addVote(Vote vote) {
        // Thread-safe vote management
        // Handles vote updates and changes
    }
    
    @Override
    public boolean removeVote(String userId) {
        // Thread-safe vote removal
    }
    
    @Override
    public int getVoteCount() {
        return voteCount.get();
    }
    
    // Commentable interface implementation
    @Override
    public void addComment(Comment comment) {
        comments.add(comment);
    }
    
    @Override
    public List<Comment> getComments() {
        return new ArrayList<>(comments);
    }
}
```

**Implements:**
- ✅ `Votable` - Questions can be upvoted/downvoted
- ✅ `Commentable` - Questions can receive comments

### Answer Class

```java
public class Answer implements Votable, Commentable {
    // ... fields ...
    
    // Votable interface implementation
    @Override
    public Vote addVote(Vote vote) {
        // Thread-safe vote management
        // Handles vote updates and changes
    }
    
    @Override
    public boolean removeVote(String userId) {
        // Thread-safe vote removal
    }
    
    @Override
    public int getVoteCount() {
        return voteCount.get();
    }
    
    // Commentable interface implementation
    @Override
    public void addComment(Comment comment) {
        comments.add(comment);
    }
    
    @Override
    public List<Comment> getComments() {
        return new ArrayList<>(comments);
    }
}
```

**Implements:**
- ✅ `Votable` - Answers can be upvoted/downvoted
- ✅ `Commentable` - Answers can receive comments

## Benefits of This Design

### 1. Interface Segregation Principle (ISP)
- **Small, focused interfaces** rather than one large interface
- Classes only implement what they need
- Easy to extend with new votable or commentable entities

### 2. Single Responsibility Principle (SRP)
- Each interface has one clear responsibility
- `Votable` handles voting behavior
- `Commentable` handles commenting behavior

### 3. Open/Closed Principle (OCP)
- New entities can implement these interfaces without modifying existing code
- For example, if we add `Comment` voting in the future, we can make `Comment` implement `Votable`

### 4. Dependency Inversion Principle (DIP)
- Client code can depend on the interfaces rather than concrete classes
- Example: A vote counting service can work with any `Votable` entity

### 5. Liskov Substitution Principle (LSP)
- Any `Votable` can be substituted for another `Votable`
- Any `Commentable` can be substituted for another `Commentable`

## Usage Examples

### Polymorphic Voting

```java
public void processVote(Votable votable, User voter, VoteType voteType) {
    Vote vote = new Vote(generateId(), voter, voteType);
    votable.addVote(vote);
    
    // Award reputation based on vote count
    int reputation = calculateReputation(votable.getVoteCount());
    votable.getAuthor().addReputation(reputation);
}

// Works with both Question and Answer
processVote(question, user, VoteType.UPVOTE);
processVote(answer, user, VoteType.UPVOTE);
```

### Polymorphic Commenting

```java
public void addCommentTo(Commentable commentable, User author, String content) {
    Comment comment = new Comment(generateId(), author, content);
    commentable.addComment(comment);
    
    // Notify interested parties
    notifyCommentAdded(commentable, comment);
}

// Works with both Question and Answer
addCommentTo(question, user, "Great question!");
addCommentTo(answer, user, "Excellent answer!");
```

### Vote Statistics

```java
public int getTotalVotes(List<Votable> votables) {
    return votables.stream()
            .mapToInt(Votable::getVoteCount)
            .sum();
}

// Can combine votes from questions and answers
List<Votable> allVotables = new ArrayList<>();
allVotables.addAll(questions);
allVotables.addAll(answers);
int total = getTotalVotes(allVotables);
```

## Thread Safety

Both `Question` and `Answer` implement these interfaces in a **thread-safe manner**:

1. **Atomic vote counts** using `AtomicInteger`
2. **Concurrent vote storage** using `ConcurrentHashMap`
3. **Thread-safe collections** using `CopyOnWriteArrayList` for comments
4. **Synchronized vote operations** to prevent race conditions

## Future Extensibility

This design makes it easy to extend the system:

### Add Voting to Comments
```java
public class Comment implements Votable {
    // Now comments can be voted on too!
}
```

### Add New Votable Entity
```java
public class Article implements Votable, Commentable {
    // Articles can be voted on and commented
}
```

### Create Composite Votable
```java
public class UserContributions implements Votable {
    private List<Question> questions;
    private List<Answer> answers;
    
    @Override
    public int getVoteCount() {
        int total = 0;
        for (Votable v : questions) total += v.getVoteCount();
        for (Votable v : answers) total += v.getVoteCount();
        return total;
    }
}
```

## Conclusion

By making `Question` and `Answer` implement `Votable` and `Commentable` interfaces:

✅ We follow **Interface Segregation Principle**  
✅ Code is more **maintainable** and **testable**  
✅ System is **extensible** for future features  
✅ We can use **polymorphism** effectively  
✅ Thread safety is properly implemented  
✅ All **SOLID principles** are respected  

This design provides a solid foundation for a scalable, maintainable Stack Overflow-like system.

