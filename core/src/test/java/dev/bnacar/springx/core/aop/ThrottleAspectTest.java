package dev.bnacar.springx.core.aop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ThrottleAspectTest {

    private ThrottleAspect aspect;

    @Mock
    private ProceedingJoinPoint joinPoint;

    @Mock
    private MethodSignature methodSignature;

    @BeforeEach
    public void setup() throws NoSuchMethodException {
        aspect = new ThrottleAspect();
        TestService testService = new TestService();

        // Setup mocks
        when(joinPoint.getSignature()).thenReturn(methodSignature);
        when(joinPoint.getTarget()).thenReturn(testService);
    }

    @Test
    public void testThrottleWithinLimit() throws Throwable {
        // Arrange
        Method method = TestService.class.getMethod("throttledMethod");
        when(methodSignature.getMethod()).thenReturn(method);
        when(joinPoint.proceed()).thenReturn("result");

        // Act
        Object result = aspect.throttle(joinPoint);

        // Assert
        assertEquals("result", result);
        verify(joinPoint).proceed();
    }

    @Test
    public void testThrottleExceedsLimitWithException() throws Throwable {
        // Arrange
        Method method = TestService.class.getMethod("throttledMethodWithException");
        when(methodSignature.getMethod()).thenReturn(method);
        when(joinPoint.proceed()).thenReturn("result");

        // Act & Assert
        // First call should succeed
        Object result = aspect.throttle(joinPoint);
        assertEquals("result", result);

        // Simulate exceeding the limit by calling multiple times
        for (int i = 0; i < 4; i++) {
            aspect.throttle(joinPoint);
        }

        // Next call should throw exception
        assertThrows(ThrottleExceededException.class, () -> aspect.throttle(joinPoint));
    }

    @Test
    public void testThrottleExceedsLimitWithDefaultReturn() throws Throwable {
        // Arrange
        Method method = TestService.class.getMethod("throttledMethodWithDefaultReturn");
        when(methodSignature.getMethod()).thenReturn(method);
        when(joinPoint.proceed()).thenReturn("result");

        // Act
        // First call should succeed
        Object result = aspect.throttle(joinPoint);
        assertEquals("result", result);

        // Simulate exceeding the limit by calling multiple times
        for (int i = 0; i < 5; i++) {
            aspect.throttle(joinPoint);
        }

        // Next call should return default value
        result = aspect.throttle(joinPoint);
        assertNull(result);
    }

    // Test service class with annotated methods
    static class TestService {

        @Throttle(limit = 10, timeUnit = TimeUnit.MINUTES)
        public String throttledMethod() {
            return "result";
        }

        @Throttle(limit = 5, timeUnit = TimeUnit.SECONDS, behavior = Throttle.ThrottleBehavior.THROW_EXCEPTION)
        public String throttledMethodWithException() {
            return "result";
        }

        @Throttle(limit = 5, timeUnit = TimeUnit.SECONDS, behavior = Throttle.ThrottleBehavior.RETURN_DEFAULT)
        public String throttledMethodWithDefaultReturn() {
            return "result";
        }
    }
}
