package dev.bnacar.springx.core.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to automatically retry a method on failure.
 * When applied to a method, the method will be retried based on the specified parameters.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Retry {

    /**
     * Maximum number of retry attempts.
     * @return the maximum number of attempts
     */
    int maxAttempts() default 3;

    /**
     * Delay between retry attempts in milliseconds.
     * @return the delay in milliseconds
     */
    long backoffMs() default 1000;

    /**
     * Whether to use exponential backoff for retry delays.
     * @return true if exponential backoff should be used
     */
    boolean exponential() default false;

    /**
     * Exception types that should trigger a retry.
     * If empty, all exceptions will trigger a retry.
     * @return the exception types to retry on
     */
    Class<? extends Throwable>[] retryOn() default {};

    /**
     * Exception types that should not trigger a retry.
     * These exceptions take precedence over retryOn.
     * @return the exception types to not retry on
     */
    Class<? extends Throwable>[] noRetryOn() default {};
}
