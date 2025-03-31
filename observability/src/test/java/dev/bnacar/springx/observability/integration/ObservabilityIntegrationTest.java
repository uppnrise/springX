package dev.bnacar.springx.observability.integration;

import dev.bnacar.springx.observability.annotation.Metric;
import dev.bnacar.springx.observability.aspect.MetricAspect;
import dev.bnacar.springx.observability.health.MemoryHealthIndicator;
import dev.bnacar.springx.observability.tracing.TraceInterceptor;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Service;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {ObservabilityIntegrationTest.TestConfig.class})
public class ObservabilityIntegrationTest {

    @Autowired
    private TestService testService;

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    private MemoryHealthIndicator memoryHealthIndicator;

    @Autowired
    private TraceInterceptor traceInterceptor;

    @BeforeEach
    public void setup() {
        // Clear the meter registry before each test
        SimpleMeterRegistry simpleMeterRegistry = (SimpleMeterRegistry) meterRegistry;
        simpleMeterRegistry.clear();
    }

    @Test
    public void testMetricAspect() {
        // Execute method with Metric annotation
        testService.metricMethod("test");

        // Verify metrics were recorded
        Counter counter = meterRegistry.find("TestService.metricMethod.count").counter();
        assertNotNull(counter);
        assertEquals(1, counter.count());

        Timer timer = meterRegistry.find("TestService.metricMethod.time").timer();
        assertNotNull(timer);
        assertEquals(1, timer.count());
    }

    @Test
    public void testMetricAspectWithException() {
        // Execute method with Metric annotation that throws an exception
        assertThrows(RuntimeException.class, () -> testService.metricMethodWithException());

        // Verify metrics were recorded
        Counter counter = meterRegistry.find("TestService.metricMethodWithException.count").counter();
        assertNotNull(counter);
        assertEquals(1, counter.count());

        Timer timer = meterRegistry.find("TestService.metricMethodWithException.time").timer();
        assertNotNull(timer);
        assertEquals(1, timer.count());

        Counter exceptionCounter = meterRegistry.find("TestService.metricMethodWithException.exceptions")
                .tag("exception", "RuntimeException").counter();
        assertNotNull(exceptionCounter);
        assertEquals(1, exceptionCounter.count());
    }

    @Test
    public void testMemoryHealthIndicator() {
        // Get health status
        Health health = memoryHealthIndicator.health();

        // Verify health status
        assertNotNull(health);
        // Status could be UP or WARNING depending on the current memory usage
        assertTrue(health.getStatus().equals(Status.UP) || health.getStatus().getCode().equals("WARNING"));
        assertNotNull(health.getDetails().get("heap.used"));
        assertNotNull(health.getDetails().get("heap.max"));
        assertNotNull(health.getDetails().get("heap.usage"));
    }

    @Test
    public void testTraceInterceptor() throws Exception {
        // Create mock request and response
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Execute the interceptor
        boolean result = traceInterceptor.preHandle(request, response, null);

        // Verify the result
        assertTrue(result);
        assertNotNull(response.getHeader("X-Trace-ID"));

        // Clean up
        traceInterceptor.afterCompletion(request, response, null, null);
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
        public MeterRegistry meterRegistry() {
            return new SimpleMeterRegistry();
        }

        @Bean
        public MetricAspect metricAspect(MeterRegistry meterRegistry) {
            return new MetricAspect(meterRegistry);
        }

        @Bean
        public MemoryHealthIndicator memoryHealthIndicator() {
            return new MemoryHealthIndicator(0.7, 0.9);
        }

        @Bean
        public TraceInterceptor traceInterceptor() {
            return new TraceInterceptor();
        }
    }

    @Service
    static class TestService {

        @Metric
        public void metricMethod(String input) {
            try {
                Thread.sleep(100); // Simulate some work
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        @Metric(recordExceptions = true)
        public void metricMethodWithException() {
            throw new RuntimeException("Test exception");
        }
    }
}

