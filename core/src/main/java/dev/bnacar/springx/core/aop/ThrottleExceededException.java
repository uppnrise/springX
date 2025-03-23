package dev.bnacar.springx.core.aop;

/**
 * Exception thrown when a throttle limit is exceeded.
 */
public class ThrottleExceededException extends RuntimeException {

    /**
     * Constructs a new ThrottleExceededException with the specified detail message.
     *
     * @param message the detail message
     */
    public ThrottleExceededException(String message) {
        super(message);
    }

    /**
     * Constructs a new ThrottleExceededException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause
     */
    public ThrottleExceededException(String message, Throwable cause) {
        super(message, cause);
    }
}
