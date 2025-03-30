package dev.bnacar.springx.testing.assertion;

import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Utility class for common REST API assertions.
 * Provides methods for asserting common REST API response patterns.
 */
public class RestAssertions {

    /**
     * Asserts that the response is a successful response with status 200 OK.
     *
     * @return the result matcher
     */
    public static ResultMatcher isOk() {
        return MockMvcResultMatchers.status().isOk();
    }

    /**
     * Asserts that the response is a created response with status 201 Created.
     *
     * @return the result matcher
     */
    public static ResultMatcher isCreated() {
        return MockMvcResultMatchers.status().isCreated();
    }

    /**
     * Asserts that the response is a no content response with status 204 No Content.
     *
     * @return the result matcher
     */
    public static ResultMatcher isNoContent() {
        return MockMvcResultMatchers.status().isNoContent();
    }

    /**
     * Asserts that the response is a bad request response with status 400 Bad Request.
     *
     * @return the result matcher
     */
    public static ResultMatcher isBadRequest() {
        return MockMvcResultMatchers.status().isBadRequest();
    }

    /**
     * Asserts that the response is an unauthorized response with status 401 Unauthorized.
     *
     * @return the result matcher
     */
    public static ResultMatcher isUnauthorized() {
        return MockMvcResultMatchers.status().isUnauthorized();
    }

    /**
     * Asserts that the response is a forbidden response with status 403 Forbidden.
     *
     * @return the result matcher
     */
    public static ResultMatcher isForbidden() {
        return MockMvcResultMatchers.status().isForbidden();
    }

    /**
     * Asserts that the response is a not found response with status 404 Not Found.
     *
     * @return the result matcher
     */
    public static ResultMatcher isNotFound() {
        return MockMvcResultMatchers.status().isNotFound();
    }

    /**
     * Asserts that the response is a conflict response with status 409 Conflict.
     *
     * @return the result matcher
     */
    public static ResultMatcher isConflict() {
        return MockMvcResultMatchers.status().isConflict();
    }

    /**
     * Asserts that the response is an internal server error response with status 500 Internal Server Error.
     *
     * @return the result matcher
     */
    public static ResultMatcher isInternalServerError() {
        return MockMvcResultMatchers.status().isInternalServerError();
    }

    /**
     * Asserts that the response contains a JSON field with the specified value.
     *
     * @param field the JSON field path
     * @param value the expected value
     * @return the result matcher
     */
    public static ResultMatcher hasJsonField(String field, Object value) {
        return jsonPath(field, is(value));
    }

    /**
     * Asserts that the response contains a JSON field.
     *
     * @param field the JSON field path
     * @return the result matcher
     */
    public static ResultMatcher hasJsonField(String field) {
        return jsonPath(field).exists();
    }

    /**
     * Asserts that the response does not contain a JSON field.
     *
     * @param field the JSON field path
     * @return the result matcher
     */
    public static ResultMatcher doesNotHaveJsonField(String field) {
        return jsonPath(field).doesNotExist();
    }

    /**
     * Asserts that the response contains a JSON array with the specified size.
     *
     * @param field the JSON field path
     * @param size the expected array size
     * @return the result matcher
     */
    public static ResultMatcher hasJsonArrayOfSize(String field, int size) {
        return jsonPath(field, hasSize(size));
    }

    /**
     * Asserts that the response contains a JSON array that is not empty.
     *
     * @param field the JSON field path
     * @return the result matcher
     */
    public static ResultMatcher hasNonEmptyJsonArray(String field) {
        return jsonPath(field, not(empty()));
    }

    /**
     * Asserts that the response contains a JSON array that is empty.
     *
     * @param field the JSON field path
     * @return the result matcher
     */
    public static ResultMatcher hasEmptyJsonArray(String field) {
        return jsonPath(field, empty());
    }

    /**
     * Applies all the specified matchers to the result actions.
     *
     * @param resultActions the result actions
     * @param matchers the matchers to apply
     * @return the result actions for chaining
     * @throws Exception if an error occurs
     */
    public static ResultActions assertAll(ResultActions resultActions, ResultMatcher... matchers) throws Exception {
        for (ResultMatcher matcher : matchers) {
            resultActions.andExpect(matcher);
        }
        return resultActions;
    }
}