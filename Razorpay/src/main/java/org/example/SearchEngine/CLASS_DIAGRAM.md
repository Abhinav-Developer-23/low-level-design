# Class Diagram

## Complete Class Structure

```
┌─────────────────────────────────────────────────────────────────────────┐
│                           MODEL LAYER                                    │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                           │
│  ┌─────────────────────────┐         ┌─────────────────────────┐       │
│  │      Document           │         │      Dataset            │       │
│  ├─────────────────────────┤         ├─────────────────────────┤       │
│  │ - id: String            │         │ - id: String            │       │
│  │ - content: String       │    ┌───►│ - name: String          │       │
│  │ - timestamp: long       │    │    │ - documents: Map        │       │
│  ├─────────────────────────┤    │    │ - createdAt: long       │       │
│  │ + getId()               │    │    ├─────────────────────────┤       │
│  │ + getContent()          │    │    │ + addDocument()         │       │
│  │ + getTimestamp()        │    │    │ + removeDocument()      │       │
│  │ + equals()              │    │    │ + getDocuments()        │       │
│  │ + hashCode()            │    │    │ + containsDocument()    │       │
│  │ + toString()            │    │    │ + getDocumentCount()    │       │
│  └─────────────────────────┘    │    └─────────────────────────┘       │
│           △                      │                                       │
│           │ contains             │                                       │
│           └──────────────────────┘                                       │
│                                                                           │
│  ┌─────────────────────────┐                                            │
│  │    SearchResult         │                                            │
│  ├─────────────────────────┤                                            │
│  │ - document: Document    │───────────────┐                            │
│  │ - relevanceScore: double│               │                            │
│  │ - matchCount: int       │               ▼                            │
│  ├─────────────────────────┤         (references Document)              │
│  │ + getDocument()         │                                            │
│  │ + getRelevanceScore()   │                                            │
│  │ + getMatchCount()       │                                            │
│  │ + toString()            │                                            │
│  └─────────────────────────┘                                            │
│                                                                           │
└─────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────┐
│                           ENUM LAYER                                     │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                           │
│  ┌─────────────────────────┐                                            │
│  │   <<enumeration>>       │                                            │
│  │      SortOrder          │                                            │
│  ├─────────────────────────┤                                            │
│  │ RELEVANCE_DESC          │                                            │
│  │ RELEVANCE_ASC           │                                            │
│  │ ALPHABETICAL_ASC        │                                            │
│  │ ALPHABETICAL_DESC       │                                            │
│  │ TIMESTAMP_DESC          │                                            │
│  │ TIMESTAMP_ASC           │                                            │
│  ├─────────────────────────┤                                            │
│  │ - description: String   │                                            │
│  ├─────────────────────────┤                                            │
│  │ + getDescription()      │                                            │
│  └─────────────────────────┘                                            │
│                                                                           │
└─────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────┐
│                        EXCEPTION LAYER                                   │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                           │
│  ┌──────────────────────────────┐  ┌──────────────────────────────┐    │
│  │ DatasetNotFoundException     │  │ DocumentNotFoundException    │    │
│  │ extends RuntimeException     │  │ extends RuntimeException     │    │
│  └──────────────────────────────┘  └──────────────────────────────┘    │
│                                                                           │
│  ┌──────────────────────────────┐                                       │
│  │DatasetAlreadyExistsException │                                       │
│  │ extends RuntimeException     │                                       │
│  └──────────────────────────────┘                                       │
│                                                                           │
└─────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────┐
│                       REPOSITORY LAYER                                   │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                           │
│  ┌─────────────────────────────────────────────────────────────┐        │
│  │         <<interface>>                                        │        │
│  │         DatasetRepository                                    │        │
│  ├─────────────────────────────────────────────────────────────┤        │
│  │ + createDataset(name): Dataset                              │        │
│  │ + getDataset(id): Optional<Dataset>                         │        │
│  │ + getAllDatasets(): List<Dataset>                           │        │
│  │ + deleteDataset(id): boolean                                │        │
│  │ + datasetExists(id): boolean                                │        │
│  │ + addDocument(datasetId, document): void                    │        │
│  │ + removeDocument(datasetId, documentId): void               │        │
│  │ + getDocuments(datasetId): List<Document>                   │        │
│  └──────────────────────────┬──────────────────────────────────┘        │
│                             △                                            │
│                             │ implements                                 │
│                             │                                            │
│  ┌──────────────────────────┴──────────────────────────────────┐        │
│  │         DatasetRepositoryImpl                                │        │
│  ├─────────────────────────────────────────────────────────────┤        │
│  │ - datasets: Map<String, Dataset>                            │        │
│  ├─────────────────────────────────────────────────────────────┤        │
│  │ + createDataset(name): Dataset                              │        │
│  │ + getDataset(id): Optional<Dataset>                         │        │
│  │ + getAllDatasets(): List<Dataset>                           │        │
│  │ + deleteDataset(id): boolean                                │        │
│  │ + datasetExists(id): boolean                                │        │
│  │ + addDocument(datasetId, document): void                    │        │
│  │ + removeDocument(datasetId, documentId): void               │        │
│  │ + getDocuments(datasetId): List<Document>                   │        │
│  └─────────────────────────────────────────────────────────────┘        │
│                    (Uses ConcurrentHashMap)                              │
│                                                                           │
└─────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────┐
│                         SERVICE LAYER                                    │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                           │
│  ┌────────────────────────┐  ┌────────────────────────┐                │
│  │   <<interface>>        │  │   <<interface>>        │                │
│  │   DatasetService       │  │   DocumentService      │                │
│  ├────────────────────────┤  ├────────────────────────┤                │
│  │ + createDataset()      │  │ + addDocument()        │                │
│  │ + getDataset()         │  │ + deleteDocument()     │                │
│  │ + getAllDatasets()     │  │ + getDocument()        │                │
│  │ + deleteDataset()      │  └────────────────────────┘                │
│  └──────────┬─────────────┘            △                                │
│             △                          │ implements                      │
│             │ implements               │                                 │
│             │                ┌─────────┴────────────┐                   │
│  ┌──────────┴─────────────┐  │  DocumentServiceImpl │                   │
│  │  DatasetServiceImpl    │  ├──────────────────────┤                   │
│  ├────────────────────────┤  │ - repository         │───┐               │
│  │ - repository           │──┤ + addDocument()      │   │               │
│  │ + createDataset()      │  │ + deleteDocument()   │   │               │
│  │ + getDataset()         │  │ + getDocument()      │   │               │
│  │ + getAllDatasets()     │  └──────────────────────┘   │               │
│  │ + deleteDataset()      │                             │               │
│  └────────────────────────┘                             │               │
│                                                          │               │
│  ┌────────────────────────────────────────┐             │               │
│  │      <<interface>>                     │             │               │
│  │      SearchService                     │             │               │
│  ├────────────────────────────────────────┤             │               │
│  │ + search(datasetId, pattern): List     │             │               │
│  │ + search(datasetId, pattern, order)    │             │               │
│  └──────────────────┬─────────────────────┘             │               │
│                     △                                   │               │
│                     │ implements                        │               │
│                     │                                   │               │
│  ┌──────────────────┴─────────────────────┐            │               │
│  │      SearchServiceImpl                 │            │               │
│  ├────────────────────────────────────────┤            │               │
│  │ - repository                           │────────────┘               │
│  ├────────────────────────────────────────┤                            │
│  │ + search(datasetId, pattern): List     │                            │
│  │ + search(datasetId, pattern, order)    │                            │
│  │ - calculateRelevanceScore()            │                            │
│  └────────────────────────────────────────┘                            │
│                     │ uses                                              │
│                     ▼                                                    │
│          (SortStrategyFactory)                                          │
│                                                                           │
└─────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────┐
│                      STRATEGY LAYER (Strategy Pattern)                   │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                           │
│  ┌─────────────────────────────────────────────────────────────┐        │
│  │              <<interface>>                                   │        │
│  │              SortStrategy                                    │        │
│  ├─────────────────────────────────────────────────────────────┤        │
│  │ + sort(results: List<SearchResult>): void                   │        │
│  └────┬────────────────────────────────────────────────────────┘        │
│       △                                                                  │
│       │ implements                                                       │
│       │                                                                  │
│       ├────────────────┬────────────────┬────────────────┐             │
│       │                │                │                │             │
│  ┌────┴──────────┐┌────┴──────────┐┌────┴──────────┐┌────┴──────────┐ │
│  │ RelevanceDesc ││ RelevanceAsc  ││AlphabeticalAsc││AlphabeticalDesc││
│  │ SortStrategy  ││ SortStrategy  ││ SortStrategy  ││ SortStrategy   ││
│  └───────────────┘└───────────────┘└───────────────┘└────────────────┘ │
│                                                                           │
│  ┌───────────────┐┌───────────────┐                                     │
│  │ TimestampDesc ││ TimestampAsc  │                                     │
│  │ SortStrategy  ││ SortStrategy  │                                     │
│  └───────────────┘└───────────────┘                                     │
│                                                                           │
│  ┌─────────────────────────────────────────────────────────────┐        │
│  │         SortStrategyFactory (Factory Pattern)                │        │
│  ├─────────────────────────────────────────────────────────────┤        │
│  │ + getSortStrategy(SortOrder): SortStrategy                  │        │
│  └─────────────────────────────────────────────────────────────┘        │
│                                                                           │
└─────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────┐
│                    FACADE LAYER (Facade Pattern)                         │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                           │
│  ┌─────────────────────────────────────────────────────────────┐        │
│  │                    SearchEngine                              │        │
│  ├─────────────────────────────────────────────────────────────┤        │
│  │ - datasetService: DatasetService                            │        │
│  │ - documentService: DocumentService                          │        │
│  │ - searchService: SearchService                              │        │
│  ├─────────────────────────────────────────────────────────────┤        │
│  │ + SearchEngine()                                            │        │
│  │ + SearchEngine(DatasetRepository)                           │        │
│  ├─────────────────────────────────────────────────────────────┤        │
│  │ // Dataset Operations                                       │        │
│  │ + createDataset(name): Dataset                              │        │
│  │ + getDataset(id): Dataset                                   │        │
│  │ + getAllDatasets(): List<Dataset>                           │        │
│  │ + deleteDataset(id): void                                   │        │
│  ├─────────────────────────────────────────────────────────────┤        │
│  │ // Document Operations                                      │        │
│  │ + addDocument(datasetId, content): Document                 │        │
│  │ + deleteDocument(datasetId, documentId): void               │        │
│  │ + getDocument(datasetId, documentId): Document              │        │
│  ├─────────────────────────────────────────────────────────────┤        │
│  │ // Search Operations                                        │        │
│  │ + search(datasetId, pattern): List<SearchResult>            │        │
│  │ + search(datasetId, pattern, order): List<SearchResult>     │        │
│  └─────────────────────────────────────────────────────────────┘        │
│                             │                                            │
│                             │ coordinates                                │
│                             ▼                                            │
│              ┌──────────────┴───────────────┐                           │
│              │                               │                           │
│      DatasetService              DocumentService                        │
│      SearchService                                                       │
│                                                                           │
└─────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────┐
│                         CLIENT LAYER                                     │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                           │
│  ┌─────────────────────────────────────────────────────────────┐        │
│  │                        Main                                  │        │
│  ├─────────────────────────────────────────────────────────────┤        │
│  │ + main(String[] args): void                                 │        │
│  │ - printSearchResults(List<SearchResult>): void              │        │
│  └─────────────────────────────────────────────────────────────┘        │
│                             │                                            │
│                             │ uses                                       │
│                             ▼                                            │
│                       SearchEngine                                       │
│                                                                           │
└─────────────────────────────────────────────────────────────────────────┘
```

## Relationship Summary

### Composition (HAS-A)
- `SearchEngine` ──► `DatasetService`
- `SearchEngine` ──► `DocumentService`
- `SearchEngine` ──► `SearchService`
- `Dataset` ──► `Map<String, Document>`
- `SearchResult` ──► `Document`

### Dependency (USES)
- All Services ──► `DatasetRepository` (interface)
- `SearchServiceImpl` ──► `SortStrategyFactory`
- `SortStrategyFactory` ──► `SortStrategy` implementations

### Implementation (IS-A)
- `DatasetRepositoryImpl` implements `DatasetRepository`
- `DatasetServiceImpl` implements `DatasetService`
- `DocumentServiceImpl` implements `DocumentService`
- `SearchServiceImpl` implements `SearchService`
- 6 concrete strategies implement `SortStrategy`

### Inheritance
- All custom exceptions extend `RuntimeException`

## Key Design Decisions

### Why Interfaces?
- **Flexibility**: Easy to swap implementations
- **Testing**: Can mock interfaces for unit tests
- **DIP**: High-level modules depend on abstractions

### Why Strategy Pattern?
- **OCP**: New sort strategies without modifying existing code
- **Flexibility**: Runtime algorithm selection
- **Maintainability**: Each strategy is independent

### Why Facade?
- **Simplicity**: Single entry point for clients
- **Decoupling**: Clients don't need to know about services
- **Ease of use**: Simple API hides complexity

### Why Repository Pattern?
- **Abstraction**: Business logic independent of storage
- **Testability**: Easy to mock data layer
- **Flexibility**: Can switch from in-memory to database

This class diagram shows a well-architected system with clear separation of concerns and proper application of design patterns and SOLID principles.



