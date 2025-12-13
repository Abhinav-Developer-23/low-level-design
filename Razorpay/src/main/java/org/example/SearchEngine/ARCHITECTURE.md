# Search Engine Architecture

## System Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                         CLIENT CODE                              │
│                          (Main.java)                             │
└────────────────────────────┬────────────────────────────────────┘
                             │
                             ▼
┌─────────────────────────────────────────────────────────────────┐
│                    FACADE LAYER                                  │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │              SearchEngine (Facade)                        │  │
│  │  - createDataset(), getDataset(), deleteDataset()        │  │
│  │  - addDocument(), deleteDocument()                        │  │
│  │  - search()                                               │  │
│  └────┬─────────────────┬─────────────────┬──────────────────┘  │
└───────┼─────────────────┼─────────────────┼─────────────────────┘
        │                 │                 │
        ▼                 ▼                 ▼
┌────────────────┐ ┌────────────────┐ ┌────────────────┐
│ DatasetService │ │DocumentService │ │ SearchService  │
│   Interface    │ │   Interface    │ │   Interface    │
└───────┬────────┘ └───────┬────────┘ └───────┬────────┘
        │                  │                  │
        ▼                  ▼                  ▼
┌────────────────┐ ┌────────────────┐ ┌────────────────┐
│ DatasetService │ │DocumentService │ │ SearchService  │
│     Impl       │ │     Impl       │ │     Impl       │
└───────┬────────┘ └───────┬────────┘ └───────┬────────┘
        │                  │                  │
        └──────────────────┴──────────────────┘
                           │
                           ▼
        ┌──────────────────────────────────────┐
        │    DatasetRepository (Interface)      │
        └────────────────┬─────────────────────┘
                         │
                         ▼
        ┌──────────────────────────────────────┐
        │   DatasetRepositoryImpl              │
        │   (In-Memory ConcurrentHashMap)      │
        └────────────────┬─────────────────────┘
                         │
                         ▼
        ┌──────────────────────────────────────┐
        │        DATA STORAGE                   │
        │  Map<String, Dataset>                │
        │    └─ Dataset                         │
        │       └─ Map<String, Document>        │
        └──────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│                   STRATEGY PATTERN (Sorting)                     │
│                                                                   │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │          SortStrategyFactory (Factory Pattern)           │   │
│  │  + getSortStrategy(SortOrder): SortStrategy             │   │
│  └──────────────────────┬──────────────────────────────────┘   │
│                         │ creates                               │
│                         ▼                                        │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │         SortStrategy (Interface)                         │   │
│  │         + sort(List<SearchResult>)                       │   │
│  └────┬────────────────────────────────────────────────────┘   │
│       │ implements                                              │
│       ├──────┬────────┬────────┬────────┬───────┐              │
│       ▼      ▼        ▼        ▼        ▼       ▼              │
│  ┌────────┐┌────────┐┌────────┐┌────────┐┌────────┐┌────────┐│
│  │Relevance││Relevance││Alphabetical││Alphabetical││Timestamp││Timestamp││
│  │  Desc  ││  Asc   ││   Asc  ││   Desc ││  Desc  ││  Asc   ││
│  └────────┘└────────┘└────────┘└────────┘└────────┘└────────┘│
└─────────────────────────────────────────────────────────────────┘
```

## Layer Responsibilities

### 1. Facade Layer (SearchEngine)
**Purpose**: Simplify complex subsystem interactions
- Single entry point for clients
- Coordinates between multiple services
- Hides implementation complexity

### 2. Service Layer
**Purpose**: Business logic and orchestration

**DatasetService**:
- Dataset lifecycle management
- Dataset validation

**DocumentService**:
- Document CRUD operations
- Document validation

**SearchService**:
- Search algorithm implementation
- Relevance scoring
- Result sorting coordination

### 3. Repository Layer
**Purpose**: Data access abstraction

**DatasetRepository**:
- Abstract data operations
- Encapsulate storage mechanism
- Enable easy storage switching

### 4. Strategy Layer
**Purpose**: Flexible sorting algorithms

**SortStrategy**:
- Interchangeable sorting algorithms
- Runtime algorithm selection
- Easy to extend with new strategies

## Data Flow

### Creating and Adding Documents
```
Client → SearchEngine.addDocument()
       → DocumentService.addDocument()
       → DatasetRepository.addDocument()
       → In-Memory Storage
```

### Searching Documents
```
Client → SearchEngine.search()
       → SearchService.search()
       ↓
       ├─→ DatasetRepository.getDocuments()
       │   └─→ Retrieve documents
       ↓
       ├─→ Pattern Matching (Regex)
       │   └─→ Calculate relevance scores
       ↓
       ├─→ SortStrategyFactory.getSortStrategy()
       │   └─→ Create appropriate strategy
       ↓
       └─→ SortStrategy.sort()
           └─→ Return sorted results
```

## Design Patterns Applied

### 1. Repository Pattern
```
┌───────────────────────┐
│  Business Logic       │
│  (Services)           │
└───────────┬───────────┘
            │ uses
            ▼
┌───────────────────────┐
│  DatasetRepository    │ ← Interface (abstraction)
│  (Interface)          │
└───────────┬───────────┘
            │ implements
            ▼
┌───────────────────────┐
│ DatasetRepositoryImpl │
│ (In-Memory Storage)   │
└───────────────────────┘
```

### 2. Strategy Pattern
```
┌──────────────┐
│ SearchService│
└──────┬───────┘
       │ uses
       ▼
┌──────────────┐       ┌───────────────┐
│SortStrategy  │◄──────│SortStrategy   │
│(Interface)   │       │Factory        │
└──────┬───────┘       └───────────────┘
       │ implemented by
       ├──────────┬──────────┬────────────┐
       ▼          ▼          ▼            ▼
┌──────────┐┌──────────┐┌──────────┐┌──────────┐
│Relevance ││Alphabetical││Timestamp││   ...    │
│Strategy  ││Strategy   ││Strategy  ││          │
└──────────┘└──────────┘└──────────┘└──────────┘
```

### 3. Facade Pattern
```
┌─────────────────────────────────────┐
│         SearchEngine (Facade)        │
│  Simple, unified interface           │
└─────────────┬───────────────────────┘
              │ coordinates
    ┌─────────┼──────────┐
    ▼         ▼          ▼
┌────────┐┌────────┐┌────────┐
│Dataset ││Document││Search  │
│Service ││Service ││Service │
└────────┘└────────┘└────────┘
    Complex subsystem
```

### 4. Factory Pattern
```
┌─────────────────────────────────┐
│   SortStrategyFactory           │
│   + getSortStrategy(SortOrder)  │
└────────────┬────────────────────┘
             │ creates
             ▼
┌─────────────────────────────────┐
│   Concrete SortStrategy         │
│   implementations               │
└─────────────────────────────────┘
```

## SOLID Principles Mapping

### Single Responsibility Principle (SRP)
```
DatasetService     → Manages datasets only
DocumentService    → Manages documents only
SearchService      → Handles search only
DatasetRepository  → Handles data persistence only
```

### Open/Closed Principle (OCP)
```
Adding new sort strategy:
1. Create new class implementing SortStrategy
2. Update SortStrategyFactory
3. No changes to existing strategies ✓
```

### Liskov Substitution Principle (LSP)
```
Any SortStrategy implementation can replace another:
SortStrategy strategy1 = new RelevanceDescSortStrategy();
SortStrategy strategy2 = new AlphabeticalAscSortStrategy();
// Both work identically from client perspective ✓
```

### Interface Segregation Principle (ISP)
```
Small, focused interfaces:
✓ DatasetService (dataset operations)
✓ DocumentService (document operations)
✓ SearchService (search operations)
✗ Not a single "SearchEngineService" with everything
```

### Dependency Inversion Principle (DIP)
```
High-level modules depend on abstractions:
SearchEngine → DatasetService (interface)
             → DocumentService (interface)
             → SearchService (interface)

Services → DatasetRepository (interface)

Not on concrete implementations ✓
```

## Class Relationships

### Composition
- `SearchEngine` HAS-A `DatasetService`
- `SearchEngine` HAS-A `DocumentService`
- `SearchEngine` HAS-A `SearchService`
- `Dataset` HAS-MANY `Document`

### Dependency
- Services DEPEND-ON `DatasetRepository`
- `SearchService` DEPENDS-ON `SortStrategyFactory`

### Inheritance
- Strategy classes IMPLEMENT `SortStrategy`
- Service implementations IMPLEMENT service interfaces

## Thread Safety

```
┌─────────────────────────────────────┐
│   DatasetRepositoryImpl             │
│   ┌─────────────────────────────┐   │
│   │ ConcurrentHashMap           │   │
│   │ <String, Dataset>           │   │
│   │                             │   │
│   │ Thread 1 ────┐              │   │
│   │ Thread 2 ────┼──► Safe     │   │
│   │ Thread 3 ────┘              │   │
│   └─────────────────────────────┘   │
└─────────────────────────────────────┘
```

- Uses `ConcurrentHashMap` for thread-safe operations
- No synchronization needed at service layer
- Safe for concurrent read/write operations

## Extension Points

### Adding New Sort Strategy
1. Create class implementing `SortStrategy`
2. Add enum value to `SortOrder`
3. Update `SortStrategyFactory`

### Adding Database Support
1. Create `DatabaseDatasetRepository implements DatasetRepository`
2. Inject into services via constructor
3. No changes to service layer needed

### Adding New Service
1. Create service interface
2. Create service implementation
3. Inject repository via constructor
4. Add methods to `SearchEngine` facade

This architecture provides a clean, maintainable, and extensible foundation for the search engine system.




