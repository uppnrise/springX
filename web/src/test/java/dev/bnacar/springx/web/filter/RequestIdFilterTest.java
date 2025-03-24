package dev.bnacar.springx.web.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RequestIdFilterTest {

    private RequestIdFilter filter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @BeforeEach
    public void setup() {
        filter = new TestRequestIdFilter();
    }

    @Test
    public void testDoFilterInternalWithExistingRequestId() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("X-Request-ID")).thenReturn("existing-request-id");

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(response).setHeader("X-Request-ID", "existing-request-id");
        verify(filterChain).doFilter(request, response);
        assertNull(MDC.get("requestId")); // MDC should be cleared after filter
    }

    @Test
    public void testDoFilterInternalWithoutExistingRequestId() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("X-Request-ID")).thenReturn(null);

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(response).setHeader(eq("X-Request-ID"), anyString());
        verify(filterChain).doFilter(request, response);
        assertNull(MDC.get("requestId")); // MDC should be cleared after filter
    }

    @Test
    public void testDoFilterInternalWithException() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("X-Request-ID")).thenReturn("existing-request-id");
        doThrow(new RuntimeException("Test exception")).when(filterChain).doFilter(request, response);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> filter.doFilterInternal(request, response, filterChain));
        verify(response).setHeader("X-Request-ID", "existing-request-id");
        assertNull(MDC.get("requestId")); // MDC should be cleared even if exception occurs
    }

    // Test implementation that returns a fixed request ID for testing
    static class TestRequestIdFilter extends RequestIdFilter {
        @Override
        protected String generateRequestId() {
            return "test-request-id";
        }
    }
}
