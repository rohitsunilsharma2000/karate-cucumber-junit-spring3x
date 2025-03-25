package com.example.turingOnlineForumSystem.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for ResourceNotFoundException.
 * Verifies construction, message propagation, and exception type.
 */
public class ResourceNotFoundExceptionTest {

    /**
     * Test that the exception correctly stores and returns the message.
     */
    @Test
    public void testExceptionMessage() {
        String message = "User not found with ID 10";
        ResourceNotFoundException ex = new ResourceNotFoundException(message);

        assertEquals(message, ex.getMessage()); // Verify message is stored
    }

    /**
     * Test that the exception is a subclass of RuntimeException.
     */
    @Test
    public void testIsRuntimeException() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Some message");
        assertTrue(ex instanceof RuntimeException);
    }
}
