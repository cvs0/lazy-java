package net.cvs0.jlazy;

import java.util.function.Supplier;

/**
 * A lazy initialization implementation that provides thread-local values.
 * 
 * @param <T> The type of value to be lazily initialized
 */
public class ThreadLocalLazy<T> implements LazyInitializer<T> {
    private final Supplier<T> initializer;
    private final ThreadLocal<T> value = new ThreadLocal<>();
    private final ThreadLocal<Boolean> initialized = ThreadLocal.withInitial(() -> false);

    /**
     * Creates a new ThreadLocalLazy instance.
     * 
     * @param initializer The supplier function that will initialize the value
     */
    public ThreadLocalLazy(Supplier<T> initializer) {
        this.initializer = initializer;
    }

    /**
     * Gets the thread-local value if it's already initialized, otherwise returns null.
     * This method does not trigger initialization.
     * 
     * @return The initialized thread-local value or null if not yet initialized
     */
    @Override
    public T get() {
        if (!initialized.get()) {
            return null;
        }
        return value.get();
    }
    
    /**
     * Initializes the thread-local value if not already initialized.
     * 
     * @return The initialized thread-local value
     * @throws InterruptedException if the initialization process is interrupted
     */
    @Override
    public T initialize() throws InterruptedException {
        if (!initialized.get()) {
            synchronized (this) {
                if (!initialized.get()) {
                    value.set(initializer.get());
                    initialized.set(true);
                }
            }
        }
        return value.get();
    }
    
    /**
     * Checks if the thread-local value has been initialized.
     * 
     * @return true if the thread-local value has been initialized, false otherwise
     */
    @Override
    public boolean isInitialized() {
        return initialized.get();
    }
    
    /**
     * Removes the thread-local value for the current thread.
     */
    public void remove() {
        value.remove();
        initialized.remove();
    }
}