package com.example.maxflow.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

/**
 * Unit tests for {@link GlobalExceptionHandler}.
 * <p>
 * This class verifies that all custom exception handlers return the proper HTTP responses and error messages.
 * It covers the handling of:
 * <ul>
 *   <li>Custom {@link MaxFlowException}.</li>
 *   <li>{@link ConstraintViolationException} for bean validation errors.</li>
 *   <li>Generic exceptions.</li>
 * </ul>
 * </p>
 */
class GlobalExceptionHandlerTest {

    /**
     * Instance of the global exception handler used for testing.
     */
    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    /**
     * Test 1: Validates handling of a custom {@link MaxFlowException}.
     * <p>
     * Expects a 400 BAD_REQUEST status and an error message containing the exception message.
     * </p>
     */
    @Test
    @DisplayName("Test 1: Handle MaxFlowException - should return 400 BAD_REQUEST with error message")
    void testHandleMaxFlowException() {
        MaxFlowException ex = new MaxFlowException("Invalid source index");
        ResponseEntity<Map<String, String>> response = handler.handleMaxFlowException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().get("error").contains("Invalid source index"));
    }

    /**
     * Test 2: Validates handling of {@link ConstraintViolationException} with one violation.
     * <p>
     * Mocks a constraint violation and ensures that the returned error map correctly contains the violation message.
     * </p>
     */
    @Test
    @DisplayName("Test 2: Handle ConstraintViolationException - should map field violations correctly")
    void testHandleConstraintViolationException() {
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        when(violation.getMessage()).thenReturn("must not be null");

        Path mockPath = mock(Path.class);
        when(mockPath.toString()).thenReturn("graph");
        when(violation.getPropertyPath()).thenReturn(mockPath);

        Set<ConstraintViolation<?>> violations = new HashSet<>();
        violations.add(violation);

        ConstraintViolationException ex = new ConstraintViolationException(violations);
        ResponseEntity<Map<String, String>> response = handler.handleConstraintViolation(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("must not be null", response.getBody().get("field"));
    }

    /**
     * Test 3: Validates the generic exception handler.
     * <p>
     * Ensures that any unhandled exception returns a 500 INTERNAL_SERVER_ERROR status and a generic error message.
     * </p>
     */
    @Test
    @DisplayName("Test 3: Handle generic Exception - should return 500 INTERNAL_SERVER_ERROR")
    void testHandleOtherExceptions() {
        Exception ex = new RuntimeException("Unexpected failure");
        ResponseEntity<Map<String, String>> response = handler.handleOtherExceptions(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An unexpected error occurred.", response.getBody().get("error"));
    }
}
