# Logger System

A flexible, configuration-driven logging system with support for multiple log levels and destinations.

## Features

- ✅ **Multiple Log Levels**: INFO, WARNING, ERROR, DEBUG, DELETE
- ✅ **Configurable Destinations**: Console, File, Web (HTTP endpoint)
- ✅ **Configuration-Based Routing**: Different log levels can be routed to different sinks
- ✅ **Design Patterns**: Singleton, Strategy, Factory, Builder patterns
- ✅ **Async Support**: Optional asynchronous logging for better performance
- ✅ **Thread-Safe**: Safe for multi-threaded applications
- ✅ **Extensible**: Easy to add new log levels or sink types

## Architecture

### Design Patterns Used

1. **Singleton Pattern** - Logger class ensures only one instance exists
2. **Strategy Pattern** - LogSink interface with different implementations (Console, File, Web)
3. **Factory Pattern** - SinkFactory creates appropriate sink instances
4. **Builder Pattern** - LogMessage uses builder for flexible construction

### Components

```
Logger/
├── enums/
│   └── LogLevel.java           # Log level enumeration
├── model/
│   └── LogMessage.java         # Log message model with Builder pattern
├── interfaces/
│   └── LogSink.java            # Strategy interface for log destinations
├── sink/
│   ├── ConsoleSink.java        # Console output implementation
│   ├── FileSink.java           # File output implementation
│   └── WebSink.java            # Web/HTTP output implementation
├── config/
│   ├── LoggerConfig.java       # Configuration model
│   └── ConfigReader.java       # JSON config parser
├── factory/
│   └── SinkFactory.java        # Factory for creating sinks
├── Logger.java                 # Main logger class
└── Main.java                   # Demo application
```

## Configuration

The logger system is configured using a JSON file (`logger-config.json`):

```json
{
  "logLevels": {
    "INFO": [
      {
        "type": "CONSOLE"
      }
    ],
    "ERROR": [
      {
        "type": "WEB",
        "endpoint": "https://logs.example.com/api/errors"
      },
      {
        "type": "FILE",
        "filePath": "logs/errors.log"
      }
    ]
  }
}
```

### Configuration Structure

- **logLevels**: Map of log levels to their destinations
- Each log level can have multiple sinks
- **Sink Types**:
  - `CONSOLE`: Writes to standard output
  - `FILE`: Writes to a file (specify `filePath`)
  - `WEB`: Sends to HTTP endpoint (specify `endpoint`)

## Usage

### Basic Usage

```java
// Initialize logger with config file
Logger logger = Logger.initialize("logger-config.json");

// Log messages
logger.info("Application started");
logger.warning("Memory usage high");
logger.error("Connection failed");
logger.debug("Debug information");
logger.delete("User deleted");

// Shutdown gracefully
logger.shutdown();
```

### Advanced Usage

```java
// Initialize with async logging
Logger logger = Logger.initialize("logger-config.json", true);

// Log with class context
logger.log(LogLevel.ERROR, "Null pointer exception", "UserService");

// Get existing instance
Logger logger = Logger.getInstance();
```

## Example Output

Based on the default configuration:

```
[CONSOLE] [2024-12-12 10:30:45] [INFO] [main] - Application started successfully
[CONSOLE] [2024-12-12 10:30:45] [WARNING] [main] - Memory usage is above 80%
[WEB] Sending to https://logs.example.com/api/errors: [2024-12-12 10:30:45] [ERROR] [main] - Database connection failed
(Also written to logs/errors.log)
```

## Log Routing Examples

### Example 1: Console Only (INFO, WARNING)
```
INFO → Console
WARNING → Console
```

### Example 2: Multiple Destinations (ERROR)
```
ERROR → Web API + File
```

### Example 3: File Only (DEBUG, DELETE)
```
DEBUG → logs/debug.log
DELETE → logs/deletions.log + Web API
```

## Extension Points

### Adding a New Sink Type

1. Create a new class implementing `LogSink`:
```java
public class DatabaseSink implements LogSink {
    @Override
    public void write(LogMessage logMessage) {
        // Write to database
    }
    
    @Override
    public String getSinkType() {
        return "DATABASE";
    }
}
```

2. Update `SinkFactory` to handle the new type:
```java
case "DATABASE":
    return new DatabaseSink(sinkConfig.getProperty("connectionString"));
```

3. Add configuration in `logger-config.json`:
```json
{
  "type": "DATABASE",
  "connectionString": "jdbc:mysql://localhost:3306/logs"
}
```

### Adding a New Log Level

1. Add to `LogLevel` enum:
```java
public enum LogLevel {
    INFO, WARNING, ERROR, DEBUG, DELETE, TRACE, FATAL
}
```

2. Add convenience method to `Logger`:
```java
public void trace(String message) {
    log(LogLevel.TRACE, message);
}
```

3. Configure in `logger-config.json`:
```json
"TRACE": [
  {
    "type": "FILE",
    "filePath": "logs/trace.log"
  }
]
```

## Performance Considerations

- **Async Logging**: Enable for high-throughput scenarios
- **File Buffering**: FileSink uses buffered writes for efficiency
- **Thread Pool**: Async mode uses a fixed thread pool (5 threads)
- **Graceful Shutdown**: Call `shutdown()` to ensure all logs are written

## Error Handling

- Missing configuration files throw `IOException`
- Invalid sink types throw `IllegalArgumentException`
- If no sinks are configured for a level, logs to console with warning
- File write errors are caught and logged to stderr

## Thread Safety

- Logger uses synchronized initialization (Singleton)
- Each sink handles its own thread safety
- Async logging uses thread-safe executor service

## Testing

Run the demo application:
```bash
cd Razorpay
mvn compile
mvn exec:java -Dexec.mainClass="org.example.Logger.Main"
```

Check generated log files:
```
logs/
├── errors.log
├── debug.log
└── deletions.log
```

## Requirements Met

✅ Different log levels (WARNING, ERROR, DELETE, etc.)
✅ Configuration-based routing
✅ Multiple destinations per log level
✅ Design patterns (Singleton, Strategy, Factory, Builder)
✅ Extensible architecture
✅ Production-ready features (async, thread-safe, error handling)

## Time Complexity

- Log operation: O(n) where n is the number of sinks for that log level
- Config parsing: O(m) where m is the size of config file
- Initialization: O(k) where k is total number of configured sinks

## Future Enhancements

- [ ] Log filtering based on patterns
- [ ] Log rotation for file sinks
- [ ] Batch processing for web sinks
- [ ] Log compression
- [ ] Performance metrics and monitoring
- [ ] Dynamic config reload without restart




