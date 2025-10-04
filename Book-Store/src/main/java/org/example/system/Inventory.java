package org.example.system;

import org.example.model.Book;
import java.util.*;

public class Inventory {
    private static Inventory instance;
    private Map<String, Book> books;

    private Inventory() {
        books = new HashMap<>();
    }

    public static Inventory getInstance() {
        if (instance == null) {
            instance = new Inventory();
        }
        return instance;
    }

    public void addBook(Book book) {
        books.put(book.getIsbn(), book);
    }

    public Book getBookByISBN(String isbn) {
        return books.get(isbn);
    }

    public List<Book> searchByTitle(String title) {
        List<Book> results = new ArrayList<>();
        for (Book book : books.values()) {
            if (book.getTitle().toLowerCase().contains(title.toLowerCase())) {
                results.add(book);
            }
        }
        return results;
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(books.values());
    }
}

