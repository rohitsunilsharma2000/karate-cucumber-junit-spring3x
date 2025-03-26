package com.example.johnson.dto;

import java.util.Map;

/**
 * DTO for returning all-pairs shortest paths.
 */
public class GraphResponseDTO {
    private Map<String, Map<String, Integer>> shortestPaths;

    public Map<String, Map<String, Integer>> getShortestPaths() {
        return shortestPaths;
    }

    public void setShortestPaths(Map<String, Map<String, Integer>> shortestPaths) {
        this.shortestPaths = shortestPaths;
    }
}
