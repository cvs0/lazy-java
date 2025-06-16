package net.cvs0.jlazy;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.TimeUnit;

public class ExpiringLazyTest {

    @Test
    public void testLazyWithExpiration() throws InterruptedException {
        ExpiringLazy<String> expiringLazy = new ExpiringLazy<>(() -> "Expiring Value", 1000);
        
        assertNull(expiringLazy.get(), "Value should not be initialized yet");
        assertFalse(expiringLazy.isInitialized(), "Should not be initialized yet");
        
        String result = expiringLazy.initialize();
        
        assertEquals("Expiring Value", result, "Value should be 'Expiring Value'");
        assertTrue(expiringLazy.isInitialized(), "Should be initialized now");
        assertEquals("Expiring Value", expiringLazy.get(), "get() should return the initialized value");
        
        TimeUnit.MILLISECONDS.sleep(1500);
        
        assertNull(expiringLazy.get(), "Value should be null after expiration");
        assertFalse(expiringLazy.isInitialized(), "Should not be initialized after expiration");
        
        String newResult = expiringLazy.initialize();
        assertEquals("Expiring Value", newResult, "Value should be re-initialized after expiration");
        assertTrue(expiringLazy.isInitialized(), "Should be initialized again");
    }
    
    @Test
    public void testExpirationTimeTracking() throws InterruptedException {
        long expirationTime = 5000;
        ExpiringLazy<String> expiringLazy = new ExpiringLazy<>(() -> "Test Value", expirationTime);
        
        assertEquals(expirationTime, expiringLazy.getExpirationTimeInMillis(),
                "Expiration time should match the configured value");
        
        expiringLazy.initialize();
        assertTrue(expiringLazy.getTimeSinceLastAccess() < 1000, 
                "Time since last access should be less than 1 second after initialization");
    }
}