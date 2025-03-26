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
 * Global exception handler for the Notification Service Application.
 *
 * <p><strong>Functionality:</strong>
 * This class provides centralized exception handling across the notification service.
 * It intercepts client-side (4xx) and server-side (5xx) HTTP errors thrown by controllers,
 * converts them into structured error responses, and returns them with appropriate HTTP status codes.
 * </p>
 *
 * <p><strong>Acceptable Values/Range:</strong>
 * <ul>
 *   <li>Handles exceptions such as {@link HttpClientErrorException} and {@link HttpServerErrorException}.</li>
 *   <li>Accepts a valid {@link WebRequest} that contains contextual information about the incoming request.</li>
 * </ul>
 * </p>
 *
 * <p><strong>Error Conditions:</strong>
 * <ul>
 *   <li>If an exception occurs (e.g., invalid client input or server processing error), the corresponding handler
 *       processes it and constructs an error response.</li>
 *   <li>If the exception or request is null, a proper response may not be generated.</li>
 * </ul>
 * </p>
 *
 * <p><strong>Pass/Fail Conditions:</strong>
 * <ul>
 *   <li><strong>Pass:</strong> Exceptions are successfully mapped to structured error responses with the correct HTTP status codes.</li>
 *   <li><strong>Fail:</strong> If the error details cannot be extracted, the handler may fail to build a complete error response.</li>
 * </ul>
 * </p>
 *
 * <p><strong>Feedback-Related Updates:</strong>
 * <ul>
 *   <li>Detailed documentation for each handler method is provided to clarify their roles and expected behaviors.</li>
 *   <li>Ensures comprehensive error information is returned for debugging and client communication.</li>
 * </ul>
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
     *
     * <p><strong>Description:</strong>
     * Intercepts {@link HttpClientErrorException} instances thrown within the notification service,
     * extracts the associated HTTP status code and error reason, and returns a structured error response.
     * </p>
     *
     * <p><strong>Parameters:</strong></p>
     * <ul>
     *   <li><strong>ex</strong> - The {@link HttpClientErrorException} thrown by the controller. Must not be null.</li>
     *   <li><strong>request</strong> - The current {@link WebRequest} providing request context. Must not be null.</li>
     * </ul>
     *
     * <p><strong>Acceptable Values/Range:</strong>
     * <ul>
     *   <li>{@code ex} should contain a valid HTTP status code corresponding to client errors (e.g., 400, 404).</li>
     *   <li>{@code request} must supply the request description.</li>
     * </ul>
     * </p>
     *
     * <p><strong>Error Conditions:</strong>
     * <ul>
     *   <li>If {@code ex} or {@code request} is null, an error may occur during error response construction.</li>
     * </ul>
     * </p>
     *
     * <p><strong>Pass/Fail Conditions:</strong>
     * <ul>
     *   <li><strong>Pass:</strong> Returns a {@link ResponseEntity} with a properly constructed error response and a 4xx status.</li>
     *   <li><strong>Fail:</strong> Fails to construct an error response if required parameters are missing.</li>
     * </ul>
     * </p>
     *
     * @param ex      The {@link HttpClientErrorException} instance. Must not be null.
     * @param request The current {@link WebRequest} instance. Must not be null.
     * @return A {@link ResponseEntity} containing error details with the appropriate 4xx HTTP status.
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
     *
     * <p><strong>Description:</strong>
     * Intercepts {@link HttpServerErrorException} instances thrown within the notification service,
     * extracts the associated HTTP status code and error reason, and returns a structured error response.
     * </p>
     *
     * <p><strong>Parameters:</strong></p>
     * <ul>
     *   <li><strong>ex</strong> - The {@link HttpServerErrorException} thrown by the controller. Must not be null.</li>
     *   <li><strong>request</strong> - The current {@link WebRequest} providing request context. Must not be null.</li>
     * </ul>
     *
     * <p><strong>Acceptable Values/Range:</strong>
     * <ul>
     *   <li>{@code ex} should contain a valid HTTP status code corresponding to server errors (e.g., 500).</li>
     *   <li>{@code request} must supply the request description.</li>
     * </ul>
     * </p>
     *
     * <p><strong>Error Conditions:</strong>
     * <ul>
     *   <li>If {@code ex} or {@code request} is null, constructing the error response may fail.</li>
     * </ul>
     * </p>
     *
     * <p><strong>Pass/Fail Conditions:</strong>
     * <ul>
     *   <li><strong>Pass:</strong> Returns a {@link ResponseEntity} with a properly constructed error response and a 5xx status.</li>
     *   <li><strong>Fail:</strong> Fails to construct an error response if required parameters are missing.</li>
     * </ul>
     * </p>
     *
     * @param ex      The {@link HttpServerErrorException} instance. Must not be null.
     * @param request The current {@link WebRequest} instance. Must not be null.
     * @return A {@link ResponseEntity} containing error details with the appropriate 5xx HTTP status.
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
     * Helper method to build a structured error response.
     *
     * <p><strong>Description:</strong>
     * Creates a map containing error details such as the timestamp, error message, HTTP status code,
     * error reason, and request path. Wraps this map in a {@link ResponseEntity} with the provided HTTP status.
     * </p>
     *
     * <p><strong>Parameters:</strong></p>
     * <ul>
     *   <li><strong>message</strong> - The error message or reason phrase. Must not be null.</li>
     *   <li><strong>status</strong> - The HTTP status associated with the error. Must not be null.</li>
     *   <li><strong>request</strong> - The current {@link WebRequest} providing request context. Must not be null.</li>
     * </ul>
     *
     * <p><strong>Acceptable Values/Range:</strong>
     * <ul>
     *   <li>{@code message} should be a descriptive error message.</li>
     *   <li>{@code status} should be a valid {@link HttpStatus} object.</li>
     *   <li>{@code request} must supply a valid request description (e.g., URI).</li>
     * </ul>
     * </p>
     *
     * <p><strong>Error Conditions:</strong>
     * <ul>
     *   <li>If any of the parameters are null, the error response may be incomplete or invalid.</li>
     * </ul>
     * </p>
     *
     * <p><strong>Pass/Fail Conditions:</strong>
     * <ul>
     *   <li><strong>Pass:</strong> A complete and structured error response is returned with the correct HTTP status.</li>
     *   <li><strong>Fail:</strong> If required error details are missing, the response may not convey sufficient information.</li>
     * </ul>
     * </p>
     *
     * @param message The error message or reason phrase. Must not be null.
     * @param status  The HTTP status code corresponding to the error. Must not be null.
     * @param request The current {@link WebRequest} providing context. Must not be null.
     * @return A {@link ResponseEntity} containing a map of error details with the appropriate HTTP status.
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
}
