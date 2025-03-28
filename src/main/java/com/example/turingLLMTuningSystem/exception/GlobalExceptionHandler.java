package com.example.turingLLMTuningSystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the Turing LLM Tuning System.
 *
 * <p><strong>Overview:</strong></p>
 * Handles application-wide exceptions in a centralized manner and ensures
 * consistent, meaningful error responses for API consumers.
 *
 * <p><strong>Handled Exceptions:</strong></p>
 * <ul>
 *   <li>{@link MethodArgumentNotValidException}: Thrown when validation on an argument annotated with {@code @Valid} fails.</li>
 *   <li>{@link Exception}: Catches all unhandled exceptions to avoid server crashes and provide fallback messaging.</li>
 * </ul>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles validation errors thrown when @Valid annotated parameters fail validation.
     *
     * @param ex The {@link MethodArgumentNotValidException} instance containing validation details.
     * @return A map containing field names and associated error messages with HTTP 400 Bad Request status.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        // Extract each field-specific validation error and put it into the response map
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        // Return with 400 Bad Request
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles all uncaught exceptions.
     *
     * <p><strong>Note:</strong> This is a generic fallback and should be used carefully.
     * For production systems, specific exception types should be handled individually.</p>
     *
     * @param ex The exception thrown anywhere in the application.
     * @return A generic error message map with HTTP 500 Internal Server Error status.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        Map<String, String> response = new HashMap<>();

        // Provide a generic error response with exception message
        response.put("error", ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
