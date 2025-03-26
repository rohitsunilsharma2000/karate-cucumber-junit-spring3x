package com.example.johnson.service;

import com.example.johnson.dto.GraphRequestDTO;
import com.example.johnson.dto.GraphResponseDTO;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the JohnsonService.
 */
public class JohnsonServiceTest {

    private final JohnsonService johnsonService = new JohnsonService(); // Instantiate service for testing

    /**
     * Test the computation of shortest paths with a simple graph.
     */
    @Test
    public void testComputeShortestPaths() {
        // Prepare a simple graph request
        GraphRequestDTO graphRequest = new GraphRequestDTO();
        graphRequest.setVertices(Collections.singletonList("A"));
        GraphRequestDTO.Edge edge = new GraphRequestDTO.Edge();
        edge.setFrom("A");
        edge.setTo("A");
        edge.setWeight(0);
        graphRequest.setEdges(Collections.singletonList(edge)); // Self-loop

        // Compute shortest paths
        GraphResponseDTO response = johnsonService.computeShortestPaths(graphRequest);
        Map<String, Map<String, Integer>> shortestPaths = response.getShortestPaths();

        // Validate the result
        assertNotNull(shortestPaths);
        assertEquals(1, shortestPaths.size());
        assertTrue(shortestPaths.get("A").containsKey("A"));
        assertEquals(0, shortestPaths.get("A").get("A")); // Self-loop distance should be 0
    }
}
