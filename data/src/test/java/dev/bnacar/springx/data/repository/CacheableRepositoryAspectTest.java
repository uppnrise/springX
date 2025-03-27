package dev.bnacar.springx.data.repository;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CacheableRepositoryAspectTest {

    private CacheableRepositoryAspect aspect;

    @Mock
    private ProceedingJoinPoint joinPoint;

    @Mock
    private MethodSignature methodSignature;

    @BeforeEach
    public void setup() throws NoSuchMethodException {
        aspect = new CacheableRepositoryAspect();

        // Setup mocks
        when(joinPoint.getSignature()).thenReturn(methodSignature);
    }

    @Test
    public void testCacheableFirstCall() throws Throwable {
        // Arrange
        Method method = TestRepository.class.getMethod("findById", Long.class);
        when(methodSignature.getMethod()).thenReturn(method);
        when(joinPoint.getArgs()).thenReturn(new Object[]{1L});
        when(joinPoint.proceed()).thenReturn("Result for ID 1");

        // Act
        Object result = aspect.cacheable(joinPoint);

        // Assert
        assertEquals("Result for ID 1", result);
        verify(joinPoint, times(1)).proceed();
    }

    @Test
    public void testCacheableSecondCallCached() throws Throwable {
        // Arrange
        Method method = TestRepository.class.getMethod("findById", Long.class);
        when(methodSignature.getMethod()).thenReturn(method);
        when(joinPoint.getArgs()).thenReturn(new Object[]{1L});
        when(joinPoint.proceed()).thenReturn("Result for ID 1");

        // Act - First call
        Object result1 = aspect.cacheable(joinPoint);

        // Act - Second call with same parameters
        Object result2 = aspect.cacheable(joinPoint);

        // Assert
        assertEquals("Result for ID 1", result1);
        assertEquals("Result for ID 1", result2);
        verify(joinPoint, times(1)).proceed(); // Should only proceed once
    }

    @Test
    public void testCacheableDifferentParameters() throws Throwable {
        // Arrange
        Method method = TestRepository.class.getMethod("findById", Long.class);
        when(methodSignature.getMethod()).thenReturn(method);

        // First call setup
        when(joinPoint.getArgs()).thenReturn(new Object[]{1L});
        when(joinPoint.proceed()).thenReturn("Result for ID 1");

        // Act - First call
        Object result1 = aspect.cacheable(joinPoint);

        // Second call setup with different parameters
        when(joinPoint.getArgs()).thenReturn(new Object[]{2L});
        when(joinPoint.proceed()).thenReturn("Result for ID 2");

        // Act - Second call with different parameters
        Object result2 = aspect.cacheable(joinPoint);

        // Assert
        assertEquals("Result for ID 1", result1);
        assertEquals("Result for ID 2", result2);
        verify(joinPoint, times(2)).proceed(); // Should proceed twice for different parameters
    }

    @Test
    public void testCacheableWithNullResult() throws Throwable {
        // Arrange
        Method method = TestRepository.class.getMethod("findByIdWithNullCaching", Long.class);
        when(methodSignature.getMethod()).thenReturn(method);
        when(joinPoint.getArgs()).thenReturn(new Object[]{1L});
        when(joinPoint.proceed()).thenReturn(null);

        // Act - First call
        Object result1 = aspect.cacheable(joinPoint);

        // Act - Second call with same parameters
        Object result2 = aspect.cacheable(joinPoint);

        // Assert
        assertNull(result1);
        assertNull(result2);
        verify(joinPoint, times(1)).proceed(); // Should only proceed once even for null result
    }

    @Test
    public void testCacheableWithNoNullCaching() throws Throwable {
        // Arrange
        Method method = TestRepository.class.getMethod("findByIdWithNoNullCaching", Long.class);
        when(methodSignature.getMethod()).thenReturn(method);
        when(joinPoint.getArgs()).thenReturn(new Object[]{1L});
        when(joinPoint.proceed()).thenReturn(null);

        // Act - First call
        Object result1 = aspect.cacheable(joinPoint);

        // Act - Second call with same parameters
        Object result2 = aspect.cacheable(joinPoint);

        // Assert
        assertNull(result1);
        assertNull(result2);
        verify(joinPoint, times(2)).proceed(); // Should proceed twice because null results are not cached
    }

    // Test repository class with annotated methods
    static class TestRepository {

        @Cacheable(ttlSeconds = 300)
        public String findById(Long id) {
            return "Result for ID " + id;
        }

        @Cacheable(ttlSeconds = 300, cacheNull = true)
        public String findByIdWithNullCaching(Long id) {
            return null;
        }

        @Cacheable(ttlSeconds = 300, cacheNull = false)
        public String findByIdWithNoNullCaching(Long id) {
            return null;
        }
    }
}
