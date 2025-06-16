package net.cvs0.jlazy;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LazyCacheTest {

    @Test
    public void testCacheInitialization() throws InterruptedException {
        LazyCache<Integer, String> cache = new LazyCache<>();
        
        Lazy<String> lazyValue = cache.getOrCreate(1, () -> "Lazy Value");
        
        assertEquals("Lazy Value", lazyValue.get(), "Value should be 'Lazy Value'");
        assertTrue(lazyValue.isInitialized(), "Value should be initialized");
        
        Lazy<String> cachedValue = cache.getOrCreate(1, () -> "New Value");
        
        assertSame(lazyValue, cachedValue, "Cache should return the same instance");
        assertEquals("Lazy Value", cachedValue.get(), "Cached value should be the original value");
    }
    
    @Test
    public void testCacheOperations() throws InterruptedException {
        LazyCache<String, Integer> cache = new LazyCache<>();
        
        cache.getOrCreate("one", () -> 1);
        cache.getOrCreate("two", () -> 2);
        
        assertEquals(2, cache.size(), "Cache should have 2 entries");
        
        assertTrue(cache.containsKey("one"), "Cache should contain key 'one'");
        assertFalse(cache.containsKey("three"), "Cache should not contain key 'three'");
        
        assertTrue(cache.remove("one"), "Remove should return true for existing key");
        assertFalse(cache.containsKey("one"), "Cache should not contain removed key");
        assertEquals(1, cache.size(), "Cache should have 1 entry after removal");
        
        cache.clear();
        assertEquals(0, cache.size(), "Cache should be empty after clear");
    }
}