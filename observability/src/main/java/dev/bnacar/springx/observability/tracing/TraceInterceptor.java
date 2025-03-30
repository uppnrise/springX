package dev.bnacar.springx.observability.tracing;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

/**
 * Interceptor that adds a trace ID to each request.
 * The trace ID is added to the MDC context for logging and to the response headers.
 */
public class TraceInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(TraceInterceptor.class);
    private static final String TRACE_ID_HEADER = "X-Trace-ID";
    private static final String TRACE_ID_MDC_KEY = "traceId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String traceId = request.getHeader(TRACE_ID_HEADER);

        // If no trace ID in header, generate one
        if (traceId == null || traceId.isEmpty()) {
            traceId = generateTraceId();
        }

        // Add trace ID to MDC for logging
        MDC.put(TRACE_ID_MDC_KEY, traceId);

        // Add trace ID to response headers
        response.setHeader(TRACE_ID_HEADER, traceId);

        logger.debug("Request traced with ID: {}", traceId);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // Clean up MDC
        MDC.remove(TRACE_ID_MDC_KEY);
    }

    /**
     * Generates a unique trace ID.
     *
     * @return a unique trace ID
     */
    protected String generateTraceId() {
        return UUID.randomUUID().toString();
    }
}
