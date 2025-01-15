package org.lowLevelDesign.DesignPatterns;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class IteratorDesignPattern {


    // The "Iterator" interface that defines the methods for traversing the collection.
    interface Iterator {
        boolean hasNext();

        Object next();
    }

    // The "Book" class that we will store in our collection.
    @Getter
    static class Book {
        private String title;

        public Book(String title) {
            this.title = title;
        }

    }

    // The "BookIterator" class implements the Iterator interface and provides the actual traversal logic.
    class BookIterator implements Iterator {
        private List<Book> books;
        private int position = 0;

        public BookIterator(List<Book> books) {
            this.books = books;
        }

        @Override
        public boolean hasNext() {
            return position < books.size();
        }

        @Override
        public Object next() {
            if (this.hasNext()) {
                return books.get(position++);
            }
            return null; // Or throw an exception (e.g., NoSuchElementException)
        }
    }

    // The "BookCollection" class which holds the list of books.
    class BookCollection {
        private List<Book> books;

        public BookCollection() {
            this.books = new ArrayList<>();
        }

        public void addBook(Book book) {
            books.add(book);
        }

        // This method returns the iterator for the BookCollection
        public Iterator createIterator() {
            return new BookIterator(books);
        }
    }

    // The client code that demonstrates the usage of the iterator pattern.
    public class IteratorPatternExample {
        public void main(String[] args) {
            // Create a collection of books
            BookCollection bookCollection = new BookCollection();
            bookCollection.addBook(new Book("Design Patterns"));
            bookCollection.addBook(new Book("Clean Code"));
            bookCollection.addBook(new Book("Effective Java"));

            // Create the iterator
            Iterator iterator = bookCollection.createIterator();

            // Iterate through the collection and print book titles
            while (iterator.hasNext()) {
                Book book = (Book) iterator.next();  // Cast because next() returns Object
                System.out.println("Book Title: " + book.getTitle());
            }
        }
    }

}
