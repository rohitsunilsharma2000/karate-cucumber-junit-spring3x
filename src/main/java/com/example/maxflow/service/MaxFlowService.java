package com.example.maxflow.service;

import com.example.maxflow.exception.MaxFlowException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Service class providing functionality for calculating the maximum flow in a directed graph
 * using the Ford-Fulkerson algorithm.
 *
 * <p><strong>Overview:</strong></p>
 * This service implements the Ford-Fulkerson algorithm using breadth-first search (BFS)
 * to find augmenting paths. It includes comprehensive input validation to ensure data integrity,
 * custom exception handling to provide meaningful feedback when validations fail, and extensive logging
 * for traceability during development and troubleshooting.
 *
 * <p><strong>Input Validation and Data Integrity:</strong></p>
 * <ul>
 *     <li>Verifies that the provided graph is non-null and represented by a square matrix.</li>
 *     <li>Ensures that all capacities in the matrix are non-negative.</li>
 *     <li>Checks that the source and sink indices are within the valid range.</li>
 * </ul>
 *
 * <p><strong>Custom Exception Handling and Security:</strong></p>
 * <ul>
 *     <li>If any input validation fails (e.g., null graph, non-square matrix, negative capacity, or invalid indices),
 *     a {@link MaxFlowException} is thrown with an informative message.</li>
 *     <li>Input validations help to mitigate risks associated with processing malformed or malicious data.</li>
 * </ul>
 *
 * <p><strong>Logging and Traceability:</strong></p>
 * <ul>
 *     <li>Integrates logging across all application layers using multiple log levels (INFO, DEBUG, ERROR).</li>
 *     <li>Provides clear traceability of algorithm progress and validation errors for easier troubleshooting.</li>
 * </ul>
 *
 * <p><strong>Usage:</strong></p>
 * <ul>
 *     <li>Inject and use the {@code MaxFlowService} to compute maximum flow using the {@link #fordFulkerson(int[][], int, int)} method.</li>
 *     <li>Ensure that the input graph is properly sanitized before calling the service to maintain security.</li>
 * </ul>
 */
@Service
public class MaxFlowService {

    private static final Logger logger = LoggerFactory.getLogger(MaxFlowService.class);

    /**
     * Calculates the maximum flow from the source to sink in the provided graph using the Ford-Fulkerson algorithm.
     *
     * <p><strong>Description:</strong></p>
     * This method computes the maximum flow of a network represented by a capacity matrix. It first performs
     * thorough input validation, then iteratively uses a breadth-first search (BFS) to identify augmenting paths,
     * adjusts the residual capacities, and accumulates the total flow until no further paths exist.
     *
     * <p><strong>Input Validation:</strong></p>
     * <ul>
     *     <li>Ensures the graph is non-null and the matrix is square.</li>
     *     <li>Verifies that all edge capacities are non-negative.</li>
     *     <li>Checks that the source and sink vertices are within valid bounds.</li>
     * </ul>
     *
     * <p><strong>Security Considerations:</strong></p>
     * <ul>
     *     <li>Input validations help to prevent potential exploits from malicious data inputs.</li>
     * </ul>
     *
     * @param graph  The capacity matrix representing the graph. Must be a non-null square matrix with non-negative integers.
     * @param source The index of the source vertex.
     * @param sink   The index of the sink vertex.
     * @return The maximum flow from the source to sink.
     * @throws MaxFlowException if the graph is null, not square, contains negative capacities, or if the source/sink indices are invalid.
     */
    public int fordFulkerson(int[][] graph, int source, int sink) {
        logger.info("Starting maximum flow calculation using Ford-Fulkerson algorithm.");

        // Validate the graph input
        if (graph == null) {
            logger.error("Graph cannot be null.");
            throw new MaxFlowException("Graph cannot be null.");
        }
        int n = graph.length;
        if (n == 0) {
            logger.error("Graph must have at least one vertex.");
            throw new MaxFlowException("Graph must have at least one vertex.");
        }
        // Ensure the matrix is square and all capacities are non-negative
        for (int i = 0; i < n; i++) {
            if (graph[i] == null || graph[i].length != n) {
                logger.error("Graph must be represented by a square matrix. Issue at row: {}", i);
                throw new MaxFlowException("Graph must be represented by a square matrix.");
            }
            for (int j = 0; j < n; j++) {
                if (graph[i][j] < 0) {
                    logger.error("Negative capacity detected at edge ({}, {}).", i, j);
                    throw new MaxFlowException("Negative capacity detected at edge (" + i + ", " + j + ").");
                }
            }
        }
        // Validate source and sink indices
        if (source < 0 || source >= n) {
            logger.error("Invalid source index: {}", source);
            throw new MaxFlowException("Invalid source index: " + source);
        }
        if (sink < 0 || sink >= n) {
            logger.error("Invalid sink index: {}", sink);
            throw new MaxFlowException("Invalid sink index: " + sink);
        }
        // If source equals sink, maximum flow is zero by definition
        if (source == sink) {
            logger.info("Source equals sink. Returning 0 as maximum flow.");
            return 0;
        }

        try {
            int u, v;
            // Create a residual graph and initialize it with capacities from the original graph
            int[][] rGraph = new int[n][n];
            for (u = 0; u < n; u++) {
                for (v = 0; v < n; v++) {
                    rGraph[u][v] = graph[u][v];
                }
            }

            int[] parent = new int[n]; // Array to store the augmenting path
            int maxFlow = 0; // Initialize maximum flow to zero

            logger.debug("Entering main loop to find augmenting paths.");
            // Augment the flow while there is a path from source to sink
            while (bfs(rGraph, source, sink, parent)) {
                // Find the minimum residual capacity along the path found by BFS
                int pathFlow = Integer.MAX_VALUE;
                for (v = sink; v != source; v = parent[v]) {
                    u = parent[v];
                    pathFlow = Math.min(pathFlow, rGraph[u][v]);
                }
                logger.debug("Augmenting path found with flow: {}", pathFlow);

                // Update residual capacities for the edges and reverse edges along the path
                for (v = sink; v != source; v = parent[v]) {
                    u = parent[v];
                    rGraph[u][v] -= pathFlow;
                    rGraph[v][u] += pathFlow;
                }

                maxFlow += pathFlow;
                logger.info("Updated maximum flow: {}", maxFlow);
            }

            logger.info("Ford-Fulkerson algorithm completed. Total maximum flow: {}", maxFlow);
            return maxFlow;
        } catch (Exception ex) {
            logger.error("An error occurred while running the Ford-Fulkerson algorithm.", ex);
            throw new MaxFlowException("Something went wrong while running the algorithm.");
        }
    }

    /**
     * Performs a breadth-first search on the residual graph to find an augmenting path from source to sink.
     *
     * <p><strong>Description:</strong></p>
     * This helper method searches for an augmenting path in the residual graph. It updates the parent array to store
     * the path, and returns true if a path from source to sink exists, or false otherwise.
     *
     * @param rGraph The residual graph representing the available capacities.
     * @param s      The source vertex.
     * @param t      The sink vertex.
     * @param parent An array to store the path from source to sink.
     * @return {@code true} if an augmenting path exists; {@code false} otherwise.
     */
    private boolean bfs(int[][] rGraph, int s, int t, int[] parent) {
        int n = rGraph.length;
        boolean[] visited = new boolean[n];
        Queue<Integer> queue = new LinkedList<>();
        queue.add(s);
        visited[s] = true;
        parent[s] = -1;

        logger.debug("Starting BFS for augmenting path search from source: {} to sink: {}.", s, t);
        while (!queue.isEmpty()) {
            int u = queue.poll();
            logger.debug("Processing vertex: {}", u);
            for (int v = 0; v < n; v++) {
                if (!visited[v] && rGraph[u][v] > 0) {
                    parent[v] = u;
                    visited[v] = true;
                    logger.debug("Vertex {} visited via vertex {}.", v, u);
                    if (v == t) {
                        logger.debug("Sink {} reached during BFS.", t);
                        return true;
                    }
                    queue.add(v);
                }
            }
        }
        logger.debug("No augmenting path found in BFS.");
        return false;
    }
}
