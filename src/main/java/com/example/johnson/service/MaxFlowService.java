package com.example.johnson.service;

import com.example.johnson.exception.MaxFlowException;
import com.example.johnson.model.Graph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * Service class providing functionality for calculating the maximum flow in a directed graph
 * using the Edmonds-Karp algorithm.
 *
 * <p><strong>Overview:</strong></p>
 * This service implements the Edmonds-Karp algorithm, which is an implementation of the Ford-Fulkerson
 * method. The algorithm repeatedly searches for augmenting paths in a flow network using a breadth-first search (BFS)
 * and updates the flow until no further augmenting paths can be found.
 *
 * <p><strong>Usage:</strong></p>
 * <ul>
 *     <li>Create an instance of {@code MaxFlowService} and invoke {@link #edmondsKarp(Graph, int, int)}
 *         with a properly defined {@link Graph}, a source vertex, and a sink vertex.</li>
 *     <li>The {@link Graph} must include the number of vertices and an adjacency mapping that represents the capacities.
 *         All capacities must be non-negative.</li>
 * </ul>
 *
 * <p><strong>Constraints:</strong></p>
 * <ul>
 *     <li>The graph must not be null and must have at least one vertex.</li>
 *     <li>The source and sink indices must be valid and within the range of the graph's vertices.</li>
 *     <li>All edge capacities in the graph must be non-negative.</li>
 * </ul>
 *
 * <p><strong>Pass/Fail Conditions:</strong></p>
 * <ul>
 *     <li><strong>Pass:</strong> Successfully computes the maximum flow from the source to the sink when
 *         provided with a valid graph and valid vertex indices.</li>
 *     <li><strong>Fail:</strong> Throws a {@link MaxFlowException} if the graph is null, if the vertex count is invalid,
 *         if any capacity is negative, or if the source/sink indices are out of bounds.</li>
 * </ul>
 *
 * @see Graph
 */
@Service
public class MaxFlowService {

    private static final Logger logger = LoggerFactory.getLogger(MaxFlowService.class);

    /**
     * Calculates the maximum flow from a specified source to sink in the provided graph using the Edmonds-Karp algorithm.
     *
     * <p><strong>Description:</strong></p>
     * This method initializes capacity and flow matrices based on the provided {@link Graph}. It then employs
     * a breadth-first search (BFS) to identify augmenting paths in the residual graph. For each found path, the method
     * computes the bottleneck capacity (i.e., the minimum residual capacity along the path), updates the flow accordingly,
     * and accumulates the total maximum flow. The process repeats until no augmenting path exists.
     *
     * <p><strong>Parameters:</strong></p>
     * <ul>
     *     <li><strong>graph</strong> (<em>{@link Graph}</em>): The directed graph representing the flow network,
     *         including vertices and capacity mappings. Must be non-null, with at least one vertex and non-negative edge capacities.</li>
     *     <li><strong>source</strong> (<em>int</em>): The index of the source vertex where the flow originates.</li>
     *     <li><strong>sink</strong> (<em>int</em>): The index of the sink vertex where the flow is collected.</li>
     * </ul>
     *
     * <p><strong>Return Value:</strong></p>
     * Returns an integer representing the maximum flow from the source to the sink.
     *
     * <p><strong>Pass/Fail Conditions:</strong></p>
     * <ul>
     *     <li><strong>Pass:</strong> The method correctly computes the maximum flow when provided with valid inputs.</li>
     *     <li><strong>Fail:</strong> Throws a {@link MaxFlowException} if the graph is null, has no vertices, contains negative capacities,
     *         or if the provided source/sink indices are invalid.</li>
     * </ul>
     *
     * @param graph  The directed graph containing the vertices and capacity mappings.
     * @param source The index of the source vertex.
     * @param sink   The index of the sink vertex.
     * @return The maximum flow from the source to the sink.
     * @throws MaxFlowException if the graph is null, has no vertices, contains negative capacities, or if source/sink indices are invalid.
     */
    public int edmondsKarp(Graph graph, int source, int sink) {
        // Validate graph and vertices count
        if (graph == null) {
            throw new MaxFlowException("Graph cannot be null.");
        }
        int vertices = graph.getVertices();
        if (vertices <= 0) {
            throw new MaxFlowException("Graph must have at least one vertex.");
        }
        if (source < 0 || source >= vertices) {
            throw new MaxFlowException("Invalid source vertex: " + source);
        }
        if (sink < 0 || sink >= vertices) {
            throw new MaxFlowException("Invalid sink vertex: " + sink);
        }
        // Early exit if source equals sink
        if (source == sink) {
            logger.info("Source equals sink; returning 0 flow.");
            return 0;
        }

        // Validate that all capacities are non-negative
        for (Map.Entry<Integer, Map<Integer, Integer>> entry : graph.getAdjMatrix().entrySet()) {
            int u = entry.getKey();
            for (Map.Entry<Integer, Integer> edge : entry.getValue().entrySet()) {
                if (edge.getValue() < 0) {
                    throw new MaxFlowException("Negative capacity from vertex " + u + " to vertex " + edge.getKey());
                }
            }
        }

        logger.info("Starting Edmonds-Karp algorithm from source {} to sink {}.", source, sink);

        int maxFlow = 0;
        int[][] capacity = new int[vertices][vertices];
        int[][] flow = new int[vertices][vertices];

        // Initialize the capacity matrix using the graph's adjacency matrix
        for (int u : graph.getAdjMatrix().keySet()) {
            for (int v : graph.getAdjMatrix().get(u).keySet()) {
                capacity[u][v] = graph.getCapacity(u, v);
            }
        }

        // Find augmenting paths repeatedly until no such path exists
        while (true) {
            int[] parent = new int[vertices];
            boolean[] visited = new boolean[vertices];
            Queue<Integer> queue = new LinkedList<>();
            queue.add(source);
            visited[source] = true;

            // BFS to find an augmenting path in the residual graph
            while (!queue.isEmpty()) {
                int u = queue.poll();
                for (int v = 0; v < vertices; v++) {
                    if (!visited[v] && capacity[u][v] > flow[u][v]) {
                        parent[v] = u;
                        visited[v] = true;
                        queue.add(v);
                    }
                }
            }

            // If no augmenting path is found, terminate the loop
            if (!visited[sink]) {
                logger.debug("No augmenting path found; terminating algorithm.");
                break;
            }

            // Determine the bottleneck capacity along the found path
            int pathFlow = Integer.MAX_VALUE;
            for (int v = sink; v != source; v = parent[v]) {
                int u = parent[v];
                pathFlow = Math.min(pathFlow, capacity[u][v] - flow[u][v]);
            }

            // Update flows along the path and adjust reverse flows for residual capacity
            for (int v = sink; v != source; v = parent[v]) {
                int u = parent[v];
                flow[u][v] += pathFlow;
                flow[v][u] -= pathFlow;
            }

            maxFlow += pathFlow;
            logger.debug("Found augmenting path with flow {}; updated max flow is {}.", pathFlow, maxFlow);
        }

        logger.info("Edmonds-Karp algorithm completed. Maximum flow computed: {}.", maxFlow);
        return maxFlow;
    }
}
