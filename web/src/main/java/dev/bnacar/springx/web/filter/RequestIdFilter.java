package dev.bnacar.springx.web.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * Filter that adds a request ID to each incoming HTTP request.
 * The request ID is added to the MDC context for logging and to the response headers.
 */
public class RequestIdFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestIdFilter.class);
    private static final String REQUEST_ID_HEADER = "X-Request-ID";
    private static final String REQUEST_ID_MDC_KEY = "requestId";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String requestId = request.getHeader(REQUEST_ID_HEADER);

        // If no request ID in header, generate one
        if (requestId == null || requestId.isEmpty()) {
            requestId = generateRequestId();
        }

        // Add request ID to MDC for logging
        MDC.put(REQUEST_ID_MDC_KEY, requestId);

        try {
            // Add request ID to response headers
            response.setHeader(REQUEST_ID_HEADER, requestId);

            logger.debug("Processing request with ID: {}", requestId);

            // Continue with the filter chain
            filterChain.doFilter(request, response);
        } finally {
            // Clean up MDC
            MDC.remove(REQUEST_ID_MDC_KEY);
        }
    }

    /**
     * Generates a unique request ID.
     *
     * @return a unique request ID
     */
    protected String generateRequestId() {
        return UUID.randomUUID().toString();
    }
}
