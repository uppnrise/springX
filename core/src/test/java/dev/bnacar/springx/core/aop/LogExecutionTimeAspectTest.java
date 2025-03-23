package dev.bnacar.springx.core.aop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class LogExecutionTimeAspectTest {

    private LogExecutionTimeAspect aspect;

    @Mock
    private ProceedingJoinPoint joinPoint;

    @Mock
    private MethodSignature methodSignature;

    @BeforeEach
    public void setup() throws NoSuchMethodException {
        aspect = new LogExecutionTimeAspect();
        TestService testService = new TestService();

        // Setup mocks
        when(joinPoint.getSignature()).thenReturn(methodSignature);
        when(joinPoint.getTarget()).thenReturn(testService);
    }

    @Test
    public void testLogExecutionTimeWithDefaultSettings() throws Throwable {
        // Arrange
        Method method = TestService.class.getMethod("methodWithDefaultLogSettings");
        when(methodSignature.getMethod()).thenReturn(method);
        when(joinPoint.proceed()).thenReturn("result");

        // Act
        Object result = aspect.logExecutionTime(joinPoint);

        // Assert
        assertEquals("result", result);
        verify(joinPoint).proceed();
    }

    @Test
    public void testLogExecutionTimeWithCustomSettings() throws Throwable {
        // Arrange
        Method method = TestService.class.getMethod("methodWithCustomLogSettings", String.class);
        when(methodSignature.getMethod()).thenReturn(method);
        when(joinPoint.proceed()).thenReturn("result");
        when(joinPoint.getArgs()).thenReturn(new Object[]{"input"});

        // Act
        Object result = aspect.logExecutionTime(joinPoint);

        // Assert
        assertEquals("result", result);
        verify(joinPoint).proceed();
    }

    @Test
    public void testLogExecutionTimeWithException() throws Throwable {
        // Arrange
        Method method = TestService.class.getMethod("methodWithDefaultLogSettings");
        when(methodSignature.getMethod()).thenReturn(method);
        when(joinPoint.proceed()).thenThrow(new RuntimeException("Test exception"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> aspect.logExecutionTime(joinPoint));
        verify(joinPoint).proceed();
    }

    // Test service class with annotated methods
    static class TestService {

        @LogExecutionTime
        public String methodWithDefaultLogSettings() {
            return "result";
        }

        @LogExecutionTime(value = LogExecutionTime.LogLevel.INFO, includeArgs = true)
        public String methodWithCustomLogSettings(String input) {
            return "processed: " + input;
        }
    }
}
