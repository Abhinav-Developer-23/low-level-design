package org.lowLevelDesign.LowLevelDesign;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

// Enums
enum BookStatus {
    AVAILABLE, RESERVED, LOANED, LOST
}

enum AccountStatus {
    ACTIVE, CLOSED, CANCELED, BLACKLISTED
}

enum ReservationStatus {
    WAITING, PENDING, COMPLETED, CANCELED
}

// Search interface
interface Search {
    public List<Book> searchByTitle(String title);

    public List<Book> searchByAuthor(String author);

    public List<Book> searchBySubject(String subject);
}

// Constants
class Constants {
    public static final int MAX_BOOKS_ISSUED_TO_USER = 5;
    public static final int MAX_LENDING_DAYS = 10;
}

// Address class
class Address {
    private String streetAddress;
    private String city;
    private String state;
    private String zipCode;
    private String country;
}

// Person class
class Person {
    private String name;
    private Address address;
    private String email;
    private String phone;
}

// Abstract Account class
abstract class Account {
    private String id;
    private String password;
    private AccountStatus status;
    private Person person;

}

// Librarian class
class Librarian extends Account {
    public boolean addBookItem(BookItem bookItem) {
        return true;
    }

    public boolean blockMember(Member member) {
        return true;
    }

    public boolean unBlockMember(Member member) {
        return true;
    }


}

// Member class
class Member extends Account {
    private Date dateOfMembership;
    private int totalBooksCheckedout;

    public boolean checkoutBookItem(BookItem bookItem) {
        if (totalBooksCheckedout >= Constants.MAX_BOOKS_ISSUED_TO_USER) {
            return false;
        }
        BookReservation reservation = BookReservation.fetchReservationDetails(bookItem.getBarcode());
        if (reservation != null && !reservation.getMemberId().equals(this.getId())) {
            return false;
        }

        if (!bookItem.checkout(this.getId())) {
            return false;
        }

        totalBooksCheckedout++;
        return true;
    }

    private String getId() {
        return "member_id";  // Simplified
    }

    public void returnBookItem(BookItem bookItem) {
        bookItem.returnBook();
        totalBooksCheckedout--;
    }

    public boolean renewBookItem(BookItem bookItem) {
        // Check if book can be renewed
        return true;
    }

}

// Book class
class Book {
    private String ISBN;
    private String title;
    private String subject;
    private String publisher;
    private String language;
    private int numberOfPages;
    private List<Author> authors;
}

// BookItem class
class BookItem extends Book {
    private String barcode;
    private boolean isReferenceOnly;
    private Date borrowed;
    private Date dueDate;
    private double price;
    private BookStatus status;
    private String placedAt;

    public boolean checkout(String memberId) {
        if (this.status != BookStatus.AVAILABLE) {
            return false;
        }

        this.status = BookStatus.LOANED;
        this.borrowed = new Date();
        this.dueDate = new Date(borrowed.getTime() + (Constants.MAX_LENDING_DAYS * 24 * 60 * 60 * 1000L));
        return true;
    }

    public void returnBook() {
        this.status = BookStatus.AVAILABLE;
    }

    public String getBarcode() {
        return barcode;
    }
}

// Author class
class Author {
    private String name;
    private String description;
}

// Rack class
class Rack {
    private int number;
    private String locationIdentifier;
}

// BookReservation class
class BookReservation {
    private Date creationDate;
    private ReservationStatus status;
    private String bookItemBarcode;
    private String memberId;

    public static BookReservation fetchReservationDetails(String barcode) {
        return null;  // In real system, fetch from database
    }

    public String getMemberId() {
        return memberId;
    }
}

// Fine class
class Fine {
    private Date creationDate;
    private double amount;
    private String bookItemBarcode;
    private String memberId;

    public static void collectFine(String memberId, double amount) {
        // Implementation
    }
}

// Library class
class Library {
    private String name;
    private Address address;
    private List<BookItem> books;
    private List<Account> accounts;

    public void addBookItem(BookItem bookItem) {
        books.add(bookItem);
    }

    public void registerAccount(Account account) {
        accounts.add(account);
    }

    public List<BookItem> searchByTitle(String title) {
        // Implementation
        return new ArrayList<>();
    }

    public List<BookItem> searchByAuthor(String author) {
        // Implementation
        return new ArrayList<>();
    }

    public List<BookItem> searchBySubject(String subject) {
        // Implementation
        return new ArrayList<>();
    }
}

// Catalog class
class Catalog implements Search {
    private HashMap<String, List<Book>> bookTitles;
    private HashMap<String, List<Book>> bookAuthors;
    private HashMap<String, List<Book>> bookSubjects;
    private HashMap<String, List<Book>> bookPublicationDates;

    @Override
    public List<Book> searchByTitle(String title) {
        return bookTitles.get(title);
    }

    @Override
    public List<Book> searchByAuthor(String author) {
        return bookAuthors.get(author);
    }

    @Override
    public List<Book> searchBySubject(String subject) {
        return bookSubjects.get(subject);
    }
}