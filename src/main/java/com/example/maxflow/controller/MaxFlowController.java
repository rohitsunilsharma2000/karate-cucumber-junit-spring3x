package com.example.maxflow.controller;

import com.example.maxflow.exception.MaxFlowException;
import com.example.maxflow.service.MaxFlowService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for calculating the maximum flow in a flow network using the Ford-Fulkerson algorithm.
 */
@RestController
@RequestMapping("/api/maxflow")
@Validated
public class MaxFlowController {

    private static final Logger logger = LoggerFactory.getLogger(MaxFlowController.class);

    private final MaxFlowService maxFlowService;

    public MaxFlowController(MaxFlowService maxFlowService) {
        this.maxFlowService = maxFlowService;
    }

    /**
     * Calculates the maximum flow from source to sink in a directed graph.
     *
     * @param request Validated request payload containing the graph, source, and sink indices.
     * @return Maximum flow value as ResponseEntity or error if validation fails.
     */
    @PostMapping("/calculate")
    public ResponseEntity<?> calculateMaxFlow(@Valid @RequestBody MaxFlowRequest request) {
        logger.info("Received max flow calculation request from source={} to sink={}", request.getSource(), request.getSink());

        try {
            logger.debug("Input graph: {}", (Object) request.getGraph());

            int maxFlow = maxFlowService.fordFulkerson(request.getGraph(), request.getSource(), request.getSink());

            logger.info("Computed max flow: {}", maxFlow);
            return ResponseEntity.ok(maxFlow);

        } catch (MaxFlowException e) {
            logger.error("MaxFlowException occurred: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        } catch (Exception e) {
            logger.error("Unexpected error while computing max flow: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("An unexpected error occurred. Please try again later.");
        }
    }

    /**
     * Request DTO for max flow calculation, validated at the controller level.
     */
    public static class MaxFlowRequest {

        @NotNull(message = "Graph must not be null.")
        private int[][] graph;

        @Min(value = 0, message = "Source vertex index must be non-negative.")
        private int source;

        @Min(value = 0, message = "Sink vertex index must be non-negative.")
        private int sink;

        public int[][] getGraph() {
            return graph;
        }

        public void setGraph(int[][] graph) {
            this.graph = graph;
        }

        public int getSource() {
            return source;
        }

        public void setSource(int source) {
            this.source = source;
        }

        public int getSink() {
            return sink;
        }

        public void setSink(int sink) {
            this.sink = sink;
        }
    }
}
