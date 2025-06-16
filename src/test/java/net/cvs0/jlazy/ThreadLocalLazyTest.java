package net.cvs0.jlazy;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

public class ThreadLocalLazyTest {

    @Test
    public void testThreadLocalLazy() throws InterruptedException {
        ThreadLocalLazy<String> threadLocalLazy = new ThreadLocalLazy<>(() -> "ThreadLocal Value");

        assertNull(threadLocalLazy.get(), "Value should not be initialized yet");
        assertFalse(threadLocalLazy.isInitialized(), "Should not be initialized yet");
        
        String result = threadLocalLazy.initialize();
        
        assertEquals("ThreadLocal Value", result, "Value should be 'ThreadLocal Value'");
        assertTrue(threadLocalLazy.isInitialized(), "Should be initialized now");
        assertEquals("ThreadLocal Value", threadLocalLazy.get(), "get() should return the initialized value");
        
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<String> threadValue = new AtomicReference<>();
        AtomicReference<Boolean> threadInitializedBefore = new AtomicReference<>();
        AtomicReference<Boolean> threadInitializedAfter = new AtomicReference<>();
        AtomicReference<Exception> threadException = new AtomicReference<>();
        
        Thread thread = new Thread(() -> {
            try {
                threadInitializedBefore.set(threadLocalLazy.isInitialized());
                
                threadValue.set(threadLocalLazy.initialize());
                
                threadInitializedAfter.set(threadLocalLazy.isInitialized());
            } catch (InterruptedException e) {
                threadException.set(e);
            } finally {
                latch.countDown();
            }
        });
        thread.start();
        latch.await();
        
        assertNull(threadException.get(), "Thread should not have thrown an exception");
        
        assertFalse(threadInitializedBefore.get(), "New thread should not have initialized value initially");
        assertTrue(threadInitializedAfter.get(), "New thread should have initialized value after initialize()");
        assertEquals("ThreadLocal Value", threadValue.get(), "Value should be the same in both threads");
        
        threadLocalLazy.remove();
        assertNull(threadLocalLazy.get(), "Value should be null after remove");
        assertFalse(threadLocalLazy.isInitialized(), "Should not be initialized after remove");
    }
}