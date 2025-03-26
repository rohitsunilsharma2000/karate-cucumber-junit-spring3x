package com.example.johnson.controller;

import com.example.johnson.dto.GraphRequestDTO;
import com.example.johnson.dto.GraphResponseDTO;
import com.example.johnson.mapper.GraphMapper;
import com.example.johnson.service.JohnsonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller to handle graph input and return shortest paths.
 */
@RestController
@RequestMapping("/api/graph")
public class GraphController {

    @Autowired
    private JohnsonService johnsonService;

    /**
     * Accepts a POST request with graph data and returns all-pairs shortest paths.
     */
    @PostMapping("/shortest-paths")
    public GraphResponseDTO getAllPairsShortestPaths(@RequestBody GraphRequestDTO graphRequestDTO) {
        return johnsonService.computeAllPairsShortestPaths(GraphMapper.toDomain(graphRequestDTO));
    }
}
