package com.example.maxflow.controller;

import com.example.maxflow.model.Graph;
import com.example.maxflow.service.MaxFlowService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for calculating the maximum flow in a flow network using the Edmonds-Karp algorithm.
 *
 * <p><strong>Overview:</strong></p>
 * This controller exposes an endpoint to calculate the maximum flow from a given source to a sink vertex.
 * It leverages {@link MaxFlowService} for computation, and validates input using JSR-380 annotations.
 *
 * <p><strong>Usage:</strong></p>
 * <ul>
 *   <li>POST a JSON representation of a graph along with query parameters specifying source and sink.</li>
 *   <li>The API returns the maximum flow as an integer with HTTP 200 (OK) status if the input is valid.</li>
 * </ul>
 *
 * <p><strong>Pass/Fail Conditions:</strong></p>
 * <ul>
 *   <li><b>Pass:</b> Valid graph input returns the correct max flow.</li>
 *   <li><b>Fail:</b> Invalid input (e.g., negative vertices, invalid indices) results in an error response.</li>
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
     * This endpoint accepts a graph (validated using JSR-380 annotations) in JSON format, along with source and sink query parameters.
     * It delegates the computation to {@link MaxFlowService} which processes the graph using the Edmonds-Karp algorithm.
     *
     * @param graph  the flow network represented as a {@link Graph} object; validated for correctness.
     * @param source the index of the source vertex.
     * @param sink   the index of the sink vertex.
     * @return the maximum flow from the source to the sink.
     */
    @PostMapping("/calculate")
    public int calculateMaxFlow(@Valid @RequestBody Graph graph,
                                @RequestParam int source,
                                @RequestParam int sink) {
        return maxFlowService.edmondsKarp(graph, source, sink);
    }
}
