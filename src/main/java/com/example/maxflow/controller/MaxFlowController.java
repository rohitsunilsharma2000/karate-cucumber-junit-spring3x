package com.example.maxflow.controller;

import com.example.maxflow.exception.MaxFlowException;
import com.example.maxflow.service.MaxFlowService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for calculating the maximum flow in a flow network using the Ford-Fulkerson algorithm.
 *
 * <p><strong>Overview:</strong></p>
 * This controller exposes an endpoint to calculate the maximum flow from a given source to a sink in a directed graph.
 * The request is received in JSON format via a POST request and is mapped to a {@link MaxFlowRequest} object.
 * The business logic is delegated to the {@link MaxFlowService}, which performs further input validation, data integrity checks,
 * custom exception handling, and security measures.
 *
 * <p><strong>Input Validation and Data Integrity:</strong></p>
 * <ul>
 *     <li>The incoming {@code MaxFlowRequest} is validated to ensure that the graph is non-null and that it represents a square matrix.</li>
 *     <li>It also validates that the provided source and sink indices are within valid bounds.</li>
 * </ul>
 *
 * <p><strong>Custom Exception Handling and Security:</strong></p>
 * <ul>
 *     <li>Any validation errors in the service layer will trigger a {@link MaxFlowException} with a clear error message.</li>
 *     <li>This exception can be caught by a global exception handler to provide a secure and informative error response (e.g., HTTP 400).</li>
 *     <li>Input validation helps protect the system from malformed or malicious data inputs.</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/maxflow")
public class MaxFlowController {

    private final MaxFlowService maxFlowService;

    /**
     * Constructs a new {@code MaxFlowController} with the specified {@link MaxFlowService}.
     *
     * @param maxFlowService The service providing the maximum flow calculation logic.
     */
    public MaxFlowController(MaxFlowService maxFlowService) {
        this.maxFlowService = maxFlowService;
    }

    /**
     * Calculates the maximum flow from the specified source to sink in the provided graph.
     *
     * <p><strong>Description:</strong></p>
     * This endpoint accepts a JSON representation of the graph along with the source and sink vertices.
     * The {@link MaxFlowService} processes the request by performing input validation, computing the maximum flow,
     * and handling any custom exceptions.
     *
     * <p><strong>Input Validation and Data Integrity:</strong></p>
     * <ul>
     *     <li>Ensures the graph is non-null and is a square matrix of non-negative capacities.</li>
     *     <li>Checks that the source and sink indices are within the bounds of the graph.</li>
     * </ul>
     *
     * <p><strong>Custom Exception Handling and Security:</strong></p>
     * <ul>
     *     <li>Any errors detected during input validation or computation will result in a {@link MaxFlowException}, which is
     *     handled to return a suitable HTTP error response (e.g., HTTP 400).</li>
     *     <li>This minimizes the risk of processing invalid or malicious data.</li>
     * </ul>
     *
     * @param request The request body encapsulating the graph, source, and sink.
     * @return A {@link ResponseEntity} containing the maximum flow as an integer and an HTTP status code.
     */
    @PostMapping("/calculate")
    public ResponseEntity<?> calculateMaxFlow(@RequestBody MaxFlowRequest request) {
        try {
            // Basic controller-level validation could be added here if needed.
            if (request == null || request.getGraph() == null) {
                throw new MaxFlowException("Request or graph cannot be null.");
            }
            int maxFlow = maxFlowService.fordFulkerson(request.getGraph(), request.getSource(), request.getSink());
            return ResponseEntity.ok(maxFlow);
        } catch (MaxFlowException e) {
            // Return a bad request response with the error message
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Represents a request for maximum flow calculation.
     *
     * <p><strong>Fields:</strong></p>
     * <ul>
     *     <li><strong>graph:</strong> A 2D integer array representing the capacity matrix of the graph.
     *         The matrix must be square and contain non-negative values.</li>
     *     <li><strong>source:</strong> The index of the source vertex.</li>
     *     <li><strong>sink:</strong> The index of the sink vertex.</li>
     * </ul>
     *
     * <p><strong>Usage:</strong></p>
     * <ul>
     *     <li>Clients must send a valid JSON body matching this structure when calling the /calculate endpoint.</li>
     * </ul>
     */
    public static class MaxFlowRequest {
        private int[][] graph;
        private int source;
        private int sink;

        /**
         * Returns the capacity matrix of the graph.
         *
         * @return A 2D integer array representing the graph.
         */
        public int[][] getGraph() {
            return graph;
        }

        /**
         * Sets the capacity matrix of the graph.
         *
         * @param graph A 2D integer array representing the graph.
         */
        public void setGraph(int[][] graph) {
            this.graph = graph;
        }

        /**
         * Returns the source vertex index.
         *
         * @return The index of the source vertex.
         */
        public int getSource() {
            return source;
        }

        /**
         * Sets the source vertex index.
         *
         * @param source The index of the source vertex.
         */
        public void setSource(int source) {
            this.source = source;
        }

        /**
         * Returns the sink vertex index.
         *
         * @return The index of the sink vertex.
         */
        public int getSink() {
            return sink;
        }

        /**
         * Sets the sink vertex index.
         *
         * @param sink The index of the sink vertex.
         */
        public void setSink(int sink) {
            this.sink = sink;
        }
    }
}
