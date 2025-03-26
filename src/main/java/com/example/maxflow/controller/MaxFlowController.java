package com.example.maxflow.controller;

import com.example.maxflow.model.Graph;
import com.example.maxflow.service.MaxFlowService;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for calculating the maximum flow in a flow network using the Edmonds-Karp algorithm.
 *
 * <p><strong>Overview:</strong></p>
 * This controller provides an endpoint to calculate the maximum flow in a given directed graph.
 * The graph is represented using the {@link Graph} model, and the calculation logic is delegated
 * to the {@link MaxFlowService} which implements the Edmonds-Karp algorithm.
 *
 * <p><strong>References:</strong></p>
 * <ul>
 *   <li>{@link Graph} represents the network with vertices and capacity mappings.</li>
 *   <li>{@link MaxFlowService} contains the implementation of the maximum flow algorithm.</li>
 * </ul>
 *
 * <p><strong>Functionality:</strong></p>
 * <ul>
 *   <li>Accepts a graph definition in JSON format via a POST request.</li>
 *   <li>Requires the client to specify a source and sink vertex through query parameters.</li>
 *   <li>Calculates and returns the maximum possible flow from the source to the sink using the Edmonds-Karp algorithm.</li>
 * </ul>
 *
 * <p><strong>Acceptable Values / Constraints:</strong></p>
 * <ul>
 *   <li><strong>Graph:</strong> Must contain a valid number of vertices and a correctly structured adjacency matrix mapping each vertex to its neighbors and their respective capacities.</li>
 *   <li><strong>source:</strong> Must be a valid vertex index in the graph.</li>
 *   <li><strong>sink:</strong> Must be a valid vertex index in the graph.</li>
 * </ul>
 *
 * <p><strong>Error Conditions:</strong></p>
 * <ul>
 *   <li>If the graph is not properly defined, the service may return an error response indicating invalid input.</li>
 *   <li>If the specified source or sink vertices are invalid (e.g., out of bounds), the service may return an error response.</li>
 * </ul>
 *
 * <p><strong>Pass/Fail Conditions:</strong></p>
 * <ul>
 *   <li><strong>Pass:</strong> Successfully calculates the maximum flow and returns an integer value representing the maximum flow with HTTP 200 (OK) status.</li>
 *   <li><strong>Fail:</strong> Returns an error response (e.g., HTTP 400) if input constraints are violated or if the computation cannot be performed due to invalid parameters.</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/maxflow")
public class MaxFlowController {

    private final MaxFlowService maxFlowService;

    /**
     * Constructs a new {@code MaxFlowController} and initializes the {@link MaxFlowService}.
     */
    public MaxFlowController() {
        this.maxFlowService = new MaxFlowService();
    }

    /**
     * Calculates the maximum flow from the specified source to sink in the provided graph.
     *
     * <p><strong>Description:</strong></p>
     * This endpoint accepts a graph representation in JSON format and query parameters specifying the
     * source and sink vertices. The graph is processed using the Edmonds-Karp algorithm implemented
     * in the {@link MaxFlowService} to compute the maximum flow from the source to the sink.
     *
     * <p><strong>Parameters:</strong></p>
     * <ul>
     *   <li><strong>graph</strong> (<em>{@link Graph}</em>): The flow network graph, including vertices and capacity mappings.</li>
     *   <li><strong>source</strong> (<em>int</em>): The index of the source vertex in the graph.</li>
     *   <li><strong>sink</strong> (<em>int</em>): The index of the sink vertex in the graph.</li>
     * </ul>
     *
     * <p><strong>Pass/Fail Conditions:</strong></p>
     * <ul>
     *   <li><strong>Pass:</strong> Returns the computed maximum flow as an integer with HTTP 200 (OK) status.</li>
     *   <li><strong>Fail:</strong> Returns an error (e.g., HTTP 400) if the graph is malformed or if source/sink indices are invalid.</li>
     * </ul>
     *
     * @param graph  The flow network represented as a {@link Graph} object.
     * @param source The index of the source vertex.
     * @param sink   The index of the sink vertex.
     * @return The maximum flow from the source to the sink.
     */
    @PostMapping("/calculate")
    public int calculateMaxFlow(@RequestBody Graph graph, @RequestParam int source, @RequestParam int sink) {
        return maxFlowService.edmondsKarp(graph, source, sink);
    }
}
