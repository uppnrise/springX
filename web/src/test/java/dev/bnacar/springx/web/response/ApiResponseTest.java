package dev.bnacar.springx.web.response;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ApiResponseTest {

    @Test
    public void testSuccessWithData() {
        // Arrange & Act
        ApiResponse<String> response = ApiResponse.success("test data");

        // Assert
        assertTrue(response.isSuccess());
        assertEquals("test data", response.getData());
        assertNull(response.getErrorCode());
        assertTrue(response.getMessages() == null || response.getMessages().isEmpty());
    }

    @Test
    public void testSuccessWithDataAndMessage() {
        // Arrange & Act
        ApiResponse<String> response = ApiResponse.success("test data", "Operation successful");

        // Assert
        assertTrue(response.isSuccess());
        assertEquals("test data", response.getData());
        assertNull(response.getErrorCode());
        assertEquals(1, response.getMessages().size());
        assertEquals("Operation successful", response.getMessages().get(0));
    }

    @Test
    public void testErrorWithMessage() {
        // Arrange & Act
        ApiResponse<String> response = ApiResponse.error("An error occurred");

        // Assert
        assertFalse(response.isSuccess());
        assertNull(response.getData());
        assertNull(response.getErrorCode());
        assertEquals(1, response.getMessages().size());
        assertEquals("An error occurred", response.getMessages().get(0));
    }

    @Test
    public void testErrorWithMessageAndErrorCode() {
        // Arrange & Act
        ApiResponse<String> response = ApiResponse.error("Resource not found", "NOT_FOUND");

        // Assert
        assertFalse(response.isSuccess());
        assertNull(response.getData());
        assertEquals("NOT_FOUND", response.getErrorCode());
        assertEquals(1, response.getMessages().size());
        assertEquals("Resource not found", response.getMessages().get(0));
    }

    @Test
    public void testAddMessage() {
        // Arrange
        ApiResponse<String> response = new ApiResponse<>();

        // Act
        response.addMessage("First message");
        response.addMessage("Second message");

        // Assert
        assertEquals(2, response.getMessages().size());
        assertEquals("First message", response.getMessages().get(0));
        assertEquals("Second message", response.getMessages().get(1));
    }
}
