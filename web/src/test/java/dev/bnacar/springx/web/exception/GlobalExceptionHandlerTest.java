package dev.bnacar.springx.web.exception;

import dev.bnacar.springx.web.response.ApiResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler exceptionHandler;

    @Mock
    private WebRequest webRequest;

    @Test
    public void testHandleException() {
        // Arrange
        Exception exception = new RuntimeException("Test exception");

        // Act
        ResponseEntity<ApiResponse<Void>> responseEntity = exceptionHandler.handleException(exception, webRequest);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertFalse(responseEntity.getBody().isSuccess());
        assertTrue(responseEntity.getBody().getMessages().get(0).contains("Test exception"));
    }

    @Test
    public void testHandleValidationException() {
        // Arrange
        BindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "object");
        bindingResult.addError(new FieldError("object", "field1", "Field 1 error"));
        bindingResult.addError(new FieldError("object", "field2", "Field 2 error"));

        MethodArgumentNotValidException validationException =
                new MethodArgumentNotValidException(null, bindingResult);

        // Act
        ResponseEntity<ApiResponse<Void>> responseEntity = exceptionHandler.handleValidationException(validationException, webRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertFalse(responseEntity.getBody().isSuccess());
        assertEquals("VALIDATION_ERROR", responseEntity.getBody().getErrorCode());
        String errorMessage = responseEntity.getBody().getMessages().get(0);
        assertTrue(errorMessage.contains("field1: Field 1 error"));
        assertTrue(errorMessage.contains("field2: Field 2 error"));
    }

    @Test
    public void testHandleResourceNotFoundException() {
        // Arrange
        ResourceNotFoundException exception = new ResourceNotFoundException("Resource not found");

        // Act
        ResponseEntity<ApiResponse<Void>> responseEntity = exceptionHandler.handleResourceNotFoundException(exception, webRequest);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertFalse(responseEntity.getBody().isSuccess());
        assertEquals("RESOURCE_NOT_FOUND", responseEntity.getBody().getErrorCode());
        assertEquals("Resource not found", responseEntity.getBody().getMessages().get(0));
    }
}
