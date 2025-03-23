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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Aspect that handles the {@link Throttle} annotation.
 * Applies rate limiting to methods annotated with {@link Throttle}.
 */
@Aspect
@Component
@ConditionalOnProperty(name = "spring-boost.aop.throttle.enabled", havingValue = "true", matchIfMissing = true)
public class ThrottleAspect {

    private static final Logger logger = LoggerFactory.getLogger(ThrottleAspect.class);

    // Map to store invocation counters for each method
    private final ConcurrentHashMap<String, AtomicInteger> invocationCounters = new ConcurrentHashMap<>();

    // Executor service for resetting counters
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    /**
     * Intercepts method calls annotated with {@link Throttle} and applies rate limiting.
     *
     * @param joinPoint the join point representing the intercepted method call
     * @return the result of the method call or a default value if throttled
     * @throws Throwable if the method call throws an exception or if throttled with THROW_EXCEPTION behavior
     */
    @Around("@annotation(dev.bnacar.springx.core.aop.Throttle)")
    public Object throttle(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Throttle annotation = method.getAnnotation(Throttle.class);

        String methodKey = generateMethodKey(joinPoint);

        // Get or create counter for this method
        AtomicInteger counter = invocationCounters.computeIfAbsent(methodKey, k -> {
            AtomicInteger newCounter = new AtomicInteger(0);

            // Schedule reset of counter based on time unit
            scheduler.scheduleAtFixedRate(
                    () -> newCounter.set(0),
                    1,
                    1,
                    annotation.timeUnit()
            );

            return newCounter;
        });

        // Check if limit is exceeded
        int currentCount = counter.incrementAndGet();
        if (currentCount > annotation.limit()) {
            logger.debug("Throttle limit exceeded for method: {}", methodKey);

            switch (annotation.behavior()) {
                case THROW_EXCEPTION:
                    throw new ThrottleExceededException("Rate limit exceeded for method: " + methodKey);

                case RETURN_DEFAULT:
                    logger.debug("Returning default value due to throttling");
                    return getDefaultReturnValue(method.getReturnType());

                case BLOCK:
                    logger.debug("Blocking until throttle limit resets");
                    while (counter.get() > annotation.limit()) {
                        Thread.sleep(100);
                    }
                    return joinPoint.proceed();

                default:
                    throw new IllegalStateException("Unknown throttle behavior: " + annotation.behavior());
            }
        }

        // Proceed with method execution
        return joinPoint.proceed();
    }

    /**
     * Generates a unique key for a method to use in the invocation counter map.
     *
     * @param joinPoint the join point representing the method
     * @return a unique key for the method
     */
    private String generateMethodKey(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return joinPoint.getTarget().getClass().getName() + "." + signature.getMethod().getName();
    }

    /**
     * Returns a default value for the given return type.
     *
     * @param returnType the return type of the method
     * @return a default value for the return type
     */
    private Object getDefaultReturnValue(Class<?> returnType) {
        if (returnType.equals(Void.TYPE)) {
            return null;
        } else if (returnType.isPrimitive()) {
            if (returnType.equals(boolean.class)) {
                return false;
            } else if (returnType.equals(char.class)) {
                return '\u0000';
            } else {
                return 0;
            }
        } else {
            return null;
        }
    }
}
