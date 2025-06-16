package net.cvs0.jlazy;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * A cache for lazy-initialized values, indexed by keys.
 * 
 * @param <K> The type of keys used to index the cache
 * @param <V> The type of values stored in the cache
 */
public class LazyCache<K, V> {
    private final Map<K, Lazy<V>> cache = new HashMap<>();

    /**
     * Gets a lazy value from the cache or creates a new one if not present.
     * 
     * @param key The key to look up in the cache
     * @param initializer The supplier function to initialize the value if not in cache
     * @return A Lazy instance that will provide the value
     * @throws InterruptedException if the initialization process is interrupted
     */
    public Lazy<V> getOrCreate(K key, Supplier<V> initializer) throws InterruptedException {
        Lazy<V> lazy = cache.computeIfAbsent(key, k -> new Lazy<>(initializer));
        
        // Initialize the value immediately for the cache
        lazy.initialize();
        
        return lazy;
    }
    
    /**
     * Removes a key from the cache.
     * 
     * @param key The key to remove
     * @return true if the key was present and removed, false otherwise
     */
    public boolean remove(K key) {
        return cache.remove(key) != null;
    }
    
    /**
     * Clears all entries from the cache.
     */
    public void clear() {
        cache.clear();
    }
    
    /**
     * Checks if a key exists in the cache.
     * 
     * @param key The key to check
     * @return true if the key exists in the cache, false otherwise
     */
    public boolean containsKey(K key) {
        return cache.containsKey(key);
    }
    
    /**
     * Gets the number of entries in the cache.
     * 
     * @return The number of entries
     */
    public int size() {
        return cache.size();
    }
}