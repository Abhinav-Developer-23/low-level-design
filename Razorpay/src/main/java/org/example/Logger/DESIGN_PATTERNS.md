# Design Patterns in Logger System

This document explains the design patterns used in the Logger System and their benefits.

## 1. Singleton Pattern

**Location**: `Logger.java`

**Purpose**: Ensure only one logger instance exists throughout the application.

**Implementation**:
```java
public class Logger {
    private static Logger instance;
    
    private Logger(LoggerConfig config, boolean asyncLogging) {
        // Private constructor
    }
    
    public static synchronized Logger initialize(String configFilePath) {
        if (instance != null) {
            throw new IllegalStateException("Logger already initialized");
        }
        // Initialize once
        return instance;
    }
    
    public static Logger getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Logger not initialized");
        }
        return instance;
    }
}
```

**Benefits**:
- Single source of truth for logging configuration
- Prevents multiple conflicting logger instances
- Global access point for logging
- Thread-safe initialization with synchronized method

---

## 2. Strategy Pattern

**Location**: `LogSink.java` interface and implementations in `sink/` package

**Purpose**: Define a family of algorithms (log destinations) and make them interchangeable.

**Implementation**:
```java
// Strategy Interface
public interface LogSink {
    void write(LogMessage logMessage);
    String getSinkType();
}

// Concrete Strategies
public class ConsoleSink implements LogSink { ... }
public class FileSink implements LogSink { ... }
public class WebSink implements LogSink { ... }
```

**Benefits**:
- Easy to add new sink types without modifying existing code
- Runtime selection of logging destinations
- Encapsulates different logging behaviors
- Open/Closed Principle: open for extension, closed for modification

**Example Usage**:
```java
// Can switch between strategies at runtime based on config
List<LogSink> sinks = Arrays.asList(
    new ConsoleSink(),
    new FileSink("app.log"),
    new WebSink("https://api.example.com")
);
```

---

## 3. Factory Pattern

**Location**: `SinkFactory.java`

**Purpose**: Create objects without specifying the exact class of object to be created.

**Implementation**:
```java
public class SinkFactory {
    public static LogSink createSink(LoggerConfig.SinkConfig sinkConfig) {
        String type = sinkConfig.getType().toUpperCase();
        
        switch (type) {
            case "CONSOLE":
                return new ConsoleSink();
            case "FILE":
                return new FileSink(sinkConfig.getProperty("filePath"));
            case "WEB":
                return new WebSink(sinkConfig.getProperty("endpoint"));
            default:
                throw new IllegalArgumentException("Unknown sink type");
        }
    }
}
```

**Benefits**:
- Centralizes object creation logic
- Decouples sink creation from sink usage
- Easy to add new sink types (just add case in factory)
- Promotes loose coupling

**Flow Diagram**:
```
Config → SinkFactory → Appropriate Sink Instance
  |           |              |
  |           |              ├─ ConsoleSink
  |           |              ├─ FileSink
  |           |              └─ WebSink
```

---

## 4. Builder Pattern

**Location**: `LogMessage.java`

**Purpose**: Construct complex objects step by step with a fluent interface.

**Implementation**:
```java
public class LogMessage {
    private final LogLevel level;
    private final String message;
    private final LocalDateTime timestamp;
    private final String threadName;
    private final String className;
    
    private LogMessage(Builder builder) {
        this.level = builder.level;
        this.message = builder.message;
        // ... other fields
    }
    
    public static class Builder {
        private LogLevel level;
        private String message;
        // ... other fields
        
        public Builder level(LogLevel level) {
            this.level = level;
            return this;
        }
        
        public Builder message(String message) {
            this.message = message;
            return this;
        }
        
        public LogMessage build() {
            // Validation
            return new LogMessage(this);
        }
    }
}
```

**Benefits**:
- Fluent, readable API for creating log messages
- Immutable LogMessage objects
- Optional parameters with default values
- Clear separation between construction and representation

**Example Usage**:
```java
LogMessage msg = new LogMessage.Builder()
    .level(LogLevel.ERROR)
    .message("Connection failed")
    .className("DatabaseService")
    .build();
```

---

## 5. Configuration Pattern (Additional)

**Location**: `LoggerConfig.java`, `ConfigReader.java`

**Purpose**: Externalize configuration for flexibility and maintainability.

**Implementation**:
- JSON configuration file
- Config reader parses JSON into config objects
- Logger initializes based on configuration

**Benefits**:
- Change behavior without recompiling code
- Environment-specific configurations (dev, staging, prod)
- Easy to modify routing rules
- Non-developers can adjust logging behavior

---

## Pattern Interactions

### How Patterns Work Together

```
1. User calls Logger.initialize() (SINGLETON)
   ↓
2. ConfigReader reads config file (CONFIGURATION)
   ↓
3. Logger uses SinkFactory (FACTORY)
   ↓
4. SinkFactory creates appropriate LogSink instances (STRATEGY)
   ↓
5. User creates LogMessage using Builder (BUILDER)
   ↓
6. Logger routes message to appropriate sinks (STRATEGY)
   ↓
7. Each sink writes message in its own way (STRATEGY)
```

### Example Flow

```java
// 1. Initialize logger (Singleton + Factory + Configuration)
Logger logger = Logger.initialize("config.json");
                    ↓
         ConfigReader.readConfig()
                    ↓
         SinkFactory.createSink() for each configured sink
                    ↓
         Creates: ConsoleSink, FileSink("errors.log"), WebSink("api.com")

// 2. Log a message (Builder + Strategy)
logger.error("Database connection failed");
         ↓
   new LogMessage.Builder()  // Builder Pattern
         .level(ERROR)
         .message("Database connection failed")
         .build()
         ↓
   Routes to sinks configured for ERROR level
         ↓
   Each sink writes (Strategy Pattern):
   - FileSink writes to errors.log
   - WebSink sends to api.com
```

---

## SOLID Principles

### Single Responsibility Principle (SRP)
- `Logger`: Routing logs
- `LogSink`: Writing logs to destination
- `SinkFactory`: Creating sinks
- `ConfigReader`: Reading configuration
- Each class has one reason to change

### Open/Closed Principle (OCP)
- Open for extension: Add new sinks by implementing `LogSink`
- Closed for modification: No need to change existing code

### Liskov Substitution Principle (LSP)
- Any `LogSink` implementation can be substituted for another
- All sinks honor the `LogSink` interface contract

### Interface Segregation Principle (ISP)
- `LogSink` interface is minimal and focused
- Clients only depend on methods they use

### Dependency Inversion Principle (DIP)
- Logger depends on `LogSink` interface, not concrete classes
- High-level logging logic doesn't depend on low-level sink details

---

## Benefits of This Architecture

1. **Maintainability**: Each component is focused and independent
2. **Testability**: Easy to mock interfaces and test in isolation
3. **Extensibility**: Add new features without breaking existing code
4. **Flexibility**: Configuration-driven behavior changes
5. **Readability**: Clear structure and responsibilities
6. **Scalability**: Can handle complex routing rules easily

---

## Real-World Applications

This pattern combination is used in:
- **Log4j/Logback**: Similar architecture with appenders (sinks)
- **SLF4J**: Facade pattern with multiple logging backends
- **Winston (Node.js)**: Transports concept similar to our sinks
- **Serilog (.NET)**: Sinks and enrichers architecture

---

## Summary

| Pattern | Purpose | Benefit |
|---------|---------|---------|
| Singleton | One logger instance | Consistency, global access |
| Strategy | Interchangeable sinks | Flexibility, extensibility |
| Factory | Create sink objects | Centralized creation, loose coupling |
| Builder | Construct log messages | Fluent API, immutability |
| Configuration | External config | Flexibility without recompilation |

This combination of patterns creates a robust, maintainable, and professional logging system suitable for production use.





