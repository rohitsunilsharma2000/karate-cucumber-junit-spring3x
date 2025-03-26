package com.example.johnson.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler that intercepts exceptions thrown by controllers
 * and returns a standardized error response.
 *
 * <p><strong>Overview:</strong></p>
 * This class uses Spring's {@code @ControllerAdvice} annotation to globally handle exceptions.
 * In particular, it intercepts {@link MaxFlowException} and builds a structured JSON error response.
 *
 * <p><strong>Usage:</strong></p>
 * <ul>
 *     <li>Any controller throwing a {@link MaxFlowException} will have its exception intercepted by this handler.</li>
 *     <li>The error response includes a timestamp, error message, HTTP status code, error reason, and the request path.</li>
 * </ul>
 *
 * <p><strong>Error Response Format:</strong></p>
 * <pre>
 * {
 *   "timestamp": "Date object",
 *   "message": "Error message",
 *   "status": HTTP status code (e.g., 404),
 *   "error": "Reason phrase (e.g., Not Found)",
 *   "path": "Request URI (e.g., uri=/api/maxflow/calculate)"
 * }
 * </pre>
 *
 * @see MaxFlowException
 * @version 1.0
 * @since 2025-03-26
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Helper method to build the error response.
     *
     * @param message the error message
     * @param status  the HTTP status to be returned
     * @param request the current web request
     * @return a {@link ResponseEntity} containing the error details as a JSON map
     */
    private ResponseEntity<Map<String, Object>> buildErrorResponse(String message, HttpStatus status, WebRequest request) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", new Date());
        errorDetails.put("message", message);
        errorDetails.put("status", status.value());
        errorDetails.put("error", status.getReasonPhrase());
        errorDetails.put("path", request.getDescription(false)); // e.g., "uri=/api/..."
        return new ResponseEntity<>(errorDetails, status);
    }

    /**
     * Handles {@link MaxFlowException} thrown by controllers.
     *
     * <p><strong>Description:</strong></p>
     * This method intercepts any {@link MaxFlowException} and returns a standardized error response
     * with HTTP status 404 (Not Found). It logs the error message and builds the error response using
     * {@link #buildErrorResponse(String, HttpStatus, WebRequest)}.
     *
     * @param ex      the {@link MaxFlowException} that was thrown
     * @param request the current web request
     * @return a {@link ResponseEntity} with error details and HTTP status 404 (Not Found)
     */
    @ExceptionHandler(MaxFlowException.class)
    public ResponseEntity<Map<String, Object>> handleMaxFlowException(MaxFlowException ex, WebRequest request) {
        log.error("MaxFlowException occurred: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request);
    }
}
