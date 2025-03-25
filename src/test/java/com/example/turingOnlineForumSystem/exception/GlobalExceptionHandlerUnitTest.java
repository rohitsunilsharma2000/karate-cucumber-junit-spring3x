package com.example.turingOnlineForumSystem.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for GlobalExceptionHandler.
 * These tests call the handler methods directly and assert their responses.
 */
public class GlobalExceptionHandlerUnitTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    public void setUp() {
        handler = new GlobalExceptionHandler();
    }

    /**
     * Test handling of ResourceNotFoundException.
     * Should return HTTP 404 with the exception message as body.
     */
    @Test
    public void testHandleResourceNotFound() {
        ResourceNotFoundException ex = new ResourceNotFoundException("User not found");

        ResponseEntity<?> response = handler.handleResourceNotFound(ex);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("User not found", response.getBody());
    }

    /**
     * Test handling of generic Exception.
     * Should return HTTP 500 with "Internal server error" as body.
     */
    @Test
    public void testHandleGenericException() {
        Exception ex = new RuntimeException("Something went wrong");

        ResponseEntity<?> response = handler.handleGeneric(ex);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Internal server error", response.getBody());
    }
}
