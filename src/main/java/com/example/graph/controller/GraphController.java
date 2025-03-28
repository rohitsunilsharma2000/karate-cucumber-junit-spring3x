package com.example.graph.controller;

import com.example.graph.dto.GraphRequest;
import com.example.graph.service.GraphService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for graph analysis operations.
 *
 * <p><strong>Overview:</strong></p>
 * This controller exposes REST endpoints to analyze undirected graphs and detect:
 * <ul>
 *   <li><strong>Bridge Edges:</strong> Edges whose removal increases the number of connected components.</li>
 *   <li><strong>Articulation Points:</strong> Vertices whose removal increases the number of connected components.</li>
 * </ul>
 *
 * <p><strong>Responsibilities:</strong></p>
 * <ul>
 *   <li>Receives graph data from clients as JSON input.</li>
 *   <li>Validates the input structure using Jakarta Bean Validation annotations.</li>
 *   <li>Delegates processing to the {@link GraphService}.</li>
 *   <li>Returns analysis results in a user-friendly format.</li>
 * </ul>
 *
 * <p><strong>Base URL:</strong> <code>/graph</code></p>
 * <p><strong>Validation Handling:</strong> Managed globally via {@link com.example.graph.exception.GlobalExceptionHandler}</p>
 * <p><strong>Logging:</strong> Uses SLF4J to trace request and response data.</p>
 *
 * @author
 */
@RestController
@RequestMapping("/graph")
public class GraphController {

    // Logger to track request and response flow
    private static final Logger logger = LoggerFactory.getLogger(GraphController.class);

    // GraphService is injected via constructor
    private final GraphService graphService;

    /**
     * Constructor-based dependency injection for GraphService.
     *
     * @param graphService The service that implements graph algorithms.
     */
    public GraphController(GraphService graphService) {
        this.graphService = graphService;
    }

    /**
     * POST endpoint to identify all bridge edges in an undirected graph.
     *
     * <p><strong>Bridge edges</strong> are those whose removal would increase the number of connected components.</p>
     *
     * @param request A valid {@link GraphRequest} object containing vertex and edge data.
     * @return A list of bridge edges in the format ["1-2", "2-3"], representing critical edges.
     *
     * <p><strong>Pass/Fail Conditions:</strong></p>
     * <ul>
     *   <li><strong>Pass:</strong> Returns list of bridges if graph is valid.</li>
     *   <li><strong>Fail:</strong> Returns 400 if validation fails, 500 if internal graph error occurs.</li>
     * </ul>
     */
    @PostMapping("/bridges")
    public List<String> getBridges(@RequestBody @Valid GraphRequest request) {
        // Log the input graph for traceability
        logger.info("Received request to find bridges with vertices: {} and edges: {}",
                    request.getVertices(), request.getEdges());

        // Delegate bridge-finding logic to the service layer
        List<String> bridges = graphService.findBridges(request.getVertices(), request.getEdges());

        // Log the result of the bridge detection
        logger.info("Bridges found: {}", bridges);

        return bridges;
    }

    /**
     * POST endpoint to identify all articulation points (critical vertices) in the graph.
     *
     * <p><strong>Articulation points</strong> are vertices whose removal would increase the number of connected components.</p>
     *
     * @param request A valid {@link GraphRequest} object containing vertex and edge data.
     * @return A list of vertex indices representing articulation points.
     *
     * <p><strong>Pass/Fail Conditions:</strong></p>
     * <ul>
     *   <li><strong>Pass:</strong> Returns list of articulation point indices.</li>
     *   <li><strong>Fail:</strong> Returns 400 for validation issues, 500 for graph analysis failures.</li>
     * </ul>
     */
    @PostMapping("/articulation")
    public List<Integer> getArticulationPoints(@RequestBody @Valid GraphRequest request) {
        // Log incoming request for audit/debugging
        logger.info("Received request to find articulation points with vertices: {} and edges: {}",
                    request.getVertices(), request.getEdges());

        // Perform articulation point analysis
        List<Integer> articulationPoints = graphService.findArticulationPoints(
                request.getVertices(), request.getEdges());

        // Log the output result
        logger.info("Articulation points found: {}", articulationPoints);

        return articulationPoints;
    }
}
