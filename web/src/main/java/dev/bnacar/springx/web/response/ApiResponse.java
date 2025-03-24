package dev.bnacar.springx.web.response;

import java.util.ArrayList;
import java.util.List;

/**
 * A standardized response wrapper for REST API responses.
 * Provides a consistent structure for all API responses.
 *
 * @param <T> the type of data in the response
 */
public class ApiResponse<T> {

    private boolean success;
    private T data;
    private List<String> messages;
    private String errorCode;

    /**
     * Creates a new successful API response with data.
     *
     * @param data the response data
     * @param <T> the type of data
     * @return a new ApiResponse instance
     */
    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setData(data);
        return response;
    }

    /**
     * Creates a new successful API response with data and a message.
     *
     * @param data the response data
     * @param message the success message
     * @param <T> the type of data
     * @return a new ApiResponse instance
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        ApiResponse<T> response = success(data);
        response.addMessage(message);
        return response;
    }

    /**
     * Creates a new error API response with a message.
     *
     * @param message the error message
     * @param <T> the type of data
     * @return a new ApiResponse instance
     */
    public static <T> ApiResponse<T> error(String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(false);
        response.addMessage(message);
        return response;
    }

    /**
     * Creates a new error API response with a message and error code.
     *
     * @param message the error message
     * @param errorCode the error code
     * @param <T> the type of data
     * @return a new ApiResponse instance
     */
    public static <T> ApiResponse<T> error(String message, String errorCode) {
        ApiResponse<T> response = error(message);
        response.setErrorCode(errorCode);
        return response;
    }

    /**
     * Default constructor.
     */
    public ApiResponse() {
        this.messages = new ArrayList<>();
    }

    /**
     * Adds a message to the response.
     *
     * @param message the message to add
     */
    public void addMessage(String message) {
        if (this.messages == null) {
            this.messages = new ArrayList<>();
        }
        this.messages.add(message);
    }

    /**
     * Gets whether the response was successful.
     *
     * @return true if the response was successful, false otherwise
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Sets whether the response was successful.
     *
     * @param success true if the response was successful, false otherwise
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * Gets the response data.
     *
     * @return the response data
     */
    public T getData() {
        return data;
    }

    /**
     * Sets the response data.
     *
     * @param data the response data
     */
    public void setData(T data) {
        this.data = data;
    }

    /**
     * Gets the response messages.
     *
     * @return the response messages
     */
    public List<String> getMessages() {
        return messages;
    }

    /**
     * Sets the response messages.
     *
     * @param messages the response messages
     */
    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    /**
     * Gets the error code.
     *
     * @return the error code
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Sets the error code.
     *
     * @param errorCode the error code
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
