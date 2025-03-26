package com.example.maxflow.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link GlobalExceptionHandler} without using a dummy controller.
 *
 * <p><strong>Overview:</strong></p>
 * This test class directly invokes the exception handler method to verify that when a {@link MaxFlowException}
 * is thrown, the {@link GlobalExceptionHandler} returns a standardized error response containing the proper
 * error message, HTTP status, timestamp, error reason, and request path.
 *
 * <p><strong>Usage:</strong></p>
 * <ul>
 *     <li>A {@link WebRequest} is mocked to provide a sample request description.</li>
 *     <li>The {@code handleMaxFlowException} method is invoked with a sample exception and the mocked request.</li>
 *     <li>The returned {@link ResponseEntity} is verified to contain all required fields with expected values.</li>
 * </ul>
 *
 * <p><strong>Pass/Fail Conditions:</strong></p>
 * <ul>
 *     <li><strong>Pass:</strong> The error response contains a non-null timestamp, the expected error message,
 *         status code 404, error reason "Not Found", and the correct request path.</li>
 *     <li><strong>Fail:</strong> Any missing or incorrect fields in the error response.</li>
 * </ul>
 *
 * @version 1.0
 * @since 2025-03-26
 */
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    /**
     * Tests that the global exception handler correctly builds the error response when a {@link MaxFlowException} is thrown.
     *
     * <p><strong>Scenario:</strong> The exception handler is invoked directly with a {@link MaxFlowException} and a mocked {@link WebRequest}.
     * <strong>Assertions:</strong>
     * <ul>
     *     <li>The HTTP status in the response is 404 (Not Found).</li>
     *     <li>The "message" field equals "Test exception".</li>
     *     <li>The "status" field equals 404.</li>
     *     <li>The "error" field equals "Not Found".</li>
     *     <li>The "timestamp" field is non-null and an instance of {@link Date}.</li>
     *     <li>The "path" field contains the mocked URI.</li>
     * </ul>
     * <strong>Pass Condition:</strong> All assertions pass.
     * <strong>Fail Condition:</strong> Any assertion fails.
     * </p>
     */
    @Test
    @DisplayName("handleMaxFlowException returns proper error response")
    void testHandleMaxFlowException() {
        // Create a sample MaxFlowException
        MaxFlowException ex = new MaxFlowException("Test exception");

        // Create a mock WebRequest that returns a sample request description
        WebRequest mockRequest = mock(WebRequest.class);
        when(mockRequest.getDescription(false)).thenReturn("uri=/test");

        // Invoke the exception handler
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleMaxFlowException(ex, mockRequest);

        // Assert that the status is 404 Not Found
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        // Get the response body and verify all required fields
        Map<String, Object> body = response.getBody();
        assertNotNull(body, "Response body should not be null");
        assertEquals("Test exception", body.get("message"), "Error message should match");
        assertEquals(404, body.get("status"), "Status code should be 404");
        assertEquals("Not Found", body.get("error"), "Error reason should be 'Not Found'");
        assertTrue(body.get("timestamp") instanceof Date, "Timestamp should be a Date instance");
        assertEquals("uri=/test", body.get("path"), "Path should match the mocked request description");
    }
}
