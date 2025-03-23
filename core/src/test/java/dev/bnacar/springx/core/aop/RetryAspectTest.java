package dev.bnacar.springx.core.aop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeoutException;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RetryAspectTest {

    private RetryAspect aspect;

    @Mock
    private ProceedingJoinPoint joinPoint;

    @Mock
    private MethodSignature methodSignature;

    @BeforeEach
    public void setup() throws NoSuchMethodException {
        aspect = new RetryAspect();
        TestService testService = new TestService();

        // Setup mocks
        when(joinPoint.getSignature()).thenReturn(methodSignature);
        when(joinPoint.getTarget()).thenReturn(testService);
    }

    @Test
    public void testRetrySuccessOnFirstAttempt() throws Throwable {
        // Arrange
        Method method = TestService.class.getMethod("basicRetryMethod");
        when(methodSignature.getMethod()).thenReturn(method);
        when(joinPoint.proceed()).thenReturn("result");

        // Act
        Object result = aspect.retry(joinPoint);

        // Assert
        assertEquals("result", result);
        verify(joinPoint, times(1)).proceed();
    }

    @Test
    public void testRetrySuccessAfterFailures() throws Throwable {
        // Arrange
        Method method = TestService.class.getMethod("basicRetryMethod");
        when(methodSignature.getMethod()).thenReturn(method);

        // First two calls throw exception, third succeeds
        when(joinPoint.proceed())
                .thenThrow(new IOException("Test exception"))
                .thenThrow(new IOException("Test exception"))
                .thenReturn("result");

        // Act
        Object result = aspect.retry(joinPoint);

        // Assert
        assertEquals("result", result);
        verify(joinPoint, times(3)).proceed();
    }

    @Test
    public void testRetryMaxAttemptsExceeded() throws Throwable {
        // Arrange
        Method method = TestService.class.getMethod("basicRetryMethod");
        when(methodSignature.getMethod()).thenReturn(method);

        // All calls throw exception
        when(joinPoint.proceed()).thenThrow(new IOException("Test exception"));

        // Act & Assert
        assertThrows(IOException.class, () -> aspect.retry(joinPoint));
        verify(joinPoint, times(3)).proceed(); // Default maxAttempts is 3
    }

    @Test
    public void testRetryWithSpecificExceptionTypes() throws Throwable {
        // Arrange
        Method method = TestService.class.getMethod("retryWithSpecificExceptions");
        when(methodSignature.getMethod()).thenReturn(method);

        // Throw an exception that should be retried
        when(joinPoint.proceed())
                .thenThrow(new IOException("Test exception"))
                .thenReturn("result");

        // Act
        Object result = aspect.retry(joinPoint);

        // Assert
        assertEquals("result", result);
        verify(joinPoint, times(2)).proceed();
    }

    @Test
    public void testRetryWithNoRetryExceptionTypes() throws Throwable {
        // Arrange
        Method method = TestService.class.getMethod("retryWithSpecificExceptions");
        when(methodSignature.getMethod()).thenReturn(method);

        // Throw an exception that should not be retried
        when(joinPoint.proceed()).thenThrow(new IllegalArgumentException("Test exception"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> aspect.retry(joinPoint));
        verify(joinPoint, times(1)).proceed(); // Should not retry
    }

    // Test service class with annotated methods
    static class TestService {

        @Retry(maxAttempts = 3, backoffMs = 10) // Using small backoff for tests
        public String basicRetryMethod() {
            return "result";
        }

        @Retry(
                maxAttempts = 5,
                exponential = true,
                backoffMs = 10, // Using small backoff for tests
                retryOn = {IOException.class, TimeoutException.class},
                noRetryOn = {IllegalArgumentException.class}
        )
        public String retryWithSpecificExceptions() {
            return "result";
        }
    }
}
