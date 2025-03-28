package com.example.colorsched.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link GlobalExceptionHandler}.
 * <p>
 * This test class verifies the correct transformation of exceptions
 * into HTTP responses using the centralized error handling mechanism.
 */
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    /**
     * Test: Verifies that validation exceptions are correctly mapped to 400 Bad Request responses.
     *
     * <p><strong>Scenario:</strong> Simulate a MethodArgumentNotValidException with multiple field errors.
     *
     * <p><strong>Assertions:</strong>
     * <ul>
     *   <li>HTTP status is 400</li>
     *   <li>Response body contains correct field error mappings</li>
     * </ul>
     */
    @Test
    @DisplayName("Should return 400 BAD_REQUEST with field errors")
    void testHandleValidationException() {
        // Mock binding result with two field errors
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(
                new FieldError("objectName", "field1", "must not be null"),
                new FieldError("objectName", "field2", "must be greater than zero")
        ));

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<Map<String, String>> response = handler.handleValidationException(ex);

        // Validate status and body
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("must not be null", response.getBody().get("field1"));
        assertEquals("must be greater than zero", response.getBody().get("field2"));
    }

    /**
     * Test: Verifies that generic exceptions are correctly handled with 500 Internal Server Error.
     *
     * <p><strong>Scenario:</strong> Trigger a runtime exception and verify it is caught by the generic handler.
     *
     * <p><strong>Assertions:</strong>
     * <ul>
     *   <li>HTTP status is 500</li>
     *   <li>Response body contains a generic error message</li>
     * </ul>
     */
    @Test
    @DisplayName("Should return 500 INTERNAL_SERVER_ERROR for generic exceptions")
    void testHandleGenericException() {
        Exception ex = new RuntimeException("Something went wrong");

        ResponseEntity<Map<String, String>> response = handler.handleGenericException(ex);

        // Validate status and body
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().get("error").contains("Something went wrong"));
    }
}

