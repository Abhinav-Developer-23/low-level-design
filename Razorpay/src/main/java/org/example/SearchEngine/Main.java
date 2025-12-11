package org.example.SearchEngine;

import org.example.SearchEngine.enums.SortOrder;
import org.example.SearchEngine.exception.DatasetAlreadyExistsException;
import org.example.SearchEngine.exception.DatasetNotFoundException;
import org.example.SearchEngine.exception.DocumentNotFoundException;
import org.example.SearchEngine.model.Dataset;
import org.example.SearchEngine.model.Document;
import org.example.SearchEngine.model.SearchResult;

import java.util.List;

/**
 * Main class demonstrating the Search Engine functionality
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== Tech Blog Search Engine Demo ===\n");

        // Initialize the search engine
        SearchEngine searchEngine = new SearchEngine();

        // Demo 1: Create datasets
        System.out.println("1. Creating datasets...");
        Dataset techBlog = searchEngine.createDataset("tech-blog");
        System.out.println("   Created dataset: " + techBlog.getName());

        Dataset newsBlog = searchEngine.createDataset("news-blog");
        System.out.println("   Created dataset: " + newsBlog.getName());
        System.out.println();

        // Demo 2: Add documents to dataset
        System.out.println("2. Adding documents to tech-blog dataset...");
        Document doc1 = searchEngine.addDocument("tech-blog", 
            "Introduction to Microservices Architecture: Learn how microservices can scale your application");
        System.out.println("   Added: " + doc1.getContent());

        Document doc2 = searchEngine.addDocument("tech-blog",
            "Database Optimization Techniques: Improve your database performance with indexing");
        System.out.println("   Added: " + doc2.getContent());

        Document doc3 = searchEngine.addDocument("tech-blog",
            "Kubernetes Best Practices: Deploy and scale microservices efficiently");
        System.out.println("   Added: " + doc3.getContent());

        Document doc4 = searchEngine.addDocument("tech-blog",
            "Understanding Docker Containers: Containerization basics for beginners");
        System.out.println("   Added: " + doc4.getContent());

        Document doc5 = searchEngine.addDocument("tech-blog",
            "RESTful API Design Principles: Build scalable and maintainable APIs");
        System.out.println("   Added: " + doc5.getContent());
        System.out.println();

        // Demo 3: Search with default sorting (Relevance Descending)
        System.out.println("3. Searching for 'microservices' (default sorting - relevance)...");
        List<SearchResult> results1 = searchEngine.search("tech-blog", "microservices");
        printSearchResults(results1);
        System.out.println();

        // Demo 4: Search with different sorting - Alphabetical
        System.out.println("4. Searching for 'scale' (alphabetical ascending)...");
        List<SearchResult> results2 = searchEngine.search("tech-blog", "scale", SortOrder.ALPHABETICAL_ASC);
        printSearchResults(results2);
        System.out.println();

        // Demo 5: Search with case-insensitive pattern
        System.out.println("5. Searching for 'DATABASE' (case-insensitive)...");
        List<SearchResult> results3 = searchEngine.search("tech-blog", "DATABASE");
        printSearchResults(results3);
        System.out.println();

        // Demo 6: Delete a document
        System.out.println("6. Deleting document: " + doc4.getId());
        searchEngine.deleteDocument("tech-blog", doc4.getId());
        System.out.println("   Document deleted successfully");
        System.out.println();

        // Demo 7: Search after deletion
        System.out.println("7. Searching for 'Docker' after deletion...");
        List<SearchResult> results4 = searchEngine.search("tech-blog", "Docker");
        if (results4.isEmpty()) {
            System.out.println("   No results found (document was deleted)");
        } else {
            printSearchResults(results4);
        }
        System.out.println();

        // Demo 8: Search with timestamp sorting
        System.out.println("8. Searching for 'API' with timestamp sorting (newest first)...");
        List<SearchResult> results5 = searchEngine.search("tech-blog", ".*", SortOrder.TIMESTAMP_DESC);
        printSearchResults(results5);
        System.out.println();

        // Demo 9: List all datasets
        System.out.println("9. Listing all datasets...");
        List<Dataset> datasets = searchEngine.getAllDatasets();
        for (Dataset dataset : datasets) {
            System.out.println("   Dataset: " + dataset.getName() + 
                             " (Documents: " + dataset.getDocumentCount() + ")");
        }
        System.out.println();

        // Demo 10: Exception handling
        System.out.println("10. Exception Handling Demo:");
        
        // Try to create duplicate dataset
        try {
            searchEngine.createDataset("tech-blog");
        } catch (DatasetAlreadyExistsException e) {
            System.out.println("   ✓ Caught expected exception: " + e.getMessage());
        }

        // Try to access non-existent dataset
        try {
            searchEngine.search("non-existent-dataset", "test");
        } catch (DatasetNotFoundException e) {
            System.out.println("   ✓ Caught expected exception: " + e.getMessage());
        }

        // Try to delete non-existent document
        try {
            searchEngine.deleteDocument("tech-blog", "non-existent-id");
        } catch (DocumentNotFoundException e) {
            System.out.println("   ✓ Caught expected exception: " + e.getMessage());
        }
        System.out.println();

        // Demo 11: Complex search patterns with regex
        System.out.println("11. Advanced search with regex pattern 'Micro.*Architecture'...");
        List<SearchResult> results6 = searchEngine.search("tech-blog", "Micro.*Architecture");
        printSearchResults(results6);
        System.out.println();

        // Demo 12: Delete dataset
        System.out.println("12. Deleting dataset: news-blog");
        searchEngine.deleteDataset("news-blog");
        System.out.println("   Dataset deleted successfully");
        System.out.println();

        System.out.println("=== Demo Complete ===");
        System.out.println("\nDesign Patterns Used:");
        System.out.println("✓ Repository Pattern - DatasetRepository");
        System.out.println("✓ Strategy Pattern - Sort strategies for flexible sorting");
        System.out.println("✓ Factory Pattern - SortStrategyFactory");
        System.out.println("✓ Facade Pattern - SearchEngine as simplified interface");
        System.out.println("\nSOLID Principles Applied:");
        System.out.println("✓ Single Responsibility - Each class has one clear purpose");
        System.out.println("✓ Open/Closed - New sort strategies can be added without modifying existing code");
        System.out.println("✓ Liskov Substitution - All sort strategies are interchangeable");
        System.out.println("✓ Interface Segregation - Focused interfaces for each service");
        System.out.println("✓ Dependency Inversion - Services depend on abstractions (interfaces)");
    }

    private static void printSearchResults(List<SearchResult> results) {
        if (results.isEmpty()) {
            System.out.println("   No results found");
            return;
        }
        
        System.out.println("   Found " + results.size() + " result(s):");
        for (int i = 0; i < results.size(); i++) {
            SearchResult result = results.get(i);
            System.out.println("   [" + (i + 1) + "] Score: " + 
                             String.format("%.2f", result.getRelevanceScore()) + 
                             " | Matches: " + result.getMatchCount());
            System.out.println("       " + result.getDocument().getContent());
        }
    }
}
