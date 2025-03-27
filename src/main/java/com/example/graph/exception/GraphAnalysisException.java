package com.example.graph.exception;


/**
 * Exception thrown when an error occurs during graph analysis,
 * such as detecting bridges or articulation points.
 */
public class GraphAnalysisException extends RuntimeException {

    public GraphAnalysisException(String message) {
        super(message);
    }

    public GraphAnalysisException(String message, Throwable cause) {
        super(message, cause);
    }
}
