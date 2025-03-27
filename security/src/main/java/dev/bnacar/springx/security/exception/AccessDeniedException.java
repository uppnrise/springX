package dev.bnacar.springx.security.exception;

/**
 * Exception thrown when access to a secured resource is denied.
 */
public class AccessDeniedException extends RuntimeException {

    /**
     * Constructs a new AccessDeniedException with the specified detail message.
     *
     * @param message the detail message
     */
    public AccessDeniedException(String message) {
        super(message);
    }

    /**
     * Constructs a new AccessDeniedException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause
     */
    public AccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }
}
