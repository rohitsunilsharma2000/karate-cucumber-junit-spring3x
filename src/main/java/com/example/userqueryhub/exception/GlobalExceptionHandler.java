
package com.example.userqueryhub.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for the UserQueryHub application.
 *
 * <p>
 * This class intercepts exceptions thrown across the whole application and provides meaningful HTTP responses.
 * It specifically handles {@link UserNotFoundException} and any generic exceptions, ensuring that errors are logged
 * and appropriate HTTP status codes and messages are returned to the client.
 * </p>
 *
 * <h3>Key Features:</h3>
 * <ul>
 *   <li><strong>Centralized Error Handling:</strong> Captures exceptions from all controllers in the application.</li>
 *   <li><strong>Custom Exception Handling:</strong> Provides specific handling for {@link UserNotFoundException}.</li>
 *   <li><strong>Logging:</strong> Uses SLF4J to log error details at the error level for debugging and production monitoring.</li>
 *   <li><strong>HTTP Response:</strong> Returns a ResponseEntity with an appropriate HTTP status code and error message.</li>
 * </ul>
 *
 * @version 1.0
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handles {@link UserNotFoundException} exceptions.
     *
     * <p>
     * Logs the error message at the error level and returns a ResponseEntity with HTTP status 404 (Not Found)
     * and the exception's message in the response body.
     * </p>
     *
     * @param ex the {@link UserNotFoundException} thrown when a user is not found.
     * @return a ResponseEntity containing the error message and a 404 status.
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFound(UserNotFoundException ex) {
        log.error("Resource not found: {}", ex.getMessage());
        return ResponseEntity.status(404).body(ex.getMessage());
    }

    /**
     * Handles all generic exceptions not explicitly handled by other exception handlers.
     *
     * <p>
     * Logs the exception stack trace at the error level and returns a ResponseEntity with HTTP status 500 (Internal Server Error)
     * and a generic error message in the response body.
     * </p>
     *
     * @param ex the exception that was thrown.
     * @return a ResponseEntity containing a generic error message and a 500 status.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception ex) {
        log.error("Unhandled error: ", ex);
        return ResponseEntity.status(500).body("Internal server error");
    }
}
