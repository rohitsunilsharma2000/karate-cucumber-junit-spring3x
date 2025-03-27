package com.example.userqueryhub.exception;


import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.userqueryhub.exception.GlobalExceptionHandler;
import com.example.userqueryhub.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

/**
 * Unit tests for {@link GlobalExceptionHandler} to ensure full code coverage.
 *
 * <p>
 * This test class simulates exceptions to verify that the GlobalExceptionHandler correctly processes:
 * <ul>
 *   <li>{@link UserNotFoundException} - returning a 404 Not Found response.</li>
 *   <li>Generic {@link Exception} - returning a 500 Internal Server Error response.</li>
 * </ul>
 * Each test case asserts that the returned ResponseEntity has the expected HTTP status code and message.
 * </p>
 *
 * @version 1.0
 * @since 2025-03-27
 */
public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    /**
     * Tests that the {@link GlobalExceptionHandler#handleResourceNotFound(UserNotFoundException)}
     * method returns a ResponseEntity with HTTP status 404 and the correct error message.
     */
    @Test
    public void testHandleResourceNotFound() {
        String errorMessage = "User not found";
        UserNotFoundException ex = new UserNotFoundException(errorMessage);

        ResponseEntity<?> response = globalExceptionHandler.handleResourceNotFound(ex);

        assertEquals(404, response.getStatusCodeValue(), "Expected status code 404 for UserNotFoundException");
        assertEquals(errorMessage, response.getBody(), "Expected response body to match the exception message");
    }

    /**
     * Tests that the {@link GlobalExceptionHandler#handleGeneric(Exception)} method returns
     * a ResponseEntity with HTTP status 500 and a generic error message.
     */
    @Test
    public void testHandleGeneric() {
        Exception ex = new Exception("Unexpected error");

        ResponseEntity<?> response = globalExceptionHandler.handleGeneric(ex);

        assertEquals(500, response.getStatusCodeValue(), "Expected status code 500 for generic exceptions");
        assertEquals("Internal server error", response.getBody(), "Expected generic error message in response body");
    }
}
