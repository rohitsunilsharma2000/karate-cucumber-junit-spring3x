package com.example.graph.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the Graph Application.
 *
 * <p>
 * This class centralizes exception handling for all controllers, ensuring that errors are
 * consistently formatted, logged, and returned with appropriate HTTP status codes.
 * </p>
 *
 * <p><strong>Key Responsibilities:</strong></p>
 * <ul>
 *   <li>Handle validation errors arising from invalid request payloads.</li>
 *   <li>Catch and log unexpected runtime exceptions.</li>
 *   <li>Ensure secure and meaningful error responses are sent to clients.</li>
 * </ul>
 *
 * <p><strong>Registered Exception Handlers:</strong></p>
 * <ul>
 *   <li>{@link MethodArgumentNotValidException}: Thrown when a `@Valid` annotated DTO fails validation.</li>
 *   <li>{@link Exception}: Catch-all handler for any uncaught runtime exception.</li>
 * </ul>
 *
 * <p><strong>Pass/Fail Conditions:</strong></p>
 * <ul>
 *   <li><strong>Pass:</strong> The application returns structured and meaningful error responses for invalid or exceptional conditions.</li>
 *   <li><strong>Fail:</strong> Errors are returned as raw stack traces or unformatted messages, leaking implementation details or failing to inform clients.</li>
 * </ul>
 *
 *
 * @author
 * @since 2025-03-26
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles validation errors from failed `@Valid` DTOs.
     *
     * <p>
     * Aggregates all validation errors and returns them in a structured format with HTTP 400.
     * </p>
     *
     * @param ex the MethodArgumentNotValidException thrown by Spring
     * @return ResponseEntity containing field-specific error messages
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        logger.error("Validation error occurred: {}", errors);
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Catches any unexpected or unhandled exception.
     *
     * <p>
     * Logs the full stack trace for debugging and returns a generic error message to the client.
     * </p>
     *
     * @param ex the unexpected exception
     * @return ResponseEntity with HTTP 500 and a general error message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        logger.error("Unexpected error occurred: ", ex);
        return new ResponseEntity<>("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
