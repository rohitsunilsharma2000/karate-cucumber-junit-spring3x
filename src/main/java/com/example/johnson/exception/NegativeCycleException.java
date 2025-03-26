package com.example.johnson.exception;

/**
 * Exception thrown when a negative-weight cycle is detected in the graph.
 */
public class NegativeCycleException extends RuntimeException {
    public NegativeCycleException(String message) {
        super(message);
    }
}