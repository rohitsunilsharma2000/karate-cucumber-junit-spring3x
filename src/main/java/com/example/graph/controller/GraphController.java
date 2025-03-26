package com.example.graph.controller;


import com.example.graph.dto.GraphRequest;
import com.example.graph.service.GraphService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/graph")
public class GraphController {

    private static final Logger logger = LoggerFactory.getLogger(GraphController.class);

    @Autowired
    private GraphService graphService;

    @PostMapping("/bridges")
    public List<String> getBridges(@RequestBody @Valid GraphRequest request) {
        logger.info("Received request to find bridges with vertices: {} and edges: {}", request.getVertices(), request.getEdges());
        List<String> bridges = graphService.findBridges(request.getVertices(), request.getEdges());
        logger.info("Bridges found: {}", bridges);
        return bridges;
    }

    @PostMapping("/articulation")
    public List<Integer> getArticulationPoints(@RequestBody @Valid GraphRequest request) {
        logger.info("Received request to find articulation points with vertices: {} and edges: {}", request.getVertices(), request.getEdges());
        List<Integer> articulationPoints = graphService.findArticulationPoints(request.getVertices(), request.getEdges());
        logger.info("Articulation points found: {}", articulationPoints);
        return articulationPoints;
    }
}
