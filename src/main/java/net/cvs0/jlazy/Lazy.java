package net.cvs0.jlazy;

import java.util.function.Supplier;

/**
 * Basic implementation of lazy initialization.
 * @param <T> The type of value to be lazily initialized
 */
public class Lazy<T> implements LazyInitializer<T> {
    private final Supplier<T> initializer;
    private T value;
    private boolean initialized = false;

    /**
     * Creates a new Lazy instance with the given initializer.
     * 
     * @param initializer The supplier function that will initialize the value when needed
     */
    public Lazy(Supplier<T> initializer) {
        this.initializer = initializer;
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
     * Initializes the value if not already initialized and returns it.
     * 
     * @return The initialized value
     * @throws InterruptedException if the initialization process is interrupted
     */
    @Override
    public T initialize() throws InterruptedException {
        if (!initialized) {
            initialized = true;
            value = initializer.get();
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