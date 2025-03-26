package com.example.graph.controller;

import com.example.graph.dto.GraphRequest;
import com.example.graph.service.GraphService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for graph analysis operations.
 *
 * <p>
 * Provides HTTP POST endpoints to analyze undirected graphs and determine:
 * <ul>
 *   <li>Bridge Edges – Edges whose removal increases the number of connected components.</li>
 *   <li>Articulation Points – Vertices whose removal increases the number of connected components.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Validates incoming requests using Jakarta Bean Validation and delegates processing to {@link GraphService}.
 * </p>
 *
 * <p><strong>Base URL:</strong> <code>/graph</code></p>
 *
 * <p><strong>Logging:</strong> Uses SLF4J to log request input and analysis results.</p>
 *
 * <p><strong>Security:</strong> If enabled, these endpoints may be protected by HTTP Basic Auth or other Spring Security configurations.</p>
 *
 * <p><strong>Validation Failures:</strong> Handled by {@link com.example.graph.exception.GlobalExceptionHandler}</p>
 *
 * @author
 * @since 2025-03-26
 */
@RestController
@RequestMapping("/graph")
public class GraphController {

    private static final Logger logger = LoggerFactory.getLogger(GraphController.class);

    @Autowired
    private GraphService graphService;

    /**
     * Endpoint to detect all bridges (critical edges) in the graph.
     *
     * @param request A valid {@link GraphRequest} containing vertices and edges.
     * @return A list of string representations of bridge edges (e.g., "1-3", "3-4").
     */
    @PostMapping("/bridges")
    public List<String> getBridges(@RequestBody @Valid GraphRequest request) {
        logger.info("Received request to find bridges with vertices: {} and edges: {}", request.getVertices(), request.getEdges());
        List<String> bridges = graphService.findBridges(request.getVertices(), request.getEdges());
        logger.info("Bridges found: {}", bridges);
        return bridges;
    }

    /**
     * Endpoint to detect all articulation points (critical vertices) in the graph.
     *
     * @param request A valid {@link GraphRequest} containing vertices and edges.
     * @return A list of articulation point vertex indices.
     */
    @PostMapping("/articulation")
    public List<Integer> getArticulationPoints(@RequestBody @Valid GraphRequest request) {
        logger.info("Received request to find articulation points with vertices: {} and edges: {}", request.getVertices(), request.getEdges());
        List<Integer> articulationPoints = graphService.findArticulationPoints(request.getVertices(), request.getEdges());
        logger.info("Articulation points found: {}", articulationPoints);
        return articulationPoints;
    }
}
