package com.example.graph.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link MaxFlowException}.
 *
 * <p><strong>Purpose:</strong> To verify the correctness of the custom runtime exception
 * {@link MaxFlowException}, including its construction and message propagation.</p>
 */
public class MaxFlowExceptionTest {

    /**
     * Test to verify that {@link MaxFlowException} correctly stores and returns the detail message.
     *
     * <p><strong>Scenario:</strong> Creating a new exception with a custom message and asserting the message is stored properly.</p>
     *
     * <p><strong>Expectation:</strong> The exception's message should match the one provided during construction.</p>
     */
    @Test
    public void testMaxFlowExceptionMessage() {
        String errorMessage = "Max flow computation failed due to invalid graph configuration.";

        MaxFlowException exception = new MaxFlowException(errorMessage);

        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage(), "The exception message should match the input message");
    }

    /**
     * Test to ensure {@link MaxFlowException} is a subclass of {@link RuntimeException}.
     *
     * <p><strong>Expectation:</strong> Instance of {@link MaxFlowException} should also be instance of {@link RuntimeException}.</p>
     */
    @Test
    public void testMaxFlowExceptionIsRuntimeException() {
        MaxFlowException exception = new MaxFlowException("Flow error");
        assertInstanceOf(RuntimeException.class, exception, "MaxFlowException should extend RuntimeException");
    }
}

