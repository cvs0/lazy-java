package net.cvs0.jlazy;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LazyTest {

    @Test
    public void testLazyInitialization() throws InterruptedException {
        Lazy<String> lazy = new Lazy<>(() -> "Hello, Lazy!");
        
        assertNull(lazy.get(), "Value should not be initialized yet");
        assertFalse(lazy.isInitialized(), "Should not be initialized yet");
        
        String result = lazy.initialize();
        
        assertEquals("Hello, Lazy!", result, "Lazy initialization failed");
        assertTrue(lazy.isInitialized(), "Should be initialized now");
        
        assertEquals("Hello, Lazy!", lazy.get(), "Lazy get() should return initialized value");
    }
    
    @Test
    public void testMultipleInitializations() throws InterruptedException {
        int[] counter = {0};
        Lazy<String> lazy = new Lazy<>(() -> {
            counter[0]++;
            return "Initialized " + counter[0] + " time(s)";
        });
        
        String result1 = lazy.initialize();
        assertEquals("Initialized 1 time(s)", result1, "First initialization failed");
        assertEquals(1, counter[0], "Initializer should be called exactly once");
        
        String result2 = lazy.initialize();
        assertEquals("Initialized 1 time(s)", result2, "Second initialization should return the same value");
        assertEquals(1, counter[0], "Initializer should still be called exactly once");
    }
}