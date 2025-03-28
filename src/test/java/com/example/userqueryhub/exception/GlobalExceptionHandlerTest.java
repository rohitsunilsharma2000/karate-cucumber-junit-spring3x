package com.example.userqueryhub.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for {@link GlobalExceptionHandler} to ensure full code coverage.
 *
 * <p>
 * This test class simulates exceptions to verify that the GlobalExceptionHandler correctly processes:
 * <ul>
 *   <li>{@link UserNotFoundException} - returning a 404 Not Found response with an appropriate error message.</li>
 *   <li>Generic {@link Exception} - returning a 500 Internal Server Error response with a generic error message.</li>
 * </ul>
 * Each test asserts that the returned {@link ResponseEntity} has the expected HTTP status code and message.
 * </p>
 *
 * @version 1.0
 */
public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    /**
     * Tests that the {@link GlobalExceptionHandler#handleResourceNotFound(UserNotFoundException)} method
     * returns a {@link ResponseEntity} with HTTP status 404 and the correct error message.
     *
     * <p>
     * <strong>GIVEN:</strong> A {@link UserNotFoundException} with a specific error message.
     * <br>
     * <strong>WHEN:</strong> The handleResourceNotFound method is invoked.
     * <br>
     * <strong>THEN:</strong> A ResponseEntity with status 404 and a body containing the error message should be returned.
     * </p>
     */
    @Test
    public void testHandleResourceNotFound() {
        // GIVEN: Define an error message and create a UserNotFoundException instance.
        String errorMessage = "User not found";
        UserNotFoundException ex = new UserNotFoundException(errorMessage);

        // WHEN: Invoke the exception handler for resource not found.
        ResponseEntity<?> response = globalExceptionHandler.handleResourceNotFound(ex);

        // THEN: Assert that the response has a 404 status and the expected error message in the body.
        assertEquals(404, response.getStatusCodeValue(), "Expected status code 404 for UserNotFoundException");
        assertEquals(errorMessage, response.getBody(), "Expected response body to match the exception message");
    }

    /**
     * Tests that the {@link GlobalExceptionHandler#handleGeneric(Exception)} method returns
     * a {@link ResponseEntity} with HTTP status 500 and a generic error message.
     *
     * <p>
     * <strong>GIVEN:</strong> A generic {@link Exception} with a custom message.
     * <br>
     * <strong>WHEN:</strong> The handleGeneric method is invoked.
     * <br>
     * <strong>THEN:</strong> A ResponseEntity with status 500 and a body containing "Internal server error" should be returned.
     * </p>
     */
    @Test
    public void testHandleGeneric() {
        // GIVEN: Create a generic exception instance.
        Exception ex = new Exception("Unexpected error");

        // WHEN: Invoke the generic exception handler.
        ResponseEntity<?> response = globalExceptionHandler.handleGeneric(ex);

        // THEN: Assert that the response has a 500 status and a generic error message.
        assertEquals(500, response.getStatusCodeValue(), "Expected status code 500 for generic exceptions");
        assertEquals("Internal server error", response.getBody(), "Expected generic error message in response body");
    }
}
