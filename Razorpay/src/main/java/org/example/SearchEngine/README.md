# Tech Blog Search Engine - Low Level Design

## Overview
An in-memory search engine designed for tech blog content with comprehensive search and sorting capabilities. Built with proper design patterns and SOLID principles.

## Architecture

### Package Structure
```
org.example.SearchEngine/
├── model/                      # Domain entities
│   ├── Document.java          # Document entity
│   ├── Dataset.java           # Dataset entity (collection of documents)
│   └── SearchResult.java      # Search result with relevance score
├── enums/                      # Enumerations
│   └── SortOrder.java         # Sorting options
├── exception/                  # Custom exceptions
│   ├── DatasetNotFoundException.java
│   ├── DatasetAlreadyExistsException.java
│   └── DocumentNotFoundException.java
├── repository/                 # Data access layer
│   ├── DatasetRepository.java          # Repository interface
│   └── DatasetRepositoryImpl.java      # In-memory implementation
├── service/                    # Business logic layer
│   ├── DatasetService.java            # Dataset operations interface
│   ├── DatasetServiceImpl.java        # Dataset operations implementation
│   ├── DocumentService.java           # Document operations interface
│   ├── DocumentServiceImpl.java       # Document operations implementation
│   ├── SearchService.java             # Search operations interface
│   └── SearchServiceImpl.java         # Search operations implementation
├── strategy/                   # Sorting strategies
│   ├── SortStrategy.java              # Strategy interface
│   ├── RelevanceDescSortStrategy.java
│   ├── RelevanceAscSortStrategy.java
│   ├── AlphabeticalAscSortStrategy.java
│   ├── AlphabeticalDescSortStrategy.java
│   ├── TimestampDescSortStrategy.java
│   ├── TimestampAscSortStrategy.java
│   └── SortStrategyFactory.java       # Factory for creating strategies
├── SearchEngine.java           # Facade - Main entry point
└── Main.java                   # Demo application
```

## Design Patterns

### 1. Repository Pattern
**Purpose**: Abstracts data access logic and provides a clean separation between business logic and data layer.

**Implementation**:
- `DatasetRepository` interface defines the contract
- `DatasetRepositoryImpl` provides in-memory implementation using `ConcurrentHashMap`
- Thread-safe operations for concurrent access

**Benefits**:
- Easy to swap implementations (e.g., add database support later)
- Testability - can mock repository for unit tests
- Clean separation of concerns

### 2. Strategy Pattern
**Purpose**: Defines a family of algorithms (sorting strategies) and makes them interchangeable.

**Implementation**:
- `SortStrategy` interface with `sort()` method
- Multiple concrete strategies:
  - `RelevanceDescSortStrategy` / `RelevanceAscSortStrategy`
  - `AlphabeticalAscSortStrategy` / `AlphabeticalDescSortStrategy`
  - `TimestampDescSortStrategy` / `TimestampAscSortStrategy`

**Benefits**:
- Open/Closed Principle - new strategies can be added without modifying existing code
- Runtime strategy selection
- Eliminates conditional statements

### 3. Factory Pattern
**Purpose**: Encapsulates object creation logic.

**Implementation**:
- `SortStrategyFactory` creates appropriate sort strategy based on `SortOrder` enum
- Centralizes strategy creation logic

**Benefits**:
- Single place to manage strategy creation
- Easy to add new strategies
- Reduces coupling

### 4. Facade Pattern
**Purpose**: Provides a simplified interface to a complex subsystem.

**Implementation**:
- `SearchEngine` class acts as a facade
- Coordinates between multiple services
- Simplifies client interaction

**Benefits**:
- Simple API for clients
- Hides system complexity
- Single entry point

## SOLID Principles

### 1. Single Responsibility Principle (SRP)
✅ **Applied**: Each class has one well-defined responsibility
- `DocumentService` - handles document operations only
- `SearchService` - handles search operations only
- `DatasetService` - handles dataset operations only
- `DatasetRepository` - handles data persistence only

### 2. Open/Closed Principle (OCP)
✅ **Applied**: Open for extension, closed for modification
- New sort strategies can be added without modifying existing code
- New search algorithms can be added by implementing `SearchService`
- Strategy pattern enables extension without modification

### 3. Liskov Substitution Principle (LSP)
✅ **Applied**: Subtypes are substitutable for their base types
- All `SortStrategy` implementations are interchangeable
- Any implementation of service interfaces can replace another
- Maintains behavioral consistency

### 4. Interface Segregation Principle (ISP)
✅ **Applied**: Clients should not depend on interfaces they don't use
- Small, focused interfaces: `DatasetService`, `DocumentService`, `SearchService`
- Each interface contains only relevant methods
- No fat interfaces

### 5. Dependency Inversion Principle (DIP)
✅ **Applied**: Depend on abstractions, not concretions
- Services depend on `DatasetRepository` interface, not implementation
- `SearchEngine` depends on service interfaces
- Easy to inject different implementations (great for testing)

## Key Features

### 1. Dataset Management
- Create new datasets
- List all datasets
- Delete datasets
- Thread-safe operations

### 2. Document Management
- Add documents to datasets
- Delete documents from datasets
- Retrieve documents by ID
- Auto-generated unique IDs

### 3. Search Functionality
- **Pattern matching**: Case-insensitive regex-based search
- **Relevance scoring**: Intelligent scoring based on:
  - Match count (higher is better)
  - Document length (shorter documents with same matches rank higher)
  - Pattern specificity (longer patterns are more valuable)
- **Multiple sorting options**:
  - Relevance (ascending/descending)
  - Alphabetical (ascending/descending)
  - Timestamp (newest/oldest first)

### 4. Error Handling
- Custom exceptions for different error scenarios
- `DatasetNotFoundException`
- `DatasetAlreadyExistsException`
- `DocumentNotFoundException`

## Usage Example

```java
// Initialize search engine
SearchEngine searchEngine = new SearchEngine();

// Create a dataset
Dataset dataset = searchEngine.createDataset("tech-blog");

// Add documents
Document doc1 = searchEngine.addDocument("tech-blog", 
    "Introduction to Microservices Architecture");
Document doc2 = searchEngine.addDocument("tech-blog",
    "Database Optimization Techniques");

// Search with default sorting (relevance)
List<SearchResult> results = searchEngine.search("tech-blog", "microservices");

// Search with custom sorting
List<SearchResult> sortedResults = searchEngine.search("tech-blog", 
    "optimization", SortOrder.ALPHABETICAL_ASC);

// Delete a document
searchEngine.deleteDocument("tech-blog", doc1.getId());

// Delete a dataset
searchEngine.deleteDataset("tech-blog");
```

## Technical Highlights

### Thread Safety
- Uses `ConcurrentHashMap` for thread-safe data storage
- Safe for concurrent read/write operations

### Performance
- O(1) average case for dataset lookup
- O(1) average case for document lookup within dataset
- O(n) for search (where n = number of documents)
- O(n log n) for sorting results (where n = number of results)

### Scalability
- In-memory storage for fast access
- Can be extended to use persistent storage
- Modular design allows easy scaling

### Extensibility
- Easy to add new sort strategies
- Easy to add new search algorithms
- Easy to switch to database storage
- Repository pattern allows multiple data sources

## Testing Strategy

### Unit Tests (Recommended)
1. **Repository Tests**: Test CRUD operations
2. **Service Tests**: Test business logic with mocked repository
3. **Strategy Tests**: Test each sort strategy
4. **Search Tests**: Test search algorithm and relevance scoring

### Integration Tests (Recommended)
1. Test end-to-end workflows
2. Test exception handling
3. Test concurrent access

## Future Enhancements

1. **Advanced Search**
   - Boolean operators (AND, OR, NOT)
   - Phrase search
   - Fuzzy matching
   - Synonym support

2. **Performance**
   - Inverted index for faster search
   - Caching layer
   - Pagination support

3. **Features**
   - Document metadata (author, tags, category)
   - Search filters
   - Search history
   - Auto-complete suggestions

4. **Storage**
   - Persistence to database
   - Distributed storage support
   - Document versioning

## Running the Demo

```bash
# Compile
javac -d out src/main/java/org/example/SearchEngine/**/*.java

# Run
java -cp out org.example.SearchEngine.Main
```

## Conclusion

This design demonstrates:
- ✅ Proper use of design patterns (Repository, Strategy, Factory, Facade)
- ✅ Adherence to all SOLID principles
- ✅ Clean separation of concerns (Model, Repository, Service layers)
- ✅ Thread-safe operations
- ✅ Extensible and maintainable architecture
- ✅ Comprehensive error handling
- ✅ Production-ready code structure

