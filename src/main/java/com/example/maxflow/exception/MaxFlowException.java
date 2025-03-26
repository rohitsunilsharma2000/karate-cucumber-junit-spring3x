package com.example.maxflow.exception;




/**
 * Custom exception for errors related to the maximum flow computation.
 */
public class MaxFlowException extends RuntimeException {

    /**
     * Constructs a new {@code MaxFlowException} with the specified detail message.
     *
     * @param message the detail message.
     */
    public MaxFlowException(String message) {
        super(message);
    }
}
