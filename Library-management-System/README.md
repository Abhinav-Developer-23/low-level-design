# Library Management System - Low Level Design

A comprehensive Library Management System designed using SOLID principles, design patterns, and core OOP concepts.

## 📋 Table of Contents
- [Features](#features)
- [Design Patterns Used](#design-patterns-used)
- [SOLID Principles](#solid-principles)
- [Core Components](#core-components)
- [Class Diagram Overview](#class-diagram-overview)
- [How to Run](#how-to-run)
- [Usage Examples](#usage-examples)

## ✨ Features

### User Management
- **Two user types**: Librarian and Member (Student/Faculty)
- **Librarians can**:
  - Add/remove books
  - Manage user memberships
  - View all library statistics
- **Members can**:
  - Search for books
  - Borrow up to 5 books simultaneously
  - Return books
  - Pay fines

### Book Management
- Each book has: ISBN, Title, Author, Category, Multiple Copies
- Track individual book copy status (Available, Issued, Damaged, Lost)
- Support for multiple copies of the same book

### Borrowing & Returning
- 14-day borrowing period
- Automatic fine calculation (₹10/day for overdue books)
- Issue tracking with complete audit trail
- Automatic notifications on issue and return

### Search Functionality
- Search by Title (partial match)
- Search by Author (partial match)
- Search by Category (partial match)
- Easily extensible for new search strategies

### Fine Management
- Automatic calculation of overdue fines
- Track total fines per member
- Fine payment functionality

## 🎨 Design Patterns Used

### 1. Singleton Pattern
**Used in**: `LibrarySystem`
- Ensures only one instance of the library system exists
- Thread-safe implementation with double-checked locking

```java
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
**Used in**: Search functionality and Fine calculation
- **SearchStrategy**: Allows different search algorithms (by title, author, category)
- **FineCalculator**: Enables flexible fine calculation rules

```java
interface SearchStrategy {
    List<Book> search(String query, List<Book> books);
}
```

### 3. Observer Pattern
**Used in**: Notification system
- Decouples notification mechanism from core business logic
- Supports multiple notification channels (Email, SMS)
- Easy to add new notification types

```java
interface NotificationObserver {
    void update(String message, User user);
}
```

### 4. Facade Pattern
**Used in**: `LibrarySystem`
- Provides simplified interface to complex subsystems
- Hides implementation details from clients
- Centralizes system operations

### 5. Builder Pattern (Implicit)
**Used in**: Book and IssueRecord creation
- Simplifies object creation with multiple parameters
- Ensures object consistency

## 🏗️ SOLID Principles

### Single Responsibility Principle (SRP)
Each class has one reason to change:
- `Inventory` - manages book storage
- `IssueRecordManager` - manages issue records
- `MembershipManager` - manages members
- `SearchService` - handles searching
- `NotificationService` - handles notifications

### Open/Closed Principle (OCP)
- New search strategies can be added without modifying existing code
- New notification types can be added without changing notification logic
- New fine calculation rules can be implemented without affecting existing code

### Liskov Substitution Principle (LSP)
- `Member` and `Librarian` can substitute `User` without breaking functionality
- All search strategies are interchangeable
- All notification observers are interchangeable

### Interface Segregation Principle (ISP)
- Small, focused interfaces: `SearchStrategy`, `FineCalculator`, `NotificationObserver`
- Clients depend only on methods they use

### Dependency Inversion Principle (DIP)
- High-level modules depend on abstractions (interfaces)
- `LibrarySystem` depends on `SearchStrategy` interface, not concrete implementations
- Easy to swap implementations

## 🧩 Core Components

### Models
- **User** (abstract): Base class for all users
  - **Librarian**: System administrator
  - **Member**: Library member (Student/Faculty)
- **Book**: Represents a book with multiple copies
- **BookCopy**: Individual physical copy of a book
- **IssueRecord**: Tracks book borrowing transactions

### Services
- **Inventory**: Manages book collection
- **IssueRecordManager**: Manages borrowing records
- **MembershipManager**: Manages user memberships
- **SearchService**: Handles book searches
- **NotificationService**: Manages notifications

### Strategies
- **TitleSearchStrategy**: Search by book title
- **AuthorSearchStrategy**: Search by author name
- **CategorySearchStrategy**: Search by category
- **DefaultFineCalculator**: Calculate overdue fines

### Observers
- **EmailNotificationObserver**: Send email notifications
- **SmsNotificationObserver**: Send SMS notifications

## 📊 Class Diagram Overview

```
┌─────────────┐
│    User     │ (Abstract)
└──────┬──────┘
       │
       ├────────────┬────────────┐
       │            │            │
┌──────▼──────┐ ┌──▼───────┐   │
│  Librarian  │ │  Member  │   │
└─────────────┘ └──────────┘   │
                                │
┌───────────────────────────────▼─┐
│      LibrarySystem (Facade)     │
├─────────────────────────────────┤
│ - inventory                     │
│ - issueRecordManager            │
│ - membershipManager             │
│ - searchService                 │
│ - notificationService           │
└─────────────────────────────────┘
       │
       ├──────────────┬──────────────┬──────────────┐
       │              │              │              │
┌──────▼──────┐ ┌────▼─────┐ ┌─────▼──────┐ ┌─────▼─────────┐
│  Inventory  │ │  Search  │ │Membership  │ │ IssueRecord   │
│             │ │  Service │ │  Manager   │ │    Manager    │
└─────────────┘ └──────────┘ └────────────┘ └───────────────┘
```

## 🚀 How to Run

### Prerequisites
- Java 8 or higher
- Maven (optional)

### Compile and Run

**Option 1: Using javac**
```bash
cd Library-management-System/src/main/java
javac org/example/LibraryManagementSystem.java
java org.example.LibraryManagementSystem
```

**Option 2: Using Maven**
```bash
cd Library-management-System
mvn clean compile
mvn exec:java -Dexec.mainClass="org.example.LibraryManagementSystem"
```

## 💡 Usage Examples

### 1. Add Books to Library
```java
Librarian librarian = new Librarian("LIB001", "John Smith", "john@library.com", "EMP123");
Book book = new Book("ISBN001", "Clean Code", "Robert C. Martin", BookCategory.TECHNOLOGY, 3);
librarian.addBook(book, library);
```

### 2. Register Members
```java
Member student = new Member("MEM001", "Alice", "alice@student.com", MembershipType.STUDENT);
librarian.registerMember(student, library);
```

### 3. Search for Books
```java
// Search by title
List<Book> results = library.searchByTitle("Clean");

// Search by author
List<Book> results = library.searchByAuthor("Martin");

// Search by category
List<Book> results = library.searchByCategory("TECHNOLOGY");
```

### 4. Issue and Return Books
```java
// Issue a book
library.issueBook("ISBN001", "MEM001");

// Return a book
library.returnBook("IR00001");
```

### 5. View Member Information
```java
library.displayMemberBooks("MEM001");
```

### 6. Check Overdue Books
```java
library.displayOverdueBooks();
```

## 🎯 Key Features Demonstrated

### OOP Concepts
- **Abstraction**: Abstract `User` class, interfaces for strategies
- **Encapsulation**: Private fields with public getters
- **Inheritance**: `Librarian` and `Member` extend `User`
- **Polymorphism**: Method overriding in subclasses

### Design Benefits
- **Extensibility**: Easy to add new search strategies, notification types
- **Maintainability**: Clear separation of concerns
- **Testability**: Each component can be tested independently
- **Flexibility**: Strategies can be changed at runtime

## 📝 Business Rules

1. **Borrowing Limit**: Maximum 5 books per member
2. **Loan Period**: 14 days
3. **Fine Rate**: ₹10 per day for overdue books
4. **Book Status**: Tracks Available, Issued, Damaged, Lost
5. **Notifications**: Automatic on issue and return

## 🔧 Extensibility Points

The system can be easily extended:
- Add new search strategies (e.g., by ISBN, by publication year)
- Implement different fine calculation rules (e.g., faculty vs student rates)
- Add new notification channels (e.g., push notifications, in-app)
- Introduce reservation system
- Add book recommendation engine
- Implement rating and review system

## 📄 License

This is an educational project demonstrating Low-Level Design principles.

---

**Author**: Library Management System Design  
**Purpose**: Educational demonstration of SOLID principles and design patterns  
**Date**: October 2025

