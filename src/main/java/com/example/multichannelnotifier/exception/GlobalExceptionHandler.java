package com.example.multichannelnotifier.exception;

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
 * Global exception handler for the Notification Service Application.
 *
 * <p><strong>Functionality:</strong>
 * This class provides centralized exception handling across the notification service.
 * It intercepts client-side (4xx) and server-side (5xx) HTTP errors thrown by controllers,
 * converts them into structured error responses, and returns them with appropriate HTTP status codes.
 * Additionally, it now handles NotificationException, ensuring that all errors follow a standard response format.
 * </p>
 *
 * @version 1.0
 * @since 2025-03-26
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles client-side HTTP errors (4xx).
     */
    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Map<String, Object>> handleHttpClientErrorException(
            HttpClientErrorException ex, WebRequest request) {
        // Convert the exception's HTTP status code into a HttpStatus object.
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        log.error("Client error occurred: {} - {}", status.getReasonPhrase(), ex.getMessage(), ex);
        return buildErrorResponse(status.getReasonPhrase(), status, request);
    }

    /**
     * Handles server-side HTTP errors (5xx).
     */
    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<Map<String, Object>> handleHttpServerErrorException(
            HttpServerErrorException ex, WebRequest request) {
        // Convert the exception's HTTP status code into a HttpStatus object.
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        log.error("Server error occurred: {} - {}", status.getReasonPhrase(), ex.getMessage(), ex);
        return buildErrorResponse(status.getReasonPhrase(), status, request);
    }

    /**
     * Handles NotificationException errors.
     * <p>
     * This method intercepts NotificationException instances thrown within the notification service,
     * logs the error, and returns a structured error response with HTTP 500 (Internal Server Error).
     * </p>
     *
     * @param ex      The NotificationException instance.
     * @param request The current WebRequest providing request context.
     * @return A ResponseEntity containing error details and HTTP status 500.
     */
    @ExceptionHandler(NotificationException.class)
    public ResponseEntity<Map<String, Object>> handleNotificationException(
            NotificationException ex, WebRequest request) {
        // Use HTTP 500 as the status for NotificationException.
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        log.error("Notification error occurred: {} - {}", ex.getMessage(), ex);
        return buildErrorResponse(ex.getMessage(), status, request);
    }

    /**
     * Helper method to build a structured error response.
     */
    private ResponseEntity<Map<String, Object>> buildErrorResponse(String message, HttpStatus status, WebRequest request) {
        // Create a map to store error details.
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", new Date());
        errorDetails.put("message", message);
        errorDetails.put("status", status.value());
        errorDetails.put("error", status.getReasonPhrase());
        errorDetails.put("path", request.getDescription(false)); // e.g., "uri=/api/..."
        return new ResponseEntity<>(errorDetails, status);
    }
}
