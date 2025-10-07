package org.example;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

// ========================== ENUMS ==========================

/**
 * Enum representing user roles in the system
 */
enum UserRole {
    LIBRARIAN,
    MEMBER
}

/**
 * Enum representing book categories
 */
enum BookCategory {
    FICTION,
    NON_FICTION,
    SCIENCE,
    TECHNOLOGY,
    HISTORY,
    BIOGRAPHY,
    CHILDREN,
    REFERENCE
}

/**
 * Enum representing the status of a book copy
 */
enum BookCopyStatus {
    AVAILABLE,
    ISSUED,
    DAMAGED,
    LOST
}

/**
 * Enum representing membership type
 */
enum MembershipType {
    STUDENT,
    FACULTY
}

// ========================== INTERFACES ==========================

/**
 * Strategy interface for searching books
 * Follows Strategy Pattern for flexible search implementations
 */
interface SearchStrategy {
    List<Book> search(String query, List<Book> books);
}

/**
 * Interface for fine calculation
 * Follows Strategy Pattern for different fine calculation rules
 */
interface FineCalculator {
    double calculateFine(LocalDate dueDate, LocalDate returnDate);
}

/**
 * Observer interface for notifications
 * Follows Observer Pattern for event-based notifications
 */
interface NotificationObserver {
    void update(String message, User user);
}

// ========================== MODELS ==========================

/**
 * Abstract base class for all users in the system
 * Demonstrates Abstraction and Encapsulation
 */
abstract class User {
    protected String userId;
    protected String name;
    protected String email;
    protected UserRole role;

    public User(String userId, String name, String email, UserRole role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    // Getters
    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public UserRole getRole() { return role; }

    /**
     * Abstract method to be implemented by subclasses
     * Demonstrates Polymorphism
     */
    public abstract void displayUserInfo();
}

/**
 * Librarian class - can manage books and memberships
 * Demonstrates Inheritance
 */
class Librarian extends User {
    private String employeeId;

    public Librarian(String userId, String name, String email, String employeeId) {
        super(userId, name, email, UserRole.LIBRARIAN);
        this.employeeId = employeeId;
    }

    public String getEmployeeId() { return employeeId; }

    @Override
    public void displayUserInfo() {
        System.out.println("Librarian: " + name + " (ID: " + userId + ", Employee ID: " + employeeId + ")");
    }

    /**
     * Add a new book to the library
     */
    public void addBook(Book book, LibrarySystem library) {
        library.addBook(book);
        System.out.println("Librarian " + name + " added book: " + book.getTitle());
    }

    /**
     * Remove a book from the library
     */
    public void removeBook(String isbn, LibrarySystem library) {
        library.removeBook(isbn);
        System.out.println("Librarian " + name + " removed book with ISBN: " + isbn);
    }

    /**
     * Register a new member
     */
    public void registerMember(Member member, LibrarySystem library) {
        library.registerMember(member);
        System.out.println("Librarian " + name + " registered member: " + member.getName());
    }
}

/**
 * Member class - can borrow and return books
 * Demonstrates Inheritance
 */
class Member extends User {
    private MembershipType membershipType;
    private LocalDate membershipDate;
    private List<IssueRecord> borrowedBooks;
    private double totalFines;
    private static final int MAX_BOOKS_ALLOWED = 5;

    public Member(String userId, String name, String email, MembershipType membershipType) {
        super(userId, name, email, UserRole.MEMBER);
        this.membershipType = membershipType;
        this.membershipDate = LocalDate.now();
        this.borrowedBooks = new ArrayList<>();
        this.totalFines = 0.0;
    }

    // Getters
    public MembershipType getMembershipType() { return membershipType; }
    public LocalDate getMembershipDate() { return membershipDate; }
    public List<IssueRecord> getBorrowedBooks() { return new ArrayList<>(borrowedBooks); }
    public double getTotalFines() { return totalFines; }

    @Override
    public void displayUserInfo() {
        System.out.println("Member: " + name + " (" + membershipType + ", ID: " + userId + ")");
        System.out.println("Books borrowed: " + borrowedBooks.size() + "/" + MAX_BOOKS_ALLOWED);
        System.out.println("Total fines: ₹" + totalFines);
    }

    /**
     * Check if member can borrow more books
     */
    public boolean canBorrowMoreBooks() {
        return borrowedBooks.size() < MAX_BOOKS_ALLOWED;
    }

    /**
     * Add an issue record when book is borrowed
     */
    public void addIssueRecord(IssueRecord record) {
        borrowedBooks.add(record);
    }

    /**
     * Remove issue record when book is returned
     */
    public void removeIssueRecord(IssueRecord record) {
        borrowedBooks.remove(record);
    }

    /**
     * Add fine to member's account
     */
    public void addFine(double fine) {
        this.totalFines += fine;
    }

    /**
     * Pay outstanding fines
     */
    public void payFine(double amount) {
        if (amount <= totalFines) {
            totalFines -= amount;
            System.out.println("Fine of ₹" + amount + " paid. Remaining: ₹" + totalFines);
        } else {
            System.out.println("Amount exceeds total fines.");
        }
    }
}

/**
 * Book class representing a book in the library
 * Demonstrates Encapsulation
 */
class Book {
    private String isbn;
    private String title;
    private String author;
    private BookCategory category;
    private List<BookCopy> copies;

    public Book(String isbn, String title, String author, BookCategory category, int copyCount) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.category = category;
        this.copies = new ArrayList<>();
        
        // Create book copies
        for (int i = 0; i < copyCount; i++) {
            copies.add(new BookCopy(isbn + "-" + (i + 1), this));
        }
    }

    // Getters
    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public BookCategory getCategory() { return category; }
    public List<BookCopy> getCopies() { return new ArrayList<>(copies); }

    /**
     * Get number of available copies
     */
    public int getAvailableCopyCount() {
        return (int) copies.stream()
                .filter(copy -> copy.getStatus() == BookCopyStatus.AVAILABLE)
                .count();
    }

    /**
     * Get total number of copies
     */
    public int getTotalCopyCount() {
        return copies.size();
    }

    /**
     * Get an available copy
     */
    public BookCopy getAvailableCopy() {
        return copies.stream()
                .filter(copy -> copy.getStatus() == BookCopyStatus.AVAILABLE)
                .findFirst()
                .orElse(null);
    }

    /**
     * Add a new copy
     */
    public void addCopy() {
        copies.add(new BookCopy(isbn + "-" + (copies.size() + 1), this));
    }

    /**
     * Remove a copy
     */
    public void removeCopy(BookCopy copy) {
        copies.remove(copy);
    }

    @Override
    public String toString() {
        return "Book{" +
                "ISBN='" + isbn + '\'' +
                ", Title='" + title + '\'' +
                ", Author='" + author + '\'' +
                ", Category=" + category +
                ", Available=" + getAvailableCopyCount() +
                ", Total=" + getTotalCopyCount() +
                '}';
    }
}

/**
 * BookCopy class representing individual copies of a book
 * Allows tracking of each physical copy
 */
class BookCopy {
    private String copyId;
    private Book book;
    private BookCopyStatus status;

    public BookCopy(String copyId, Book book) {
        this.copyId = copyId;
        this.book = book;
        this.status = BookCopyStatus.AVAILABLE;
    }

    // Getters and Setters
    public String getCopyId() { return copyId; }
    public Book getBook() { return book; }
    public BookCopyStatus getStatus() { return status; }
    public void setStatus(BookCopyStatus status) { this.status = status; }

    @Override
    public String toString() {
        return "BookCopy{" +
                "CopyId='" + copyId + '\'' +
                ", Status=" + status +
                '}';
    }
}

/**
 * IssueRecord class to track book borrowing
 * Maintains complete audit trail
 */
class IssueRecord {
    private String recordId;
    private Member member;
    private BookCopy bookCopy;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private double fine;
    private static final int ISSUE_PERIOD_DAYS = 14;

    public IssueRecord(String recordId, Member member, BookCopy bookCopy) {
        this.recordId = recordId;
        this.member = member;
        this.bookCopy = bookCopy;
        this.issueDate = LocalDate.now();
        this.dueDate = issueDate.plusDays(ISSUE_PERIOD_DAYS);
        this.returnDate = null;
        this.fine = 0.0;
    }

    // Getters
    public String getRecordId() { return recordId; }
    public Member getMember() { return member; }
    public BookCopy getBookCopy() { return bookCopy; }
    public LocalDate getIssueDate() { return issueDate; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public double getFine() { return fine; }

    /**
     * Set return date and calculate fine if overdue
     */
    public void setReturnDate(LocalDate returnDate, FineCalculator calculator) {
        this.returnDate = returnDate;
        if (returnDate.isAfter(dueDate)) {
            this.fine = calculator.calculateFine(dueDate, returnDate);
        }
    }

    /**
     * Check if book is overdue
     */
    public boolean isOverdue() {
        return returnDate == null && LocalDate.now().isAfter(dueDate);
    }

    /**
     * Get days overdue
     */
    public long getDaysOverdue() {
        if (returnDate != null && returnDate.isAfter(dueDate)) {
            return ChronoUnit.DAYS.between(dueDate, returnDate);
        } else if (LocalDate.now().isAfter(dueDate)) {
            return ChronoUnit.DAYS.between(dueDate, LocalDate.now());
        }
        return 0;
    }

    @Override
    public String toString() {
        return "IssueRecord{" +
                "RecordId='" + recordId + '\'' +
                ", Book='" + bookCopy.getBook().getTitle() + '\'' +
                ", IssueDate=" + issueDate +
                ", DueDate=" + dueDate +
                ", ReturnDate=" + (returnDate != null ? returnDate : "Not returned") +
                ", Fine=₹" + fine +
                '}';
    }
}

// ========================== STRATEGY IMPLEMENTATIONS ==========================

/**
 * Search by book title
 * Implements Strategy Pattern
 */
class TitleSearchStrategy implements SearchStrategy {
    @Override
    public List<Book> search(String query, List<Book> books) {
        return books.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }
}

/**
 * Search by author name
 * Implements Strategy Pattern
 */
class AuthorSearchStrategy implements SearchStrategy {
    @Override
    public List<Book> search(String query, List<Book> books) {
        return books.stream()
                .filter(book -> book.getAuthor().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }
}

/**
 * Search by category
 * Implements Strategy Pattern
 */
class CategorySearchStrategy implements SearchStrategy {
    @Override
    public List<Book> search(String query, List<Book> books) {
        return books.stream()
                .filter(book -> book.getCategory().name().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }
}

/**
 * Default fine calculator - ₹10 per day
 * Implements Strategy Pattern for fine calculation
 */
class DefaultFineCalculator implements FineCalculator {
    private static final double FINE_PER_DAY = 10.0;

    @Override
    public double calculateFine(LocalDate dueDate, LocalDate returnDate) {
        long daysOverdue = ChronoUnit.DAYS.between(dueDate, returnDate);
        return daysOverdue > 0 ? daysOverdue * FINE_PER_DAY : 0.0;
    }
}

// ========================== OBSERVER IMPLEMENTATIONS ==========================

/**
 * Email notification observer
 * Implements Observer Pattern
 */
class EmailNotificationObserver implements NotificationObserver {
    @Override
    public void update(String message, User user) {
        System.out.println("[EMAIL to " + user.getEmail() + "]: " + message);
    }
}

/**
 * SMS notification observer
 * Implements Observer Pattern
 */
class SmsNotificationObserver implements NotificationObserver {
    @Override
    public void update(String message, User user) {
        System.out.println("[SMS to " + user.getName() + "]: " + message);
    }
}

// ========================== SERVICES ==========================

/**
 * SearchService to handle book searches
 * Demonstrates Single Responsibility Principle
 */
class SearchService {
    private SearchStrategy strategy;

    public void setStrategy(SearchStrategy strategy) {
        this.strategy = strategy;
    }

    public List<Book> executeSearch(String query, List<Book> books) {
        if (strategy == null) {
            throw new IllegalStateException("Search strategy not set");
        }
        return strategy.search(query, books);
    }
}

/**
 * NotificationService to handle notifications
 * Implements Observer Pattern and Single Responsibility Principle
 */
class NotificationService {
    private List<NotificationObserver> observers;

    public NotificationService() {
        this.observers = new ArrayList<>();
    }

    public void addObserver(NotificationObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(NotificationObserver observer) {
        observers.remove(observer);
    }

    public void notifyUser(String message, User user) {
        for (NotificationObserver observer : observers) {
            observer.update(message, user);
        }
    }
}

/**
 * Inventory to manage books
 * Demonstrates Single Responsibility Principle
 */
class Inventory {
    private Map<String, Book> books; // ISBN -> Book

    public Inventory() {
        this.books = new HashMap<>();
    }

    /**
     * Add a book to inventory
     */
    public void addBook(Book book) {
        books.put(book.getIsbn(), book);
    }

    /**
     * Remove a book from inventory
     */
    public void removeBook(String isbn) {
        books.remove(isbn);
    }

    /**
     * Get a book by ISBN
     */
    public Book getBook(String isbn) {
        return books.get(isbn);
    }

    /**
     * Get all books
     */
    public List<Book> getAllBooks() {
        return new ArrayList<>(books.values());
    }

    /**
     * Check if book exists
     */
    public boolean hasBook(String isbn) {
        return books.containsKey(isbn);
    }

    /**
     * Get available book copy
     */
    public BookCopy getAvailableBookCopy(String isbn) {
        Book book = books.get(isbn);
        return book != null ? book.getAvailableCopy() : null;
    }
}

/**
 * IssueRecordManager to manage issue records
 * Demonstrates Single Responsibility Principle
 */
class IssueRecordManager {
    private Map<String, IssueRecord> issueRecords; // RecordId -> IssueRecord
    private int recordCounter;

    public IssueRecordManager() {
        this.issueRecords = new HashMap<>();
        this.recordCounter = 1;
    }

    /**
     * Create a new issue record
     */
    public IssueRecord createIssueRecord(Member member, BookCopy bookCopy) {
        String recordId = "IR" + String.format("%05d", recordCounter++);
        IssueRecord record = new IssueRecord(recordId, member, bookCopy);
        issueRecords.put(recordId, record);
        return record;
    }

    /**
     * Get issue record by ID
     */
    public IssueRecord getIssueRecord(String recordId) {
        return issueRecords.get(recordId);
    }

    /**
     * Get all issue records for a member
     */
    public List<IssueRecord> getMemberIssueRecords(Member member) {
        return issueRecords.values().stream()
                .filter(record -> record.getMember().equals(member) && record.getReturnDate() == null)
                .collect(Collectors.toList());
    }

    /**
     * Get all overdue records
     */
    public List<IssueRecord> getOverdueRecords() {
        return issueRecords.values().stream()
                .filter(IssueRecord::isOverdue)
                .collect(Collectors.toList());
    }

    /**
     * Get all issue records
     */
    public List<IssueRecord> getAllIssueRecords() {
        return new ArrayList<>(issueRecords.values());
    }
}

/**
 * MembershipManager to manage members
 * Demonstrates Single Responsibility Principle
 */
class MembershipManager {
    private Map<String, Member> members; // UserId -> Member

    public MembershipManager() {
        this.members = new HashMap<>();
    }

    /**
     * Register a new member
     */
    public void registerMember(Member member) {
        members.put(member.getUserId(), member);
    }

    /**
     * Remove a member
     */
    public void removeMember(String userId) {
        members.remove(userId);
    }

    /**
     * Get member by ID
     */
    public Member getMember(String userId) {
        return members.get(userId);
    }

    /**
     * Get all members
     */
    public List<Member> getAllMembers() {
        return new ArrayList<>(members.values());
    }

    /**
     * Check if member exists
     */
    public boolean hasMember(String userId) {
        return members.containsKey(userId);
    }
}

// ========================== MAIN SYSTEM ==========================

/**
 * LibrarySystem - Main facade for the library management system
 * Implements Facade Pattern to provide a unified interface
 * Demonstrates Single Responsibility and Open/Closed Principles
 */
class LibrarySystem {
    private static LibrarySystem instance; // Singleton instance
    private Inventory inventory;
    private IssueRecordManager issueRecordManager;
    private MembershipManager membershipManager;
    private SearchService searchService;
    private NotificationService notificationService;
    private FineCalculator fineCalculator;

    /**
     * Private constructor for Singleton pattern
     */
    private LibrarySystem() {
        this.inventory = new Inventory();
        this.issueRecordManager = new IssueRecordManager();
        this.membershipManager = new MembershipManager();
        this.searchService = new SearchService();
        this.notificationService = new NotificationService();
        this.fineCalculator = new DefaultFineCalculator();
        
        // Add default observers
        notificationService.addObserver(new EmailNotificationObserver());
        notificationService.addObserver(new SmsNotificationObserver());
    }

    /**
     * Get singleton instance
     * Implements Singleton Pattern
     */
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

    // ========================== Book Management ==========================

    /**
     * Add a new book to the library
     */
    public void addBook(Book book) {
        inventory.addBook(book);
        System.out.println("✓ Book added: " + book.getTitle());
    }

    /**
     * Remove a book from the library
     */
    public void removeBook(String isbn) {
        if (inventory.hasBook(isbn)) {
            inventory.removeBook(isbn);
            System.out.println("✓ Book removed with ISBN: " + isbn);
        } else {
            System.out.println("✗ Book not found with ISBN: " + isbn);
        }
    }

    /**
     * Get a book by ISBN
     */
    public Book getBook(String isbn) {
        return inventory.getBook(isbn);
    }

    /**
     * Get all books
     */
    public List<Book> getAllBooks() {
        return inventory.getAllBooks();
    }

    // ========================== Member Management ==========================

    /**
     * Register a new member
     */
    public void registerMember(Member member) {
        membershipManager.registerMember(member);
        System.out.println("✓ Member registered: " + member.getName());
    }

    /**
     * Get member by ID
     */
    public Member getMember(String userId) {
        return membershipManager.getMember(userId);
    }

    /**
     * Get all members
     */
    public List<Member> getAllMembers() {
        return membershipManager.getAllMembers();
    }

    // ========================== Search Operations ==========================

    /**
     * Search books by title
     */
    public List<Book> searchByTitle(String title) {
        searchService.setStrategy(new TitleSearchStrategy());
        return searchService.executeSearch(title, inventory.getAllBooks());
    }

    /**
     * Search books by author
     */
    public List<Book> searchByAuthor(String author) {
        searchService.setStrategy(new AuthorSearchStrategy());
        return searchService.executeSearch(author, inventory.getAllBooks());
    }

    /**
     * Search books by category
     */
    public List<Book> searchByCategory(String category) {
        searchService.setStrategy(new CategorySearchStrategy());
        return searchService.executeSearch(category, inventory.getAllBooks());
    }

    // ========================== Borrow & Return Operations ==========================

    /**
     * Issue a book to a member
     */
    public boolean issueBook(String isbn, String memberId) {
        Member member = membershipManager.getMember(memberId);
        if (member == null) {
            System.out.println("✗ Member not found with ID: " + memberId);
            return false;
        }

        if (!member.canBorrowMoreBooks()) {
            System.out.println("✗ Member has reached maximum borrowing limit (5 books)");
            return false;
        }

        BookCopy availableCopy = inventory.getAvailableBookCopy(isbn);
        if (availableCopy == null) {
            System.out.println("✗ No available copies of the book with ISBN: " + isbn);
            return false;
        }

        // Create issue record
        IssueRecord record = issueRecordManager.createIssueRecord(member, availableCopy);
        
        // Update book copy status
        availableCopy.setStatus(BookCopyStatus.ISSUED);
        
        // Add record to member
        member.addIssueRecord(record);

        // Send notification
        String message = "Book '" + availableCopy.getBook().getTitle() + 
                        "' has been issued. Due date: " + record.getDueDate();
        notificationService.notifyUser(message, member);

        System.out.println("✓ Book issued successfully!");
        System.out.println("  Record ID: " + record.getRecordId());
        System.out.println("  Due Date: " + record.getDueDate());

        return true;
    }

    /**
     * Return a book
     */
    public boolean returnBook(String recordId) {
        IssueRecord record = issueRecordManager.getIssueRecord(recordId);
        if (record == null) {
            System.out.println("✗ Issue record not found with ID: " + recordId);
            return false;
        }

        if (record.getReturnDate() != null) {
            System.out.println("✗ Book has already been returned");
            return false;
        }

        // Set return date and calculate fine
        record.setReturnDate(LocalDate.now(), fineCalculator);

        // Update book copy status
        record.getBookCopy().setStatus(BookCopyStatus.AVAILABLE);

        // Remove record from member
        record.getMember().removeIssueRecord(record);

        // Add fine if applicable
        if (record.getFine() > 0) {
            record.getMember().addFine(record.getFine());
            String message = "Book '" + record.getBookCopy().getBook().getTitle() + 
                            "' returned late. Fine: ₹" + record.getFine();
            notificationService.notifyUser(message, record.getMember());
            System.out.println("⚠ Book returned with fine: ₹" + record.getFine());
        } else {
            String message = "Book '" + record.getBookCopy().getBook().getTitle() + 
                            "' returned successfully. Thank you!";
            notificationService.notifyUser(message, record.getMember());
            System.out.println("✓ Book returned successfully!");
        }

        return true;
    }

    // ========================== Report Operations ==========================

    /**
     * Get overdue books
     */
    public List<IssueRecord> getOverdueBooks() {
        return issueRecordManager.getOverdueRecords();
    }

    /**
     * Display all overdue books
     */
    public void displayOverdueBooks() {
        List<IssueRecord> overdueRecords = getOverdueBooks();
        if (overdueRecords.isEmpty()) {
            System.out.println("No overdue books.");
            return;
        }

        System.out.println("\n=== OVERDUE BOOKS ===");
        for (IssueRecord record : overdueRecords) {
            System.out.println(record);
            System.out.println("  Days Overdue: " + record.getDaysOverdue());
        }
    }

    /**
     * Display member's borrowed books
     */
    public void displayMemberBooks(String memberId) {
        Member member = membershipManager.getMember(memberId);
        if (member == null) {
            System.out.println("✗ Member not found with ID: " + memberId);
            return;
        }

        member.displayUserInfo();
        List<IssueRecord> records = issueRecordManager.getMemberIssueRecords(member);
        
        if (records.isEmpty()) {
            System.out.println("No books currently borrowed.");
            return;
        }

        System.out.println("\nBorrowed Books:");
        for (IssueRecord record : records) {
            System.out.println("  - " + record);
        }
    }

    /**
     * Display library statistics
     */
    public void displayStatistics() {
        System.out.println("\n=== LIBRARY STATISTICS ===");
        System.out.println("Total Books: " + inventory.getAllBooks().size());
        System.out.println("Total Members: " + membershipManager.getAllMembers().size());
        System.out.println("Total Issue Records: " + issueRecordManager.getAllIssueRecords().size());
        System.out.println("Overdue Books: " + issueRecordManager.getOverdueRecords().size());
        
        int totalCopies = inventory.getAllBooks().stream()
                .mapToInt(Book::getTotalCopyCount)
                .sum();
        int availableCopies = inventory.getAllBooks().stream()
                .mapToInt(Book::getAvailableCopyCount)
                .sum();
        
        System.out.println("Total Book Copies: " + totalCopies);
        System.out.println("Available Copies: " + availableCopies);
        System.out.println("Issued Copies: " + (totalCopies - availableCopies));
    }
}

// ========================== MAIN CLASS ==========================

/**
 * Main class to demonstrate the Library Management System
 */
public class LibraryManagementSystem {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("   LIBRARY MANAGEMENT SYSTEM DEMO");
        System.out.println("========================================\n");

        // Get library system instance (Singleton)
        LibrarySystem library = LibrarySystem.getInstance();

        // Create librarian
        Librarian librarian = new Librarian("LIB001", "John Smith", "john@library.com", "EMP123");
        
        // Create members
        Member student1 = new Member("MEM001", "Alice Johnson", "alice@student.com", MembershipType.STUDENT);
        Member student2 = new Member("MEM002", "Bob Williams", "bob@student.com", MembershipType.STUDENT);
        Member faculty = new Member("MEM003", "Dr. Carol Brown", "carol@faculty.com", MembershipType.FACULTY);

        System.out.println(">>> Registering Members...\n");
        librarian.registerMember(student1, library);
        librarian.registerMember(student2, library);
        librarian.registerMember(faculty, library);

        // Create and add books
        System.out.println("\n>>> Adding Books to Library...\n");
        Book book1 = new Book("ISBN001", "Clean Code", "Robert C. Martin", BookCategory.TECHNOLOGY, 3);
        Book book2 = new Book("ISBN002", "Design Patterns", "Gang of Four", BookCategory.TECHNOLOGY, 2);
        Book book3 = new Book("ISBN003", "The Hobbit", "J.R.R. Tolkien", BookCategory.FICTION, 5);
        Book book4 = new Book("ISBN004", "Sapiens", "Yuval Noah Harari", BookCategory.HISTORY, 2);
        Book book5 = new Book("ISBN005", "Introduction to Algorithms", "CLRS", BookCategory.TECHNOLOGY, 4);

        librarian.addBook(book1, library);
        librarian.addBook(book2, library);
        librarian.addBook(book3, library);
        librarian.addBook(book4, library);
        librarian.addBook(book5, library);

        // Display library statistics
        library.displayStatistics();

        // Search operations
        System.out.println("\n>>> Searching Books...\n");
        
        System.out.println("Search by Title 'Clean':");
        List<Book> titleResults = library.searchByTitle("Clean");
        titleResults.forEach(System.out::println);

        System.out.println("\nSearch by Author 'Tolkien':");
        List<Book> authorResults = library.searchByAuthor("Tolkien");
        authorResults.forEach(System.out::println);

        System.out.println("\nSearch by Category 'TECHNOLOGY':");
        List<Book> categoryResults = library.searchByCategory("TECHNOLOGY");
        categoryResults.forEach(System.out::println);

        // Issue books
        System.out.println("\n>>> Issuing Books...\n");
        library.issueBook("ISBN001", "MEM001"); // Alice borrows Clean Code
        library.issueBook("ISBN002", "MEM001"); // Alice borrows Design Patterns
        library.issueBook("ISBN003", "MEM002"); // Bob borrows The Hobbit
        library.issueBook("ISBN004", "MEM003"); // Carol borrows Sapiens
        library.issueBook("ISBN005", "MEM001"); // Alice borrows Algorithms

        // Display member's books
        System.out.println("\n>>> Member's Borrowed Books...\n");
        library.displayMemberBooks("MEM001");

        // Try to issue more books than limit
        System.out.println("\n>>> Testing Borrowing Limit...\n");
        library.issueBook("ISBN003", "MEM001"); // Alice tries to borrow 6th book (should fail)

        // Display statistics after issuing
        library.displayStatistics();

        // Return books
        System.out.println("\n>>> Returning Books...\n");
        List<IssueRecord> aliceRecords = student1.getBorrowedBooks();
        if (!aliceRecords.isEmpty()) {
            library.returnBook(aliceRecords.get(0).getRecordId());
        }

        // Display member info after return
        System.out.println("\n>>> Member Info After Return...\n");
        student1.displayUserInfo();

        // Simulate overdue scenario (for demonstration)
        System.out.println("\n>>> Simulating Overdue Scenario...\n");
        System.out.println("(In a real system, books would become overdue after 14 days)");
        library.displayOverdueBooks();

        // Display all books
        System.out.println("\n>>> All Books in Library...\n");
        List<Book> allBooks = library.getAllBooks();
        allBooks.forEach(System.out::println);

        // Final statistics
        System.out.println("\n>>> Final Statistics...\n");
        library.displayStatistics();

        // Display user info
        System.out.println("\n>>> User Information...\n");
        librarian.displayUserInfo();
        student1.displayUserInfo();
        faculty.displayUserInfo();

        System.out.println("\n========================================");
        System.out.println("   DEMO COMPLETED SUCCESSFULLY!");
        System.out.println("========================================");
    }
}

