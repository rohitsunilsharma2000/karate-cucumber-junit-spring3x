package com.example.notificationservice.exception;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

/**
 * Unit tests for {@link GlobalExceptionHandler}.
 */
public class GlobalExceptionHandlerTest {

    // Instantiate the GlobalExceptionHandler to test its methods.
    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    /**
     * Tests the handling of a HttpClientErrorException (4xx errors).
     * <p>
     * This method creates a simulated 404 Not Found exception and verifies that the
     * GlobalExceptionHandler returns a response with the correct status code and error details.
     * </p>
     */
    @Test
    public void testHandleHttpClientErrorException() {
        // Create a simulated HttpClientErrorException with status 404 Not Found.
        HttpClientErrorException clientErrorException = HttpClientErrorException.create(
                HttpStatus.NOT_FOUND, "Not Found", null, null, null);

        // Create a mock WebRequest that returns a dummy URI description.
        WebRequest mockRequest = mock(WebRequest.class);
        when(mockRequest.getDescription(false)).thenReturn("uri=/api/test");

        // Call the handler method for HttpClientErrorException.
        ResponseEntity<Map<String, Object>> responseEntity =
                exceptionHandler.handleHttpClientErrorException(clientErrorException, mockRequest);

        // Assert that the response status code is 404 (Not Found).
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        // Retrieve the error details from the response body.
        Map<String, Object> errorDetails = responseEntity.getBody();
        assertNotNull(errorDetails, "Error details should not be null");

        // Verify that the error details contain the expected values.
        assertEquals("Not Found", errorDetails.get("message"));
        assertEquals(404, errorDetails.get("status"));
        assertEquals("Not Found", errorDetails.get("error"));
        assertEquals("uri=/api/test", errorDetails.get("path"));
        assertNotNull(errorDetails.get("timestamp"), "Timestamp should not be null");
    }

    /**
     * Tests the handling of a HttpServerErrorException (5xx errors).
     * <p>
     * This method creates a simulated 500 Internal Server Error exception and verifies that the
     * GlobalExceptionHandler returns a response with the correct status code and error details.
     * </p>
     */
    @Test
    public void testHandleHttpServerErrorException() {
        // Create a simulated HttpServerErrorException with status 500 Internal Server Error.
        HttpServerErrorException serverErrorException = HttpServerErrorException.create(
                HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", null, null, null);

        // Create a mock WebRequest that returns a dummy URI description.
        WebRequest mockRequest = mock(WebRequest.class);
        when(mockRequest.getDescription(false)).thenReturn("uri=/api/test");

        // Call the handler method for HttpServerErrorException.
        ResponseEntity<Map<String, Object>> responseEntity =
                exceptionHandler.handleHttpServerErrorException(serverErrorException, mockRequest);

        // Assert that the response status code is 500 (Internal Server Error).
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        // Retrieve the error details from the response body.
        Map<String, Object> errorDetails = responseEntity.getBody();
        assertNotNull(errorDetails, "Error details should not be null");

        // Verify that the error details contain the expected values.
        assertEquals("Internal Server Error", errorDetails.get("message"));
        assertEquals(500, errorDetails.get("status"));
        assertEquals("Internal Server Error", errorDetails.get("error"));
        assertEquals("uri=/api/test", errorDetails.get("path"));
        assertNotNull(errorDetails.get("timestamp"), "Timestamp should not be null");
    }
}
