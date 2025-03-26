package com.example.maxflow.exception;

import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler to catch and process exceptions thrown by controller methods.
 * <p>
 * This class is annotated with {@code @RestControllerAdvice} to intercept exceptions across the whole application.
 * It provides specific exception handlers for:
 * <ul>
 *   <li>{@link MaxFlowException} - Custom exception for max flow computation errors.</li>
 *   <li>{@link MethodArgumentNotValidException} - Validation errors when request arguments fail validation.</li>
 *   <li>{@link ConstraintViolationException} - Violations of bean validation constraints.</li>
 *   <li>{@code Exception} - Any other unexpected exceptions.</li>
 * </ul>
 * Each handler logs the error using different log levels and returns an appropriate {@code ResponseEntity} with a corresponding HTTP status code.
 * </p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Logger instance for logging error messages.
     */
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles custom {@link MaxFlowException} thrown during max flow computation.
     *
     * @param ex the {@link MaxFlowException} instance
     * @return a {@link ResponseEntity} containing an error message map and a {@code BAD_REQUEST} status
     */
    @ExceptionHandler(MaxFlowException.class)
    public ResponseEntity<Map<String, String>> handleMaxFlowException(MaxFlowException ex) {
        logger.error("MaxFlowException: {}", ex.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles {@link MethodArgumentNotValidException} thrown when request validation fails.
     *
     * @param ex the {@link MethodArgumentNotValidException} instance
     * @return a {@link ResponseEntity} containing a map of field-specific error messages and a {@code BAD_REQUEST} status
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        logger.error("Validation failure: {}", ex.getMessage());
        Map<String, String> errorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err ->
                                                               errorMap.put(err.getField(), err.getDefaultMessage())
        );
        return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles {@link ConstraintViolationException} thrown when bean validation constraints are violated.
     *
     * @param ex the {@link ConstraintViolationException} instance
     * @return a {@link ResponseEntity} containing a map of constraint violation messages and a {@code BAD_REQUEST} status
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolation(ConstraintViolationException ex) {
        logger.error("Constraint violation: {}", ex.getMessage());
        Map<String, String> errorMap = new HashMap<>();
        ex.getConstraintViolations().forEach(cv ->
                                                     errorMap.put("field", cv.getMessage())
        );
        return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles all other unhandled exceptions that are not explicitly caught by other exception handlers.
     *
     * @param ex the generic {@link Exception} instance
     * @return a {@link ResponseEntity} containing a generic error message and an {@code INTERNAL_SERVER_ERROR} status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleOtherExceptions(Exception ex) {
        logger.error("Unhandled exception: {}", ex.getMessage(), ex);
        Map<String, String> error = new HashMap<>();
        error.put("error", "An unexpected error occurred.");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
