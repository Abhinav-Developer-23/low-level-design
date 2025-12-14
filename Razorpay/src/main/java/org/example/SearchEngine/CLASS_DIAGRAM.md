# Search Engine - Complete UML Class Diagram

```
┌─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┐
│                                                         MODEL PACKAGE                                                                   │
├─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┤
│                                                                                                                                         │
│  ┌────────────────────────┐        ┌──────────────────────────────────────────┐        ┌─────────────────────────┐                    │
│  │       User             │        │            Dataset                       │        │      Document           │                    │
│  ├────────────────────────┤        ├──────────────────────────────────────────┤        ├─────────────────────────┤                    │
│  │ - userId: String       │        │ - datasetId: String                      │        │ - documentId: String    │                    │
│  │ - name: String         │        │ - name: String                           │        │ - content: String       │                    │
│  │ - email: String        │        │ - ownerId: String                        │◆───────│ - timestamp: DateTime   │                    │
│  ├────────────────────────┤        │ - documents: Map<String, Document>       │        ├─────────────────────────┤                    │
│  │ + User(...)            │        │ - authorizedReaders: Set<String>         │        │ + Document(...)         │                    │
│  │ + getUserId(): String  │        ├──────────────────────────────────────────┤        │ + getDocumentId(): String│                   │
│  │ + getName(): String    │        │ + Dataset(...)                           │        │ + getContent(): String  │                    │
│  │ + getEmail(): String   │        │ + getDatasetId(): String                 │        │ + getTimestamp(): DT    │                    │
│  │ + equals(o): boolean   │        │ + getName(): String                      │        │ + matches(p): boolean   │                    │
│  │ + hashCode(): int      │        │ + getOwnerId(): String                   │        │ + equals(o): boolean    │                    │
│  │ + toString(): String   │        │ + addDocument(d): void                   │        │ + hashCode(): int       │                    │
│  └────────────────────────┘        │ + removeDocument(id): Document           │        │ + toString(): String    │                    │
│                                    │ + getDocument(id): Document              │        └─────────────────────────┘                    │
│                                    │ + getAllDocuments(): Collection          │                    │                                   │
│                                    │ + grantReadAccess(uid): void             │                    │                                   │
│                                    │ + revokeReadAccess(uid): void            │                    ▼                                   │
│                                    │ + hasReadAccess(uid): boolean            │        ┌─────────────────────────┐                    │
│                                    │ + isOwner(uid): boolean                  │        │    SearchResult         │                    │
│                                    │ + equals(o): boolean                     │        ├─────────────────────────┤                    │
│                                    │ + hashCode(): int                        │◇───────│ - document: Document    │                    │
│                                    │ + toString(): String                     │        │ - datasetId: String     │                    │
│                                    └──────────────────────────────────────────┘        │ - matchCount: int       │                    │
│                                                                                        ├─────────────────────────┤                    │
│                                                                                        │ + SearchResult(...)     │                    │
│                                                                                        │ + getDocument(): Doc    │                    │
│                                                                                        │ + getDatasetId(): String│                    │
│                                                                                        │ + getMatchCount(): int  │                    │
│                                                                                        └─────────────────────────┘                    │
└─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┐
│                                                         ENUM PACKAGE                                                                    │
├─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┤
│                                                                                                                                         │
│                                            ┌────────────────────────┐                                                                  │
│                                            │   «enumeration»        │                                                                  │
│                                            │     SortOrder          │                                                                  │
│                                            ├────────────────────────┤                                                                  │
│                                            │ ALPHABETICAL_ASC       │                                                                  │
│                                            │ ALPHABETICAL_DESC      │                                                                  │
│                                            │ TIMESTAMP_ASC          │                                                                  │
│                                            │ TIMESTAMP_DESC         │                                                                  │
│                                            │ RELEVANCE              │                                                                  │
│                                            └────────────────────────┘                                                                  │
│                                                                                                                                         │
└─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┐
│                                                      EXCEPTION PACKAGE                                                                  │
├─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┤
│                                                                                                                                         │
│                                          ┌────────────────────────────────┐                                                            │
│                                          │    RuntimeException «Java»     │                                                            │
│                                          └────────────────┬───────────────┘                                                            │
│                                                           │                                                                            │
│              ┌────────────────────────────────────────────┼────────────────────────────────────────────┐                              │
│              │                                            │                                            │                              │
│  ┌───────────┴──────────────┐          ┌────────────────┴───────────────┐          ┌─────────────────┴────────────────┐             │
│  │ UserAlreadyExistsException│          │ DatasetAlreadyExistsException  │          │ DocumentNotFoundException        │             │
│  └───────────────────────────┘          └────────────────────────────────┘          └──────────────────────────────────┘             │
│  ┌───────────────────────────┐          ┌────────────────────────────────┐          ┌──────────────────────────────────┐             │
│  │ UserNotFoundException      │          │ DatasetNotFoundException       │          │ UnauthorizedAccessException      │             │
│  └───────────────────────────┘          └────────────────────────────────┘          └──────────────────────────────────┘             │
│                                                                                                                                         │
└─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┐
│                                                    REPOSITORY PACKAGE (Singleton)                                                       │
├─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┤
│                                                                                                                                         │
│  ┌────────────────────────────────────────┐                    ┌──────────────────────────────────────────────────┐                   │
│  │   «Singleton»                          │                    │   «Singleton»                                    │                   │
│  │   UserRepository                       │                    │   DatasetRepository                              │                   │
│  ├────────────────────────────────────────┤                    ├──────────────────────────────────────────────────┤                   │
│  │ - {static} instance: UserRepository    │                    │ - {static} instance: DatasetRepository           │                   │
│  │ - users: Map<String, User>             │◇──────User         │ - datasets: Map<String, Dataset>                 │◇──────Dataset    │
│  ├────────────────────────────────────────┤                    ├──────────────────────────────────────────────────┤                   │
│  │ - UserRepository()                     │                    │ - DatasetRepository()                            │                   │
│  │ + {static} getInstance(): UserRepo     │                    │ + {static} getInstance(): DatasetRepo            │                   │
│  │ + createUser(u): void                  │                    │ + createDataset(d): void                         │                   │
│  │ + findById(id): Optional<User>         │                    │ + findById(id): Optional<Dataset>                │                   │
│  │ + getById(id): User                    │                    │ + getById(id): Dataset                           │                   │
│  │ + exists(id): boolean                  │                    │ + exists(id): boolean                            │                   │
│  │ + deleteUser(id): User                 │                    │ + deleteDataset(id): Dataset                     │                   │
│  │ + getAllUsers(): Map                   │                    │ + getAllDatasets(): Collection                   │                   │
│  │ + clear(): void                        │                    │ + getDatasetsByOwner(id): List                   │                   │
│  └────────────────────────────────────────┘                    │ + getDatasetsWithReadAccess(id): List            │                   │
│                                                                 │ + clear(): void                                  │                   │
│                                                                 └──────────────────────────────────────────────────┘                   │
│                                                                                                                                         │
└─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┐
│                                                     STRATEGY PACKAGE                                                                    │
├─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┤
│                                                                                                                                         │
│                                        ┌────────────────────────────────────────┐                                                      │
│                                        │         «interface»                    │                                                      │
│                                        │         SortStrategy                   │                                                      │
│                                        ├────────────────────────────────────────┤                                                      │
│                                        │ + sort(results: List<SearchResult>): void│                                                    │
│                                        └────────────────┬───────────────────────┘                                                      │
│                                                         │                                                                              │
│                                                         △                                                                              │
│                          ┌──────────────────────────────┼──────────────────────────────┐                                              │
│                          │              │               │               │              │                                              │
│              ┌───────────┴─────┐  ┌────┴────────┐  ┌──┴─────────┐  ┌─┴────────────┐  ┌┴──────────────┐                              │
│              │Alphabetical     │  │Alphabetical │  │ Timestamp  │  │  Timestamp   │  │  Relevance    │                              │
│              │AscSortStrategy  │  │DescSort     │  │  AscSort   │  │  DescSort    │  │  SortStrategy │                              │
│              └─────────────────┘  │Strategy     │  │  Strategy  │  │  Strategy    │  └───────────────┘                              │
│                                   └─────────────┘  └────────────┘  └──────────────┘                                                   │
│                                                                                                                                         │
│                                        ┌────────────────────────────────────────┐                                                      │
│                                        │         «Factory»                      │                                                      │
│                                        │    SortStrategyFactory                 │                                                      │
│                                        ├────────────────────────────────────────┤                                                      │
│                                        │ + {static} getStrategy(SortOrder): SS  │───────▷ SortStrategy                                │
│                                        └────────────────────────────────────────┘                                                      │
│                                                                                                                                         │
└─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┐
│                                                      SERVICE PACKAGE                                                                    │
├─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┤
│                                                                                                                                         │
│  ┌───────────────────────────┐                         ┌──────────────────────────────────────────┐                                   │
│  │   «interface»             │                         │   «interface»                            │                                   │
│  │   UserService             │                         │   DatasetService                         │                                   │
│  ├───────────────────────────┤                         ├──────────────────────────────────────────┤                                   │
│  │ + registerUser(...): User │                         │ + createDataset(...): Dataset            │                                   │
│  │ + getUser(id): User       │                         │ + getDataset(id): Dataset                │                                   │
│  │ + userExists(id): boolean │                         │ + deleteDataset(id, rid): void           │                                   │
│  └───────────┬───────────────┘                         │ + grantReadAccess(id, oid, tid): void    │                                   │
│              △                                         │ + revokeReadAccess(id, oid, tid): void   │                                   │
│              │                                         │ + hasReadAccess(id, uid): boolean        │                                   │
│  ┌───────────┴───────────────┐                         └──────────────────┬───────────────────────┘                                   │
│  │   UserServiceImpl         │                                            △                                                           │
│  ├───────────────────────────┤                         ┌──────────────────┴───────────────────────┐                                   │
│  │ - userRepository: UserRepo│─────▷ UserRepository    │   DatasetServiceImpl                     │                                   │
│  ├───────────────────────────┤                         ├──────────────────────────────────────────┤                                   │
│  │ + UserServiceImpl()       │                         │ - datasetRepository: DatasetRepo         │─────▷ DatasetRepository           │
│  │ + registerUser(...): User │                         │ - userRepository: UserRepo               │─────▷ UserRepository              │
│  │ + getUser(id): User       │                         ├──────────────────────────────────────────┤                                   │
│  │ + userExists(id): boolean │                         │ + DatasetServiceImpl()                   │                                   │
│  └───────────────────────────┘                         │ + createDataset(...): Dataset            │                                   │
│                                                         │ + getDataset(id): Dataset                │                                   │
│  ┌───────────────────────────┐                         │ + deleteDataset(id, rid): void           │                                   │
│  │   «interface»             │                         │ + grantReadAccess(id, oid, tid): void    │                                   │
│  │   DocumentService         │                         │ + revokeReadAccess(id, oid, tid): void   │                                   │
│  ├───────────────────────────┤                         │ + hasReadAccess(id, uid): boolean        │                                   │
│  │ + addDocument(...): Doc   │                         └──────────────────────────────────────────┘                                   │
│  │ + deleteDocument(...): void│                                                                                                        │
│  │ + getDocument(...): Doc   │                         ┌──────────────────────────────────────────┐                                   │
│  └───────────┬───────────────┘                         │   «interface»                            │                                   │
│              △                                         │   SearchService                          │                                   │
│              │                                         ├──────────────────────────────────────────┤                                   │
│  ┌───────────┴───────────────┐                         │ + search(did, p, uid, so): List<Result>  │                                   │
│  │   DocumentServiceImpl     │                         │ + searchAll(p, uid, so): List<Result>    │                                   │
│  ├───────────────────────────┤                         └──────────────────┬───────────────────────┘                                   │
│  │ - datasetRepository       │─────▷ DatasetRepository                    △                                                           │
│  ├───────────────────────────┤                         ┌──────────────────┴───────────────────────┐                                   │
│  │ + DocumentServiceImpl()   │                         │   SearchServiceImpl                      │                                   │
│  │ + addDocument(...): Doc   │                         ├──────────────────────────────────────────┤                                   │
│  │ + deleteDocument(...): void│                        │ - datasetRepository: DatasetRepo         │─────▷ DatasetRepository           │
│  │ + getDocument(...): Doc   │                         ├──────────────────────────────────────────┤                                   │
│  └───────────────────────────┘                         │ + SearchServiceImpl()                    │                                   │
│                                                         │ + search(did, p, uid, so): List<Result>  │─────▷ SortStrategyFactory         │
│                                                         │ + searchAll(p, uid, so): List<Result>    │                                   │
│                                                         │ - searchInDataset(d, p): List<Result>    │                                   │
│                                                         │ - countMatches(c, p): int                │                                   │
│                                                         └──────────────────────────────────────────┘                                   │
│                                                                                                                                         │
└─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┐
│                                                         FACADE                                                                          │
├─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┤
│                                                                                                                                         │
│                                  ┌──────────────────────────────────────────────────────┐                                             │
│                                  │         «Facade»                                     │                                             │
│                                  │         SearchEngine                                 │                                             │
│                                  ├──────────────────────────────────────────────────────┤                                             │
│                                  │ - userService: UserService                           │─────▷ UserService                           │
│                                  │ - datasetService: DatasetService                     │─────▷ DatasetService                        │
│                                  │ - documentService: DocumentService                   │─────▷ DocumentService                       │
│                                  │ - searchService: SearchService                       │─────▷ SearchService                         │
│                                  ├──────────────────────────────────────────────────────┤                                             │
│                                  │ + SearchEngine()                                     │                                             │
│                                  │ + registerUser(uid, name, email): User               │                                             │
│                                  │ + getUser(uid): User                                 │                                             │
│                                  │ + createDataset(did, name, oid): Dataset             │                                             │
│                                  │ + getDataset(did): Dataset                           │                                             │
│                                  │ + deleteDataset(did, oid): void                      │                                             │
│                                  │ + grantReadAccess(did, oid, tuid): void              │                                             │
│                                  │ + revokeReadAccess(did, oid, tuid): void             │                                             │
│                                  │ + hasReadAccess(did, uid): boolean                   │                                             │
│                                  │ + addDocument(did, docid, content, uid): Document    │                                             │
│                                  │ + deleteDocument(did, docid, uid): void              │                                             │
│                                  │ + getDocument(did, docid, uid): Document             │                                             │
│                                  │ + search(did, pattern, uid, so): List<SearchResult>  │                                             │
│                                  │ + searchAll(pattern, uid, so): List<SearchResult>    │                                             │
│                                  └──────────────────────────────────────────────────────┘                                             │
│                                                                                                                                         │
└─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┐
│                                                        MAIN CLASS                                                                       │
├─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┤
│                                                                                                                                         │
│                                  ┌──────────────────────────────────────────────────────┐                                             │
│                                  │         Main                                         │                                             │
│                                  ├──────────────────────────────────────────────────────┤                                             │
│                                  │ + {static} main(args: String[]): void                │─────▷ UserService                           │
│                                  │ - {static} printSearchResults(results): void         │─────▷ DatasetService                        │
│                                  └──────────────────────────────────────────────────────┘─────▷ DocumentService                       │
│                                                                                          ─────▷ SearchService                         │
│                                                                                                                                         │
└─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┘
```

**Legend**: `△` implements | `◆` composition | `◇` aggregation | `─────▷` uses/depends
