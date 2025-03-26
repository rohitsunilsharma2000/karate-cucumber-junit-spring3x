package com.example.johnson.controller;

import com.example.johnson.dto.GraphRequestDTO;
import com.example.johnson.dto.GraphResponseDTO;
import com.example.johnson.mapper.GraphMapper;
import com.example.johnson.service.JohnsonService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * REST controller for processing graph data and returning all-pairs shortest paths.
 * <p>
 * This controller accepts a graph input in the form of vertices and edges,
 * validates the input, logs processing details at various levels, and delegates
 * computation of the shortest paths to the JohnsonService.
 * </p>
 */
@RestController
@RequestMapping("/api/graph")
@Validated
public class GraphController {

    private static final Logger logger = LoggerFactory.getLogger(GraphController.class);

    @Autowired
    private JohnsonService johnsonService;

    /**
     * Accepts a POST request with graph data and returns all-pairs shortest paths.
     * <p>
     * The input graph is validated to ensure that both vertices and edges are provided.
     * Processing logs are generated at INFO and DEBUG levels. In case of any error, an exception
     * is thrown to be handled by the global exception handler.
     * </p>
     *
     * @param graphRequestDTO the DTO representing the input graph data
     * @return a {@link GraphResponseDTO} containing the computed shortest paths
     */
    @PostMapping("/shortest-paths")
    @ResponseStatus(HttpStatus.OK)
    public GraphResponseDTO getAllPairsShortestPaths(@Valid @RequestBody GraphRequestDTO graphRequestDTO) {
        logger.info("Received graph input with {} vertices and {} edges.",
                    graphRequestDTO.getVertices().size(), graphRequestDTO.getEdges().size());
        logger.debug("Graph input details: {}", graphRequestDTO);
        try {
            // Map the DTO to the domain object and compute the shortest paths
            GraphResponseDTO response = johnsonService.computeAllPairsShortestPaths(GraphMapper.toDomain(graphRequestDTO));
            logger.info("Successfully computed all-pairs shortest paths.");
            return response;
        } catch (Exception e) {
            logger.error("Error computing shortest paths: {}", e.getMessage(), e);
            throw e; // Exception to be handled by a custom exception handler
        }
    }
}
