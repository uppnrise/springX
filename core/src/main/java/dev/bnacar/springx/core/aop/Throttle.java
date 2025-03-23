package dev.bnacar.springx.core.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * Annotation to apply rate limiting to a method.
 * When applied to a method, the method will be throttled based on the specified limit.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Throttle {

    /**
     * Maximum number of invocations allowed within the specified time unit.
     * @return the limit
     */
    int limit() default 10;

    /**
     * Time unit for the limit.
     * @return the time unit
     */
    TimeUnit timeUnit() default TimeUnit.MINUTES;

    /**
     * Behavior when the limit is exceeded.
     * @return the throttle behavior
     */
    ThrottleBehavior behavior() default ThrottleBehavior.THROW_EXCEPTION;

    /**
     * Available behaviors when the throttle limit is exceeded.
     */
    enum ThrottleBehavior {
        /**
         * Throw a ThrottleExceededException when the limit is exceeded.
         */
        THROW_EXCEPTION,

        /**
         * Return a default value when the limit is exceeded.
         */
        RETURN_DEFAULT,

        /**
         * Block until the method can be executed within the limit.
         */
        BLOCK
    }
}

