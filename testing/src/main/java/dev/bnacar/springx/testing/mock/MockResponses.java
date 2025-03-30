package dev.bnacar.springx.testing.mock;

import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Utility class for creating common mock responses.
 * Provides methods for creating mock responses for common scenarios.
 */
public class MockResponses {

    /**
     * Creates a mock ResponseEntity with the specified body and status.
     *
     * @param body   the response body
     * @param status the HTTP status
     * @param <T>    the type of the response body
     * @return the mock ResponseEntity
     */
    public static <T> ResponseEntity<T> mockResponseEntity(T body, HttpStatus status) {
        return new ResponseEntity<>(body, status);
    }

    /**
     * Creates a mock ResponseEntity with OK status.
     *
     * @param body the response body
     * @param <T>  the type of the response body
     * @return the mock ResponseEntity
     */
    public static <T> ResponseEntity<T> mockOkResponse(T body) {
        return mockResponseEntity(body, HttpStatus.OK);
    }

    /**
     * Creates a mock ResponseEntity with Created status.
     *
     * @param body the response body
     * @param <T>  the type of the response body
     * @return the mock ResponseEntity
     */
    public static <T> ResponseEntity<T> mockCreatedResponse(T body) {
        return mockResponseEntity(body, HttpStatus.CREATED);
    }

    /**
     * Creates a mock ResponseEntity with No Content status.
     *
     * @param <T> the type of the response body
     * @return the mock ResponseEntity
     */
    public static <T> ResponseEntity<T> mockNoContentResponse() {
        return mockResponseEntity(null, HttpStatus.NO_CONTENT);
    }

    /**
     * Creates a mock ResponseEntity with Bad Request status.
     *
     * @param <T> the type of the response body
     * @return the mock ResponseEntity
     */
    public static <T> ResponseEntity<T> mockBadRequestResponse() {
        return mockResponseEntity(null, HttpStatus.BAD_REQUEST);
    }

    /**
     * Creates a mock ResponseEntity with Not Found status.
     *
     * @param <T> the type of the response body
     * @return the mock ResponseEntity
     */
    public static <T> ResponseEntity<T> mockNotFoundResponse() {
        return mockResponseEntity(null, HttpStatus.NOT_FOUND);
    }

    /**
     * Creates a mock empty Optional.
     *
     * @param <T> the type of the Optional
     * @return the mock Optional
     */
    public static <T> Optional<T> mockEmptyOptional() {
        return Optional.empty();
    }

    /**
     * Creates a mock Optional with the specified value.
     *
     * @param value the value
     * @param <T>   the type of the Optional
     * @return the mock Optional
     */
    public static <T> Optional<T> mockOptional(T value) {
        return Optional.of(value);
    }

    /**
     * Creates a mock empty list.
     *
     * @param <T> the type of the list elements
     * @return the mock list
     */
    public static <T> List<T> mockEmptyList() {
        return Collections.emptyList();
    }

    /**
     * Creates a mock list with the specified elements.
     *
     * @param elements the elements
     * @param <T>      the type of the list elements
     * @return the mock list
     */
    @SafeVarargs
    public static <T> List<T> mockList(T... elements) {
        return List.of(elements);
    }

    /**
     * Creates a mock exception of the specified type.
     *
     * @param exceptionClass the exception class
     * @param message        the exception message
     * @param <T>            the type of the exception
     * @return the mock exception
     */
    public static <T extends Throwable> T mockException(Class<T> exceptionClass, String message) {
        return Mockito.mock(exceptionClass, invocation -> {
            if (invocation.getMethod().getName().equals("getMessage")) {
                return message;
            }
            return Mockito.RETURNS_DEFAULTS.answer(invocation);
        });
    }
}
