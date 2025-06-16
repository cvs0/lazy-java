# java-lazy

A lightweight Java library for lazy initialization, caching, thread-local resources, and more.

## Features

- **Lazy Initialization**: Delay the creation of objects until they are needed
- **Lazy Caching**: Cache values to avoid recomputation with key-based lookup
- **Thread-Local Lazy Initialization**: Thread-specific lazy initialization with proper synchronization
- **Expiring Lazy Values**: Lazy-loaded resources that expire after a specified time
- **Retry Mechanism**: Automatic retry for failed initializations with configurable attempts and delay
- **Common Interface**: All implementations share the same `LazyInitializer` interface

## Installation

### Gradle

```gradle
implementation 'net.cvs0.jlazy:java-lazy:1.0.0'
```

### Maven

```xml
<dependency>
    <groupId>net.cvs0.jlazy</groupId>
    <artifactId>java-lazy</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Usage Examples

### Basic Lazy Initialization

```java
import net.cvs0.jlazy.Lazy;

// Create a lazy-initialized value
Lazy<ExpensiveObject> lazy = new Lazy<>(() -> new ExpensiveObject());

// Check if initialized (returns false)
boolean initialized = lazy.isInitialized();

// Get without initializing (returns null if not initialized)
ExpensiveObject value = lazy.get();

// Initialize and get the value
ExpensiveObject initializedValue = lazy.initialize();

// Now get() will return the initialized value
ExpensiveObject cachedValue = lazy.get();
```

### Lazy Cache

```java
import net.cvs0.jlazy.LazyCache;

// Create a cache of lazy-initialized values
LazyCache<String, UserData> userCache = new LazyCache<>();

// Get or create a cached value
Lazy<UserData> userData = userCache.getOrCreate("user123", () -> fetchUserData("user123"));

// Check if a key exists in the cache
boolean exists = userCache.containsKey("user123");

// Remove a specific entry
userCache.remove("user123");

// Clear the entire cache
userCache.clear();
```

### Thread-Local Lazy Values

```java
import net.cvs0.jlazy.ThreadLocalLazy;

// Create a thread-local lazy value
ThreadLocalLazy<ThreadContext> context = new ThreadLocalLazy<>(() -> new ThreadContext());

// Each thread gets its own initialized value
ThreadContext threadContext = context.initialize();

// Remove the thread-local value when no longer needed
context.remove();
```

### Expiring Lazy Values

```java
import net.cvs0.jlazy.ExpiringLazy;

// Create a lazy value that expires after 5 minutes
ExpiringLazy<AuthToken> token = new ExpiringLazy<>(() -> fetchNewToken(), 300000);

// Get the value (returns null if expired)
AuthToken currentToken = token.get();

// Initialize or reinitialize if expired
AuthToken validToken = token.initialize();

// Check time information
long expirationTime = token.getExpirationTimeInMillis();
long timeSinceLastAccess = token.getTimeSinceLastAccess();
```

### Retry Mechanism

```java
import net.cvs0.jlazy.LazyWithRetry;

// Create a lazy value with retry (3 attempts, 1 second delay)
LazyWithRetry<RemoteData> remoteData = new LazyWithRetry<>(
    () -> fetchFromRemoteService(),
    3,
    1000
);

// Initialize with automatic retry on failure
RemoteData data = remoteData.initialize();
```

## Requirements

- Java 11 or higher

## License

This project is licensed under the MIT License - see the LICENSE file for details.