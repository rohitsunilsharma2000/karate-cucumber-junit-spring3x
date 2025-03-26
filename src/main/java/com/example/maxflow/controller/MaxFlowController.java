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
 * <p>
 * Enhancements:
 * <ul>
 *   <li>Input Validation and Data Integrity: Utilizes validation annotations to ensure incoming data meets required criteria.</li>
 *   <li>Logging and Traceability: Employs multiple log levels (INFO, DEBUG, ERROR) to provide detailed traceability.</li>
 * </ul>
 * </p>
 */
@RestController
@RequestMapping("/api/maxflow")
@Validated
public class MaxFlowController {

    /**
     * Logger instance for logging messages with various log levels.
     */
    private static final Logger logger = LoggerFactory.getLogger(MaxFlowController.class);

    /**
     * Service layer for performing the maximum flow calculation using the Ford-Fulkerson algorithm.
     */
    private final MaxFlowService maxFlowService;

    /**
     * Constructor for dependency injection of the MaxFlowService.
     *
     * @param maxFlowService The service responsible for calculating the maximum flow.
     */
    public MaxFlowController(MaxFlowService maxFlowService) {
        this.maxFlowService = maxFlowService;
    }

    /**
     * Calculates the maximum flow from a source to a sink in a directed graph.
     * <p>
     * This method receives a validated request payload containing the graph,
     * source index, and sink index. It logs the input details at INFO and DEBUG levels,
     * invokes the service layer to compute the maximum flow, and returns the result.
     * In case of validation errors or exceptions, appropriate HTTP error statuses are returned.
     * </p>
     *
     * @param request Validated request payload containing the graph, source, and sink indices.
     * @return {@link ResponseEntity} containing the computed maximum flow value or an error message.
     */
    @PostMapping("/calculate")
    public ResponseEntity<?> calculateMaxFlow(@Valid @RequestBody MaxFlowRequest request) {
        logger.info("Received max flow calculation request from source={} to sink={}",
                    request.getSource(), request.getSink());
        logger.debug("Validating input graph and indices.");

        try {
            // Log the full graph details at DEBUG level for traceability.
            logger.debug("Input graph: {}", (Object) request.getGraph());

            // Calculate max flow using the service layer.
            int maxFlow = maxFlowService.fordFulkerson(request.getGraph(), request.getSource(), request.getSink());

            logger.info("Computed max flow successfully: {}", maxFlow);
            return ResponseEntity.ok(maxFlow);

        } catch (MaxFlowException e) {
            logger.error("MaxFlowException occurred during max flow computation: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        } catch (Exception e) {
            logger.error("Unexpected error while computing max flow: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("An unexpected error occurred. Please try again later.");
        }
    }

    /**
     * Request Data Transfer Object (DTO) for max flow calculation.
     * <p>
     * This inner static class is used to encapsulate the request data required for
     * calculating the maximum flow. Input validation is enforced via annotations.
     * </p>
     *
     * Enhancements:
     * <ul>
     *   <li>Input Validation and Data Integrity: Enforced via {@link NotNull} and {@link Min} annotations.</li>
     * </ul>
     */
    public static class MaxFlowRequest {

        /**
         * Two-dimensional array representing the graph's adjacency matrix.
         * Must not be null.
         */
        @NotNull(message = "Graph must not be null.")
        private int[][] graph;

        /**
         * Index of the source vertex in the graph.
         * Must be a non-negative integer.
         */
        @Min(value = 0, message = "Source vertex index must be non-negative.")
        private int source;

        /**
         * Index of the sink vertex in the graph.
         * Must be a non-negative integer.
         */
        @Min(value = 0, message = "Sink vertex index must be non-negative.")
        private int sink;

        /**
         * Gets the graph represented as a two-dimensional array.
         *
         * @return The adjacency matrix of the graph.
         */
        public int[][] getGraph() {
            return graph;
        }

        /**
         * Sets the graph represented as a two-dimensional array.
         *
         * @param graph The adjacency matrix of the graph.
         */
        public void setGraph(int[][] graph) {
            this.graph = graph;
        }

        /**
         * Gets the source vertex index.
         *
         * @return The source index.
         */
        public int getSource() {
            return source;
        }

        /**
         * Sets the source vertex index.
         *
         * @param source The source index.
         */
        public void setSource(int source) {
            this.source = source;
        }

        /**
         * Gets the sink vertex index.
         *
         * @return The sink index.
         */
        public int getSink() {
            return sink;
        }

        /**
         * Sets the sink vertex index.
         *
         * @param sink The sink index.
         */
        public void setSink(int sink) {
            this.sink = sink;
        }
    }
}
