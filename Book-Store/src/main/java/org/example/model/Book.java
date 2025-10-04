package org.example.model;

import org.example.enums.BookCategory;

public class Book {
    private String isbn;
    private String title;
    private String author;
    private double price;
    private BookCategory category;
    private int stock;

    public Book(String isbn, String title, String author, double price, BookCategory category, int stock) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.price = price;
        this.category = category;
        this.stock = stock;
    }

    public boolean isAvailable() {
        return stock > 0;
    }

    public void reduceStock(int quantity) {
        if (quantity <= stock) {
            stock -= quantity;
        } else {
            throw new RuntimeException("Not enough stock available.");
        }
    }

    // Getters & setters
    public String getIsbn() { 
        return isbn; 
    }
    
    public String getTitle() { 
        return title; 
    }
    
    public String getAuthor() { 
        return author; 
    }
    
    public double getPrice() { 
        return price; 
    }
    
    public BookCategory getCategory() { 
        return category; 
    }
    
    public int getStock() { 
        return stock; 
    }
}

