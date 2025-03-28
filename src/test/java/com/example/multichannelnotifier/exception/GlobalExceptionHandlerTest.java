package com.example.multichannelnotifier.exception;

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
 * <strong>Overview:</strong>
 * This test class verifies that the GlobalExceptionHandler correctly maps HTTP client errors (4xx) and server errors (5xx)
 * to structured error responses. The tests simulate exceptions (e.g., 404 Not Found and 500 Internal Server Error)
 * and assert that the returned ResponseEntity contains the proper HTTP status and error details.
 * </p>
 *
 * <p>
 * <strong>Test Coverage:</strong> This class aims for 100% coverage of the exception handling logic in GlobalExceptionHandler.
 * </p>
 *
 * <p>
 * <strong>Acceptable Values / Range for Parameters:</strong>
 * <ul>
 *   <li>{@code ex}: A valid HttpClientErrorException or HttpServerErrorException with a specific HTTP status (e.g., 404 or 500).</li>
 *   <li>{@code request}: A non-null WebRequest that provides a valid request description (e.g., "uri=/api/test").</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>Error Conditions:</strong>
 * <ul>
 *   <li>If any of the parameters are null, the error response may be incomplete.</li>
 *   <li>The handler should still produce a ResponseEntity with the correct HTTP status and error details.</li>
 * </ul>
 * </p>
 *
 * @version 1.0
 * @since 2025-03-26
 */
class GlobalExceptionHandlerTest {

    // Create an instance of GlobalExceptionHandler for testing.
    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    /**
     * Tests the handling of a HttpClientErrorException (4xx errors).
     * <p>
     * <strong>Description:</strong> This test simulates a 404 Not Found exception and verifies that the
     * GlobalExceptionHandler returns a ResponseEntity with:
     * <ul>
     *   <li>HTTP status 404.</li>
     *   <li>Error details containing the correct message, status code, error description, request path, and timestamp.</li>
     * </ul>
     * </p>
     * <p>
     * <strong>Premise:</strong> A simulated HttpClientErrorException with HTTP 404 is provided along with a mock WebRequest.
     * </p>
     * <p>
     * <strong>Assertions:</strong>
     * <ul>
     *   <li>Status code is 404.</li>
     *   <li>Error message, status, error, path, and timestamp in the response are as expected.</li>
     * </ul>
     * </p>
     * <p>
     * <strong>Pass/Fail Conditions:</strong> The test passes if all assertions are met.
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

        // Act: Call the exception handler for HttpClientErrorException.
        ResponseEntity<Map<String, Object>> responseEntity =
                exceptionHandler.handleHttpClientErrorException(clientErrorException, mockRequest);

        // Assert: Validate that the response status is 404 and error details are correct.
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
     * GlobalExceptionHandler returns a ResponseEntity with:
     * <ul>
     *   <li>HTTP status 500.</li>
     *   <li>Error details containing the correct message, status code, error description, request path, and timestamp.</li>
     * </ul>
     * </p>
     * <p>
     * <strong>Premise:</strong> A simulated HttpServerErrorException with HTTP 500 is provided along with a mock WebRequest.
     * </p>
     * <p>
     * <strong>Assertions:</strong>
     * <ul>
     *   <li>Status code is 500.</li>
     *   <li>Error details in the response match the expected values.</li>
     * </ul>
     * </p>
     * <p>
     * <strong>Pass/Fail Conditions:</strong> Test passes if the response is constructed correctly.
     * </p>
     */
    @Test
    @DisplayName("Test handling of HttpServerErrorException (500 Internal Server Error)")
    public void testHandleHttpServerErrorException() {
        // Arrange: Create a simulated HttpServerErrorException with 500 status.
        HttpServerErrorException serverErrorException = HttpServerErrorException.create(
                HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", null, null, null);

        // Create a mock WebRequest with a dummy URI description.
        WebRequest mockRequest = mock(WebRequest.class);
        when(mockRequest.getDescription(false)).thenReturn("uri=/api/test");

        // Act: Call the exception handler for HttpServerErrorException.
        ResponseEntity<Map<String, Object>> responseEntity =
                exceptionHandler.handleHttpServerErrorException(serverErrorException, mockRequest);

        // Assert: Verify that the response status is 500 and error details are correct.
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
