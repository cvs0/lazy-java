package net.cvs0.jlazy;

import java.util.function.Supplier;

/**
 * A lazy initialization implementation that expires after a specified time.
 * 
 * @param <T> The type of value to be lazily initialized
 */
public class ExpiringLazy<T> implements LazyInitializer<T> {
    private final Supplier<T> initializer;
    private T value;
    private long lastAccessTime;
    private final long expirationTimeInMillis;
    private boolean initialized = false;

    /**
     * Creates a new ExpiringLazy instance.
     * 
     * @param initializer The supplier function that will initialize the value
     * @param expirationTimeInMillis The time in milliseconds after which the value expires
     */
    public ExpiringLazy(Supplier<T> initializer, long expirationTimeInMillis) {
        this.initializer = initializer;
        this.expirationTimeInMillis = expirationTimeInMillis;
        this.lastAccessTime = System.currentTimeMillis();
    }

    /**
     * Gets the value if it's already initialized and not expired, otherwise returns null.
     * This method does not trigger initialization.
     * 
     * @return The initialized value or null if not yet initialized or expired
     */
    @Override
    public T get() {
        if (!initialized) {
            return null;
        }
        
        if (isExpired()) {
            initialized = false;
            value = null;
            return null;
        }
        
        lastAccessTime = System.currentTimeMillis();
        return value;
    }
    
    /**
     * Initializes the value if not already initialized or if expired.
     * 
     * @return The initialized value
     * @throws InterruptedException if the initialization process is interrupted
     */
    @Override
    public T initialize() throws InterruptedException {
        if (!initialized || isExpired()) {
            initialized = true;
            value = initializer.get();
            lastAccessTime = System.currentTimeMillis();
        }
        return value;
    }
    
    /**
     * Checks if the value has been initialized and has not expired.
     * 
     * @return true if the value has been initialized and has not expired, false otherwise
     */
    @Override
    public boolean isInitialized() {
        if (isExpired()) {
            initialized = false;
            value = null;
        }
        return initialized;
    }
    
    /**
     * Checks if the value has expired.
     * 
     * @return true if the value has expired, false otherwise
     */
    private boolean isExpired() {
        return System.currentTimeMillis() - lastAccessTime > expirationTimeInMillis;
    }
    
    /**
     * Gets the time in milliseconds when the value will expire.
     * 
     * @return The expiration time in milliseconds
     */
    public long getExpirationTimeInMillis() {
        return expirationTimeInMillis;
    }
    
    /**
     * Gets the time in milliseconds since the last access.
     * 
     * @return The time in milliseconds since the last access
     */
    public long getTimeSinceLastAccess() {
        return System.currentTimeMillis() - lastAccessTime;
    }
}