package dev.bnacar.springx.core.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to log the execution time of a method.
 * When applied to a method, the execution time will be logged
 * at the specified log level.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogExecutionTime {

    /**
     * Log level to use for logging the execution time.
     * @return the log level
     */
    LogLevel value() default LogLevel.DEBUG;

    /**
     * Whether to include method arguments in the log message.
     * @return true if method arguments should be included
     */
    boolean includeArgs() default false;

    /**
     * Available log levels for logging execution time.
     */
    enum LogLevel {
        TRACE,
        DEBUG,
        INFO,
        WARN,
        ERROR
    }
}
