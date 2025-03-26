
package com.example.maxflow.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.lang.reflect.Executable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

/**
 * Unit tests for {@link GlobalExceptionHandler}.
 *
 * <p>This class verifies that all custom exception handlers return proper responses and error messages.
 * It covers all handler methods: MaxFlowException, validation exceptions, constraint violations, and generic exceptions.</p>
 */
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    /**
     * Test 1: Validates handling of a custom MaxFlowException.
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
     * Test 2: Validates handling of ConstraintViolationException with one violation.
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
     * Test 4: Validates fallback handler for any other unhandled exception.
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
