package com.example.notificationservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the application.
 * <p>
 * This class handles exceptions thrown by controllers and returns
 * a consistent error response. It handles both client errors (4xx) and server errors (5xx).
 * </p>
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles client-side HTTP errors (4xx).
     * <p>
     * This method intercepts {@link HttpClientErrorException} exceptions, extracts the status code and reason,
     * and returns a structured error response.
     * </p>
     *
     * @param ex      The HttpClientErrorException thrown by the controller.
     * @param request The current web request.
     * @return A {@link ResponseEntity} containing the error details.
     */
    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Map<String, Object>> handleHttpClientErrorException(HttpClientErrorException ex, WebRequest request) {
        // Convert the HTTP status code from the exception into a HttpStatus object.
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        // Build and return an error response using the helper method.
        return buildErrorResponse(status.getReasonPhrase(), status, request);
    }

    /**
     * Handles server-side HTTP errors (5xx).
     * <p>
     * This method intercepts {@link HttpServerErrorException} exceptions, extracts the status code and reason,
     * and returns a structured error response.
     * </p>
     *
     * @param ex      The HttpServerErrorException thrown by the controller.
     * @param request The current web request.
     * @return A {@link ResponseEntity} containing the error details.
     */
    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<Map<String, Object>> handleHttpServerErrorException(HttpServerErrorException ex, WebRequest request) {
        // Convert the HTTP status code from the exception into a HttpStatus object.
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        // Build and return an error response using the helper method.
        return buildErrorResponse(status.getReasonPhrase(), status, request);
    }

    /**
     * Helper method to build a structured error response.
     * <p>
     * This method creates a map of error details including the timestamp, error message, status code,
     * error reason, and the request path. It then wraps this map in a {@link ResponseEntity} with the appropriate HTTP status.
     * </p>
     *
     * @param message The error message or reason phrase.
     * @param status  The HTTP status code.
     * @param request The current web request.
     * @return A {@link ResponseEntity} containing the error details.
     */
    private ResponseEntity<Map<String, Object>> buildErrorResponse(String message, HttpStatus status, WebRequest request) {
        // Create a map to store error details.
        Map<String, Object> errorDetails = new HashMap<>();
        // Store the current date and time as the timestamp.
        errorDetails.put("timestamp", new Date());
        // Store the error message.
        errorDetails.put("message", message);
        // Store the numeric value of the HTTP status code.
        errorDetails.put("status", status.value());
        // Store the textual representation of the HTTP status.
        errorDetails.put("error", status.getReasonPhrase());
        // Extract and store the request path from the WebRequest.
        errorDetails.put("path", request.getDescription(false)); // e.g., "uri=/api/..."
        // Return a ResponseEntity containing the error details and the HTTP status.
        return new ResponseEntity<>(errorDetails, status);
    }
}
