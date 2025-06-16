package net.cvs0.jlazy;

import java.util.function.Supplier;

/**
 * A lazy initialization implementation that retries on failure.
 * 
 * @param <T> The type of value to be lazily initialized
 */
public class LazyWithRetry<T> implements LazyInitializer<T> {
    private final Supplier<T> supplier;
    private final int maxRetries;
    private final long retryDelay;
    private T value;
    private boolean initialized = false;

    /**
     * Creates a new LazyWithRetry instance.
     * 
     * @param supplier The supplier function that will initialize the value
     * @param maxRetries The maximum number of retry attempts
     * @param retryDelay The delay between retries in milliseconds
     */
    public LazyWithRetry(Supplier<T> supplier, int maxRetries, long retryDelay) {
        this.supplier = supplier;
        this.maxRetries = maxRetries;
        this.retryDelay = retryDelay;
    }

    /**
     * Gets the value if it's already initialized, otherwise returns null.
     * This method does not trigger initialization.
     * 
     * @return The initialized value or null if not yet initialized
     */
    @Override
    public T get() {
        if (!initialized) {
            return null;
        }
        return value;
    }
    
    /**
     * Initializes the value with retry logic if not already initialized.
     * 
     * @return The initialized value
     * @throws InterruptedException if the thread is interrupted during retry delay
     */
    @Override
    public T initialize() throws InterruptedException {
        if (!initialized) {
            initialized = true;
            int attempt = 0;
            while (attempt < maxRetries) {
                try {
                    value = supplier.get();
                    return value;
                } catch (RuntimeException e) {
                    attempt++;
                    if (attempt >= maxRetries) {
                        throw e;
                    }
                    Thread.sleep(retryDelay);
                }
            }
        }
        return value;
    }
    
    /**
     * Checks if the value has been initialized.
     * 
     * @return true if the value has been initialized, false otherwise
     */
    @Override
    public boolean isInitialized() {
        return initialized;
    }
}