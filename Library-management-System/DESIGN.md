# Library Management System - Design Document

## 🎯 Design Overview

This Library Management System is designed as a single-file implementation following industry best practices, SOLID principles, and proven design patterns. The system manages books, users, borrowing operations, and fines with a clean, maintainable architecture.

## 🏛️ Architecture

### Layered Architecture
```
┌─────────────────────────────────────┐
│     Presentation Layer (Main)       │
├─────────────────────────────────────┤
│   Facade Layer (LibrarySystem)     │
├─────────────────────────────────────┤
│     Service Layer                   │
│  - SearchService                    │
│  - NotificationService              │
├─────────────────────────────────────┤
│     Business Logic Layer            │
│  - Inventory                        │
│  - IssueRecordManager               │
│  - MembershipManager                │
├─────────────────────────────────────┤
│     Domain Model Layer              │
│  - User, Book, IssueRecord, etc.   │
└─────────────────────────────────────┘
```

## 🎨 Design Patterns Implementation

### 1. Singleton Pattern
**Purpose**: Ensure single instance of LibrarySystem  
**Implementation**: Thread-safe double-checked locking

**Why?**
- Library system should have one central point of control
- Prevents multiple instances from creating inconsistent state
- Provides global access point

```java
private static LibrarySystem instance;

public static LibrarySystem getInstance() {
    if (instance == null) {
        synchronized (LibrarySystem.class) {
            if (instance == null) {
                instance = new LibrarySystem();
            }
        }
    }
    return instance;
}
```

### 2. Strategy Pattern
**Purpose**: Flexible search and fine calculation algorithms  
**Used For**: 
- Search strategies (Title, Author, Category)
- Fine calculation strategies

**Why?**
- New search criteria can be added without modifying existing code
- Different fine rules can be implemented (e.g., student vs faculty)
- Algorithms can be swapped at runtime

```java
// Strategy Interface
interface SearchStrategy {
    List<Book> search(String query, List<Book> books);
}

// Concrete Strategies
class TitleSearchStrategy implements SearchStrategy { ... }
class AuthorSearchStrategy implements SearchStrategy { ... }
class CategorySearchStrategy implements SearchStrategy { ... }
```

### 3. Observer Pattern
**Purpose**: Decouple notifications from business logic  
**Used For**: Email and SMS notifications

**Why?**
- New notification channels can be added easily
- Business logic doesn't depend on notification implementation
- Multiple observers can react to same event

```java
interface NotificationObserver {
    void update(String message, User user);
}

class EmailNotificationObserver implements NotificationObserver { ... }
class SmsNotificationObserver implements NotificationObserver { ... }
```

### 4. Facade Pattern
**Purpose**: Simplify complex subsystem interactions  
**Implementation**: LibrarySystem class

**Why?**
- Provides clean, simple API for clients
- Hides complexity of multiple managers and services
- Acts as single entry point for all operations

## 🔧 SOLID Principles Application

### 1. Single Responsibility Principle (SRP)

Each class has **one reason to change**:

| Class | Responsibility |
|-------|---------------|
| `Inventory` | Manage book storage and retrieval |
| `IssueRecordManager` | Manage borrowing transactions |
| `MembershipManager` | Manage user memberships |
| `SearchService` | Execute search operations |
| `NotificationService` | Handle user notifications |
| `Book` | Represent book data and copies |
| `IssueRecord` | Track borrowing details |

**Example**: If notification logic changes, only `NotificationService` and observers change.

### 2. Open/Closed Principle (OCP)

**Open for extension, closed for modification:**

✅ **Adding New Search Strategy**
```java
// No modification to existing code needed
class PublisherSearchStrategy implements SearchStrategy {
    @Override
    public List<Book> search(String query, List<Book> books) {
        return books.stream()
            .filter(book -> book.getPublisher().contains(query))
            .collect(Collectors.toList());
    }
}
```

✅ **Adding New Notification Channel**
```java
class PushNotificationObserver implements NotificationObserver {
    @Override
    public void update(String message, User user) {
        // Send push notification
    }
}
```

### 3. Liskov Substitution Principle (LSP)

**Subtypes must be substitutable for base types:**

```java
// User is base class
User user = new Member(...);  // Works perfectly
user = new Librarian(...);     // Works perfectly

// Both can call displayUserInfo() with specific behavior
user.displayUserInfo();        // Polymorphic call
```

All derived classes strengthen, never weaken, base class contracts.

### 4. Interface Segregation Principle (ISP)

**Clients shouldn't depend on interfaces they don't use:**

❌ **Bad Design (Fat Interface)**
```java
interface AllInOne {
    List<Book> search(String query);
    double calculateFine(LocalDate due, LocalDate returned);
    void sendNotification(String message);
    void addBook(Book book);
}
```

✅ **Good Design (Segregated Interfaces)**
```java
interface SearchStrategy { ... }
interface FineCalculator { ... }
interface NotificationObserver { ... }
```

### 5. Dependency Inversion Principle (DIP)

**Depend on abstractions, not concretions:**

```java
class LibrarySystem {
    // Depends on interface, not implementation
    private SearchService searchService;
    private FineCalculator fineCalculator;  // Interface
    
    // Can inject any implementation
    public void setFineCalculator(FineCalculator calculator) {
        this.fineCalculator = calculator;
    }
}
```

## 🧱 OOP Concepts

### 1. Abstraction
```java
// Abstract class defining common user behavior
abstract class User {
    protected String userId;
    protected String name;
    
    public abstract void displayUserInfo();
}
```

### 2. Encapsulation
```java
class Book {
    // Private fields
    private String isbn;
    private String title;
    private List<BookCopy> copies;
    
    // Public methods for controlled access
    public String getIsbn() { return isbn; }
    public BookCopy getAvailableCopy() { ... }
}
```

### 3. Inheritance
```java
// Librarian inherits from User
class Librarian extends User {
    private String employeeId;
    
    @Override
    public void displayUserInfo() {
        // Librarian-specific implementation
    }
}
```

### 4. Polymorphism
```java
User user1 = new Librarian(...);
User user2 = new Member(...);

// Same method call, different behavior
user1.displayUserInfo();  // Shows librarian info
user2.displayUserInfo();  // Shows member info
```

## 📊 Data Model

### Core Entities

```
User (Abstract)
├── userId: String
├── name: String
├── email: String
├── role: UserRole
└── displayUserInfo(): void

Member extends User
├── membershipType: MembershipType
├── borrowedBooks: List<IssueRecord>
├── totalFines: double
└── canBorrowMoreBooks(): boolean

Librarian extends User
├── employeeId: String
├── addBook(): void
├── removeBook(): void
└── registerMember(): void

Book
├── isbn: String
├── title: String
├── author: String
├── category: BookCategory
├── copies: List<BookCopy>
└── getAvailableCopy(): BookCopy

BookCopy
├── copyId: String
├── book: Book
└── status: BookCopyStatus

IssueRecord
├── recordId: String
├── member: Member
├── bookCopy: BookCopy
├── issueDate: LocalDate
├── dueDate: LocalDate
├── returnDate: LocalDate
└── fine: double
```

## 🔄 Key Workflows

### 1. Issue Book Workflow
```
1. Member requests book (by ISBN)
2. System validates:
   - Member exists
   - Member hasn't reached borrowing limit (5 books)
   - Book copy is available
3. System creates IssueRecord
4. Updates BookCopy status to ISSUED
5. Adds record to Member's borrowed books
6. Sends notifications (Email + SMS)
7. Returns success
```

### 2. Return Book Workflow
```
1. Member returns book (by Record ID)
2. System validates:
   - Record exists
   - Book not already returned
3. Sets return date
4. Calculates fine if overdue
5. Updates BookCopy status to AVAILABLE
6. Removes record from Member's active borrows
7. Adds fine to Member's account if applicable
8. Sends notification
9. Returns success
```

### 3. Search Book Workflow
```
1. User provides search query
2. User selects search type (Title/Author/Category)
3. System sets appropriate search strategy
4. Strategy executes search on book collection
5. Returns matching books
```

## 🎯 Design Decisions

### Why Single File?
**Requested by user** for easier review and sharing while maintaining:
- Clear logical separation through sections
- Proper package structure
- Complete encapsulation
- All design patterns

### Why Enums?
```java
enum BookCategory { FICTION, NON_FICTION, ... }
```
- Type safety
- Compile-time checking
- Better IDE support
- Clear domain vocabulary

### Why Separate BookCopy?
```java
class Book {
    private List<BookCopy> copies;
}
```
- Track individual physical copies
- Different copies can have different statuses
- Enables detailed inventory management
- Supports real-world library operations

### Why IssueRecord?
```java
class IssueRecord { ... }
```
- Complete audit trail
- Historical data preservation
- Fine calculation
- Reporting capabilities

## 🚀 Extensibility

### Easy to Add:
1. **New Search Criteria**: Implement `SearchStrategy`
2. **New Notification Channels**: Implement `NotificationObserver`
3. **Custom Fine Rules**: Implement `FineCalculator`
4. **New User Types**: Extend `User` class
5. **Book Reservations**: Add `ReservationManager`
6. **Rating System**: Add `Review` and `Rating` classes

### Example Extensions:

**1. ISBN Search**
```java
class IsbnSearchStrategy implements SearchStrategy {
    @Override
    public List<Book> search(String query, List<Book> books) {
        return books.stream()
            .filter(book -> book.getIsbn().equals(query))
            .collect(Collectors.toList());
    }
}
```

**2. Faculty Fine Calculator** (50% discount)
```java
class FacultyFineCalculator implements FineCalculator {
    private static final double FINE_PER_DAY = 5.0;
    
    @Override
    public double calculateFine(LocalDate dueDate, LocalDate returnDate) {
        long days = ChronoUnit.DAYS.between(dueDate, returnDate);
        return days > 0 ? days * FINE_PER_DAY : 0.0;
    }
}
```

## ✅ Best Practices Applied

1. **Immutability**: Use of `LocalDate` (immutable)
2. **Defensive Copying**: `getBorrowedBooks()` returns new list
3. **Constants**: `MAX_BOOKS_ALLOWED`, `ISSUE_PERIOD_DAYS`
4. **Validation**: Input validation in all public methods
5. **Null Safety**: Null checks before operations
6. **Type Safety**: Enums instead of strings
7. **Encapsulation**: Private fields, public methods
8. **Documentation**: Comprehensive Javadoc comments
9. **Naming**: Clear, descriptive names
10. **DRY**: No code duplication

## 🧪 Testing Considerations

### Unit Testing Points:
- Individual search strategies
- Fine calculator logic
- Book availability checks
- Member borrowing limit validation
- Overdue detection

### Integration Testing Points:
- Complete issue/return workflow
- Search service with different strategies
- Notification service with observers

### Edge Cases Handled:
- Member at borrowing limit
- No available copies
- Already returned book
- Non-existent member/book
- Same-day return (no fine)

## 📈 Performance Considerations

1. **HashMap** for O(1) lookups (ISBN → Book, UserId → Member)
2. **Streams** for efficient filtering and searching
3. **Lazy Initialization** in some getters
4. **Singleton** prevents multiple system instances
5. **No premature optimization** - clear code first

## 🎓 Learning Outcomes

This design demonstrates:
- ✅ SOLID principles in practice
- ✅ Design patterns in real scenarios
- ✅ Clean code practices
- ✅ OOP concepts application
- ✅ Enterprise-level architecture
- ✅ Extensible system design
- ✅ Separation of concerns
- ✅ Domain-driven design

---

**Conclusion**: This Library Management System showcases professional software design principles in a practical, real-world application. The architecture is robust, maintainable, and ready for extension.

