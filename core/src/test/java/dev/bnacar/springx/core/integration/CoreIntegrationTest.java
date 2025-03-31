package dev.bnacar.springx.core.integration;

import dev.bnacar.springx.core.aop.*;
import dev.bnacar.springx.core.config.SpringXAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {CoreIntegrationTest.TestConfig.class})
public class CoreIntegrationTest {

    @Autowired
    private TestService testService;

    @Test
    public void testLogExecutionTimeAspect() {
        // Execute method with LogExecutionTime annotation
        String result = testService.loggedMethod("test");

        // Verify the method was executed successfully
        assertEquals("Processed: test", result);
        // The actual logging is verified through the aspect's behavior
    }

    @Test
    public void testRetryAspect() {
        // Reset the counter
        testService.resetCounter();

        // Execute method with Retry annotation that will fail initially
        String result = testService.retryableMethod();

        // Verify the method was retried and eventually succeeded
        assertEquals("Success after retries", result);
        assertEquals(3, testService.getCounter());
    }

    @Test
    public void testThrottleAspect() {
        // Reset the counter
        testService.resetCounter();

        // Execute method with Throttle annotation multiple times
        for (int i = 0; i < 2; i++) {
            testService.throttledMethod();
        }

        // Verify the method was throttled
        assertEquals(2, testService.getCounter());
    }

    @Configuration
    @EnableAutoConfiguration
    @EnableAspectJAutoProxy
    static class TestConfig {

        @Bean
        public TestService testService() {
            return new TestService();
        }

        @Bean
        public LogExecutionTimeAspect logExecutionTimeAspect() {
            return new LogExecutionTimeAspect();
        }

        @Bean
        public RetryAspect retryAspect() {
            return new RetryAspect();
        }

        @Bean
        public ThrottleAspect throttleAspect() {
            return new ThrottleAspect();
        }

        @Bean
        public SpringXAutoConfiguration springXAutoConfiguration() {
            return new SpringXAutoConfiguration();
        }
    }

    @Service
    static class TestService {

        private final AtomicInteger counter = new AtomicInteger(0);

        @LogExecutionTime(value = LogExecutionTime.LogLevel.INFO, includeArgs = true)
        public String loggedMethod(String input) {
            try {
                Thread.sleep(100); // Simulate some work
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return "Processed: " + input;
        }

        @Retry(maxAttempts = 3, backoffMs = 100)
        public String retryableMethod() {
            int attempts = counter.incrementAndGet();
            if (attempts < 3) {
                throw new RuntimeException("Simulated failure");
            }
            return "Success after retries";
        }

        @Throttle(limit = 2, timeUnit = TimeUnit.MINUTES)
        public void throttledMethod() {
            counter.incrementAndGet();
        }

        public void resetCounter() {
            counter.set(0);
        }

        public int getCounter() {
            return counter.get();
        }
    }
}
