package com.example.notificationservice.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link GlobalExceptionHandler}.
 * <p>
 * This test class verifies that the {@link GlobalExceptionHandler} correctly handles HTTP client errors (4xx)
 * and server errors (5xx) by mapping exceptions to structured error responses.
 * </p>
 *
 * @version 1.0
 * @since 2025-03-26
 */
public class GlobalExceptionHandlerTest {

    // Instantiate the GlobalExceptionHandler to test its methods.
    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    /**
     * Tests the handling of a HttpClientErrorException (4xx errors).
     * <p>
     * <strong>Description:</strong> This test simulates a 404 Not Found exception and verifies that the
     * GlobalExceptionHandler returns a ResponseEntity with status 404 and correct error details.
     * </p>
     */
    @Test
    @DisplayName("Test handling of HttpClientErrorException (404 Not Found)")
    public void testHandleHttpClientErrorException() {
        // Arrange: Create a simulated HttpClientErrorException with 404 status.
        HttpClientErrorException clientErrorException = HttpClientErrorException.create(
                HttpStatus.NOT_FOUND, "Not Found", null, null, null);

        // Create a mock WebRequest that returns a dummy URI description.
        WebRequest mockRequest = mock(WebRequest.class);
        when(mockRequest.getDescription(false)).thenReturn("uri=/api/test");

        // Act: Call the handler method for HttpClientErrorException.
        ResponseEntity<Map<String, Object>> responseEntity =
                exceptionHandler.handleHttpClientErrorException(clientErrorException, mockRequest);

        // Assert: Verify that the response status code is 404 and the error details match the expected values.
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode(), "Response status should be 404 Not Found");

        Map<String, Object> errorDetails = responseEntity.getBody();
        assertNotNull(errorDetails, "Error details should not be null");
        assertEquals("Not Found", errorDetails.get("message"), "Error message should match");
        assertEquals(404, errorDetails.get("status"), "Status code in error details should be 404");
        assertEquals("Not Found", errorDetails.get("error"), "Error description should match");
        assertEquals("uri=/api/test", errorDetails.get("path"), "Path should match the mock request description");
        assertNotNull(errorDetails.get("timestamp"), "Timestamp should not be null");
    }

    /**
     * Tests the handling of a HttpServerErrorException (5xx errors).
     * <p>
     * <strong>Description:</strong> This test simulates a 500 Internal Server Error exception and verifies that the
     * GlobalExceptionHandler returns a ResponseEntity with status 500 and correct error details.
     * </p>
     */
    @Test
    @DisplayName("Test handling of HttpServerErrorException (500 Internal Server Error)")
    public void testHandleHttpServerErrorException() {
        // Arrange: Create a simulated HttpServerErrorException with 500 status.
        HttpServerErrorException serverErrorException = HttpServerErrorException.create(
                HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", null, null, null);

        // Create a mock WebRequest that returns a dummy URI description.
        WebRequest mockRequest = mock(WebRequest.class);
        when(mockRequest.getDescription(false)).thenReturn("uri=/api/test");

        // Act: Call the handler method for HttpServerErrorException.
        ResponseEntity<Map<String, Object>> responseEntity =
                exceptionHandler.handleHttpServerErrorException(serverErrorException, mockRequest);

        // Assert: Verify that the response status code is 500 and the error details match the expected values.
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode(), "Response status should be 500 Internal Server Error");

        Map<String, Object> errorDetails = responseEntity.getBody();
        assertNotNull(errorDetails, "Error details should not be null");
        assertEquals("Internal Server Error", errorDetails.get("message"), "Error message should match");
        assertEquals(500, errorDetails.get("status"), "Status code in error details should be 500");
        assertEquals("Internal Server Error", errorDetails.get("error"), "Error description should match");
        assertEquals("uri=/api/test", errorDetails.get("path"), "Path should match the mock request description");
        assertNotNull(errorDetails.get("timestamp"), "Timestamp should not be null");
    }
}
