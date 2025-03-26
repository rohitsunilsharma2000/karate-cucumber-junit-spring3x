package com.example.maxflow.exception;

/**
 * Custom exception for errors related to the maximum flow computation.
 * <p>
 * This exception is thrown when there is an issue during the execution
 * of the Ford-Fulkerson algorithm or related operations in the maximum flow calculation.
 * </p>
 */
public class MaxFlowException extends RuntimeException {

    /**
     * Constructs a new {@code MaxFlowException} with the specified detail message.
     *
     * @param message the detail message that provides more information about the exception.
     */
    public MaxFlowException(String message) {
        super(message);
    }
}
