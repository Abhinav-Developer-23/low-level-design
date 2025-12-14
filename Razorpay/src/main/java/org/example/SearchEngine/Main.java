package org.example.SearchEngine;

import org.example.SearchEngine.enums.SortOrder;
import org.example.SearchEngine.model.Dataset;
import org.example.SearchEngine.model.Document;
import org.example.SearchEngine.model.SearchResult;
import org.example.SearchEngine.model.User;
import org.example.SearchEngine.service.*;
import org.example.SearchEngine.service.Intergace.DatasetService;
import org.example.SearchEngine.service.Intergace.DocumentService;
import org.example.SearchEngine.service.Intergace.SearchService;
import org.example.SearchEngine.service.Intergace.UserService;

import java.util.List;

/**
 * Main class demonstrating the Search Engine functionality.
 * Demonstrates SOLID principles and OOP concepts:
 * - Single Responsibility: Each class has one clear purpose
 * - Open/Closed: Strategy pattern allows adding new sort types without modifying existing code
 * - Liskov Substitution: All implementations can replace their interfaces
 * - Interface Segregation: Services have focused, specific interfaces
 * - Dependency Inversion: Classes depend on abstractions (interfaces) not concrete implementations
 */
public class Main {
    public static void main(String[] args) {
        // Initialize services
        UserService userService = new UserServiceImpl();
        DatasetService datasetService = new DatasetServiceImpl();
        DocumentService documentService = new DocumentServiceImpl();
        SearchService searchService = new SearchServiceImpl();

        System.out.println("=== Tech Blog Search Engine Demo ===\n");

        // 1. Register users
        System.out.println("1. Registering Users...");
        User alice = userService.registerUser("user1", "Alice Johnson", "alice@techblog.com");
        User bob = userService.registerUser("user2", "Bob Smith", "bob@techblog.com");
        User charlie = userService.registerUser("user3", "Charlie Brown", "charlie@techblog.com");
        System.out.println("   Registered: " + alice.getName() + ", " + bob.getName() + ", " + charlie.getName());
        System.out.println();

        // 2. Create datasets
        System.out.println("2. Creating Datasets...");
        Dataset javaDataset = datasetService.createDataset("ds1", "Java Articles", alice.getUserId());
        System.out.println("   Created dataset: " + javaDataset.getName() + " (Owner: " + alice.getName() + ")");
        
        Dataset pythonDataset = datasetService.createDataset("ds2", "Python Tutorials", bob.getUserId());
        System.out.println("   Created dataset: " + pythonDataset.getName() + " (Owner: " + bob.getName() + ")");
        System.out.println();

        // 3. Add documents to Alice's Java dataset
        System.out.println("3. Adding Documents to Java Articles...");
        documentService.addDocument("ds1", "doc1", 
            "Java Spring Boot Tutorial: Learn how to build REST APIs with Spring Boot framework", 
            alice.getUserId());
        documentService.addDocument("ds1", "doc2", 
            "Advanced Java Concurrency: Understanding threads and parallel processing in Java applications", 
            alice.getUserId());
        documentService.addDocument("ds1", "doc3", 
            "Java Design Patterns: Implementing SOLID principles with practical examples", 
            alice.getUserId());
        System.out.println("   Added 3 documents to Java Articles");
        System.out.println();

        // 4. Add documents to Bob's Python dataset
        System.out.println("4. Adding Documents to Python Tutorials...");
        documentService.addDocument("ds2", "doc4", 
            "Python Machine Learning: Building neural networks with TensorFlow and Python", 
            bob.getUserId());
        documentService.addDocument("ds2", "doc5", 
            "Python Web Development: Creating web applications using Django framework", 
            bob.getUserId());
        documentService.addDocument("ds2", "doc6", 
            "Python Data Science: Analyzing data with pandas and numpy libraries", 
            bob.getUserId());
        System.out.println("   Added 3 documents to Python Tutorials");
        System.out.println();

        // 5. Grant read access to Bob for Alice's dataset
        System.out.println("5. Managing Access Control...");
        datasetService.grantReadAccess("ds1", alice.getUserId(), bob.getUserId());
        System.out.println("   Alice granted read access to Bob for Java Articles dataset");
        
        // Alice also grants access to Charlie
        datasetService.grantReadAccess("ds1", alice.getUserId(), charlie.getUserId());
        System.out.println("   Alice granted read access to Charlie for Java Articles dataset");
        System.out.println();

        // 6. Search in specific dataset - Alice searches her own dataset
        System.out.println("6. Alice searching for 'Java' in Java Articles (RELEVANCE sort)...");
        List<SearchResult> aliceResults = searchService.search("ds1", "Java", alice.getUserId(), SortOrder.RELEVANCE);
        printSearchResults(aliceResults);
        System.out.println();

        // 7. Bob searches in Alice's dataset (has read access)
        System.out.println("7. Bob searching for 'Spring' in Java Articles (ALPHABETICAL_ASC sort)...");
        List<SearchResult> bobResults = searchService.search("ds1", "Spring", bob.getUserId(), SortOrder.ALPHABETICAL_ASC);
        printSearchResults(bobResults);
        System.out.println();

        // 8. Search across all accessible datasets
        System.out.println("8. Bob searching for 'Python' across all accessible datasets (TIMESTAMP_DESC sort)...");
        List<SearchResult> bobAllResults = searchService.searchAll("Python", bob.getUserId(), SortOrder.TIMESTAMP_DESC);
        printSearchResults(bobAllResults);
        System.out.println();

        // 9. Charlie searches across all accessible datasets
        System.out.println("9. Charlie searching for 'framework' across all accessible datasets (RELEVANCE sort)...");
        List<SearchResult> charlieResults = searchService.searchAll("framework", charlie.getUserId(), SortOrder.RELEVANCE);
        printSearchResults(charlieResults);
        System.out.println();

        // 10. Demonstrate different sort orders
        System.out.println("10. Demonstrating different sort orders for 'Java' in Alice's dataset...");
        
        System.out.println("    a) TIMESTAMP_ASC (oldest first):");
        List<SearchResult> timestampAsc = searchService.search("ds1", "Java", alice.getUserId(), SortOrder.TIMESTAMP_ASC);
        printSearchResults(timestampAsc);
        
        System.out.println("    b) TIMESTAMP_DESC (newest first):");
        List<SearchResult> timestampDesc = searchService.search("ds1", "Java", alice.getUserId(), SortOrder.TIMESTAMP_DESC);
        printSearchResults(timestampDesc);
        
        System.out.println("    c) ALPHABETICAL_DESC (Z-A):");
        List<SearchResult> alphaDesc = searchService.search("ds1", "Java", alice.getUserId(), SortOrder.ALPHABETICAL_DESC);
        printSearchResults(alphaDesc);
        System.out.println();

        // 11. Delete a document
        System.out.println("11. Alice deleting a document from Java Articles...");
        documentService.deleteDocument("ds1", "doc1", alice.getUserId());
        System.out.println("    Deleted document 'doc1' (Spring Boot Tutorial)");
        
        System.out.println("    Searching again for 'Spring':");
        List<SearchResult> afterDelete = searchService.search("ds1", "Spring", alice.getUserId(), SortOrder.RELEVANCE);
        printSearchResults(afterDelete);
        System.out.println();

        // 12. Revoke access
        System.out.println("12. Alice revoking Bob's read access to Java Articles...");
        datasetService.revokeReadAccess("ds1", alice.getUserId(), bob.getUserId());
        System.out.println("    Access revoked for Bob");
        System.out.println();

        // 13. Try unauthorized access (will throw exception)
        System.out.println("13. Bob attempting to search Java Articles after access revoked...");
        try {
            searchService.search("ds1", "Java", bob.getUserId(), SortOrder.RELEVANCE);
        } catch (Exception e) {
            System.out.println("    ERROR: " + e.getMessage());
        }
        System.out.println();

        // 14. Try unauthorized document addition (will throw exception)
        System.out.println("14. Charlie attempting to add document to Alice's dataset (unauthorized)...");
        try {
            documentService.addDocument("ds1", "doc99", "Unauthorized document", charlie.getUserId());
        } catch (Exception e) {
            System.out.println("    ERROR: " + e.getMessage());
        }
        System.out.println();

        System.out.println("=== Demo Complete ===");
        System.out.println("\nKey Design Patterns & Principles Used:");
        System.out.println("1. Singleton Pattern: Repository classes (DatasetRepository, UserRepository)");
        System.out.println("2. Strategy Pattern: Sorting strategies (AlphabeticalAscSortStrategy, etc.)");
        System.out.println("3. Factory Pattern: SortStrategyFactory for creating sort strategies");
        System.out.println("4. Repository Pattern: Separating data access logic");
        System.out.println("5. Service Layer Pattern: Business logic separation");
        System.out.println("6. SOLID Principles: Throughout the design");
        System.out.println("   - Single Responsibility: Each class has one purpose");
        System.out.println("   - Open/Closed: Easy to extend with new sort types");
        System.out.println("   - Liskov Substitution: Interface implementations are interchangeable");
        System.out.println("   - Interface Segregation: Focused service interfaces");
        System.out.println("   - Dependency Inversion: Depending on abstractions");
    }

    /**
     * Helper method to print search results in a formatted way.
     * @param results List of search results to print
     */
    private static void printSearchResults(List<SearchResult> results) {
        if (results.isEmpty()) {
            System.out.println("    No results found.");
            return;
        }
        
        for (int i = 0; i < results.size(); i++) {
            SearchResult result = results.get(i);
            Document doc = result.getDocument();
            System.out.println("    [" + (i + 1) + "] Document ID: " + doc.getDocumentId() + 
                             " | Dataset: " + result.getDatasetId() + 
                             " | Matches: " + result.getMatchCount());
            System.out.println("        Content: " + doc.getContent());
            System.out.println("        Timestamp: " + doc.getTimestamp());
        }
    }
}
