package net.cvs0.jlazy;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.atomic.AtomicInteger;

public class LazyWithRetryTest {

    @Test
    public void testLazyWithRetry() throws InterruptedException {
        int maxRetries = 3;
        long retryDelay = 10;
        
        AtomicInteger counter = new AtomicInteger(0);
        
        LazyWithRetry<String> retryLazy = new LazyWithRetry<>(() -> {
            if (counter.incrementAndGet() >= 2) {
                return "Retried Value";
            }
            throw new RuntimeException("Controlled Failure");
        }, maxRetries, retryDelay);
        
        assertNull(retryLazy.get(), "Value should not be initialized yet");
        assertFalse(retryLazy.isInitialized(), "Should not be initialized yet");
        
        String result = retryLazy.initialize();
        
        assertEquals("Retried Value", result, "The value should match expected value after retries");
        assertTrue(retryLazy.isInitialized(), "Should be initialized now");
        assertEquals(2, counter.get(), "Should have attempted exactly twice");
        
        assertEquals(result, retryLazy.get(), "get() should return the initialized value");
    }
    
    @Test
    public void testRetryExhaustion() {
        int maxRetries = 3;
        long retryDelay = 10;
        
        AtomicInteger counter = new AtomicInteger(0);
        
        LazyWithRetry<String> retryLazy = new LazyWithRetry<>(() -> {
            counter.incrementAndGet();
            throw new RuntimeException("Always Fails");
        }, maxRetries, retryDelay);
        
        Exception exception = assertThrows(RuntimeException.class, () -> retryLazy.initialize(),
                "Should throw exception after all retries are exhausted");
        
        assertEquals("Always Fails", exception.getMessage(), "Exception message should match");
        assertEquals(maxRetries, counter.get(), "Should have attempted exactly the maximum number of retries");
    }
}