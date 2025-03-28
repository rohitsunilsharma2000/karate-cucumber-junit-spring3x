package com.example.graph.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Global exception handler for managing application-wide exceptions in a consistent and structured manner.
 *
 * <p><strong>Purpose:</strong></p>
 * <ul>
 *   <li>Catches and handles known and unexpected exceptions thrown across all controller layers.</li>
 *   <li>Returns standardized error responses with appropriate HTTP status codes and informative messages.</li>
 * </ul>
 *
 * <p><strong>Features:</strong></p>
 * <ul>
 *   <li>Handles validation errors, malformed JSON input, and missing request parameters.</li>
 *   <li>Captures application-specific and generic exceptions.</li>
 *   <li>Includes timestamp, HTTP status, error message, and optional field-level errors in responses.</li>
 * </ul>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Logger instance to log all exception details
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Builds a structured error response body with optional extra fields.
     *
     * @param status  The HTTP status to be returned (e.g., 400, 500).
     * @param message A human-readable message explaining the error.
     * @param extra   Additional fields to be included in the response (e.g., field validation errors).
     * @return A {@link ResponseEntity} containing the error response body.
     */
    private ResponseEntity<Object> buildErrorResponse(HttpStatus status, String message, Map<String, ?> extra) {
        Map<String, Object> body = new LinkedHashMap<>(); // Maintain field order
        body.put("timestamp", LocalDateTime.now());       // Timestamp for error tracking
        body.put("status", status.value());               // HTTP status code
        body.put("error", status.getReasonPhrase());      // HTTP status description
        body.put("message", message);                     // Main error message

        // Merge any additional error details (e.g., field-level validation errors)
        if (extra != null && !extra.isEmpty()) {
            body.putAll(extra);
        }

        return new ResponseEntity<>(body, status); // Return complete response
    }

    /**
     * Handles validation errors triggered by `@Valid` annotations on method arguments.
     *
     * <p>Occurs when DTO validation constraints are violated (e.g., blank fields, invalid size).</p>
     *
     * @param ex The exception containing binding result with field errors.
     * @return A response with HTTP 400 and a map of field errors.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationErrors(MethodArgumentNotValidException ex) {
        logger.error("Validation error: {}", ex.getMessage());

        // Extract field errors and build a key-value map
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        // Return error response with detailed field-level messages
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed", Map.of("errors", errors));
    }

    /**
     * Handles cases where the request body contains malformed JSON or cannot be parsed.
     *
     * @param ex The exception with parsing details.
     * @return A response with HTTP 400 and a message indicating malformed JSON.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleJsonParseErrors(HttpMessageNotReadableException ex) {
        logger.error("Malformed JSON: {}", ex.getMessage());

        // Inform client that the JSON input is invalid
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Malformed JSON request", null);
    }

    /**
     * Handles custom exceptions thrown during graph-related computations or algorithms.
     *
     * <p>Used when a graph analysis algorithm fails (e.g., invalid topology, null edges).</p>
     *
     * @param ex The {@link GraphAnalysisException} instance containing the error detail.
     * @return A response with HTTP 500 and the specific error message.
     */
    @ExceptionHandler(GraphAnalysisException.class)
    public ResponseEntity<Object> handleGraphAnalysisException(GraphAnalysisException ex) {
        logger.error("Graph analysis exception: {}", ex.getMessage());

        // Return internal server error with graph-specific message
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), null);
    }

    /**
     * Handles missing required query or request parameters.
     *
     * <p>Occurs when a required @RequestParam is not supplied in the request.</p>
     *
     * @param ex The exception indicating the missing parameter.
     * @return A response with HTTP 400 and a message describing the missing parameter.
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Object> handleMissingParams(MissingServletRequestParameterException ex) {
        logger.error("Missing request parameter: {}", ex.getMessage());

        // Notify client that a required parameter is missing
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
    }

    /**
     * Handles all uncaught exceptions that do not match any specific handler above.
     *
     * <p>Acts as a fallback for unexpected or runtime exceptions.</p>
     *
     * @param ex The exception that was thrown.
     * @return A response with HTTP 500 and a generic error message.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        logger.error("Unhandled exception: ", ex); // Log full stack trace for debugging

        // Return generic internal error to avoid exposing internal logic to client
        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred. Please try again later.",
                null
        );
    }
}
