package dev.bnacar.springx.core.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Aspect that handles the {@link Retry} annotation.
 * Automatically retries methods annotated with {@link Retry} on failure.
 */
@Aspect
@Component
@ConditionalOnProperty(name = "spring-x.aop.retry.enabled", havingValue = "true", matchIfMissing = true)
public class RetryAspect {

    private static final Logger logger = LoggerFactory.getLogger(RetryAspect.class);

    /**
     * Intercepts method calls annotated with {@link Retry} and applies retry logic.
     *
     * @param joinPoint the join point representing the intercepted method call
     * @return the result of the method call
     * @throws Throwable if the method call throws an exception after all retry attempts
     */
    @Around("@annotation(dev.bnacar.springx.core.aop.Retry)")
    public Object retry(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Retry annotation = method.getAnnotation(Retry.class);

        String methodName = method.getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        // Convert retryOn and noRetryOn arrays to Sets for easier checking
        Set<Class<? extends Throwable>> retryOnSet = annotation.retryOn().length > 0 ?
                Arrays.stream(annotation.retryOn()).collect(Collectors.toSet()) :
                Collections.emptySet();

        Set<Class<? extends Throwable>> noRetryOnSet = Arrays.stream(annotation.noRetryOn())
                .collect(Collectors.toSet());

        int maxAttempts = annotation.maxAttempts();
        long backoffMs = annotation.backoffMs();
        boolean exponential = annotation.exponential();

        int attempt = 1;
        Throwable lastException = null;

        while (attempt <= maxAttempts) {
            try {
                if (attempt > 1) {
                    logger.debug("Retry attempt {} of {} for {}.{}",
                            attempt, maxAttempts, className, methodName);
                }

                return joinPoint.proceed();

            } catch (Throwable e) {
                lastException = e;

                // Check if we should retry based on exception type
                if (!shouldRetry(e, retryOnSet, noRetryOnSet)) {
                    logger.debug("Not retrying {}.{} for exception type {}",
                            className, methodName, e.getClass().getName());
                    throw e;
                }

                // If this was the last attempt, throw the exception
                if (attempt >= maxAttempts) {
                    logger.debug("Max retry attempts ({}) reached for {}.{}",
                            maxAttempts, className, methodName);
                    throw e;
                }

                // Calculate backoff time for next attempt
                long currentBackoff = exponential ?
                        backoffMs * (long)Math.pow(2, attempt - 1) : backoffMs;

                logger.debug("Waiting {}ms before retry attempt {} for {}.{}",
                        currentBackoff, attempt + 1, className, methodName);

                try {
                    Thread.sleep(currentBackoff);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw e;
                }

                attempt++;
            }
        }

        // This should never happen, but just in case
        throw lastException != null ? lastException :
                new IllegalStateException("Unexpected error in retry logic");
    }

    /**
     * Determines whether a method should be retried based on the exception type.
     *
     * @param exception the exception that was thrown
     * @param retryOnSet set of exception types that should trigger a retry
     * @param noRetryOnSet set of exception types that should not trigger a retry
     * @return true if the method should be retried, false otherwise
     */
    private boolean shouldRetry(Throwable exception,
                                Set<Class<? extends Throwable>> retryOnSet,
                                Set<Class<? extends Throwable>> noRetryOnSet) {

        // First check noRetryOn - these take precedence
        for (Class<? extends Throwable> noRetryType : noRetryOnSet) {
            if (noRetryType.isInstance(exception)) {
                return false;
            }
        }

        // If retryOn is empty, retry all exceptions not in noRetryOn
        if (retryOnSet.isEmpty()) {
            return true;
        }

        // Otherwise, only retry exceptions in retryOn
        for (Class<? extends Throwable> retryType : retryOnSet) {
            if (retryType.isInstance(exception)) {
                return true;
            }
        }

        return false;
    }
}
