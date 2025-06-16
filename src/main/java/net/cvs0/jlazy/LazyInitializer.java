package net.cvs0.jlazy;

/**
 * Common interface for all lazy initialization implementations.
 * @param <T> The type of value to be lazily initialized
 */
public interface LazyInitializer<T> {
    
    /**
     * Gets the value if it's already initialized, otherwise returns null.
     * This method does not trigger initialization.
     * 
     * @return The initialized value or null if not yet initialized
     */
    T get();
    
    /**
     * Initializes the value if not already initialized and returns it.
     * 
     * @return The initialized value
     * @throws InterruptedException if the initialization process is interrupted
     */
    T initialize() throws InterruptedException;
    
    /**
     * Checks if the value has been initialized.
     * 
     * @return true if the value has been initialized, false otherwise
     */
    boolean isInitialized();
}