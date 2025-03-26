package com.example.graph.exception;

import com.example.graph.service.GraphService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GraphServiceTest {

    private GraphService graphService;

    @BeforeEach
    public void setup() {
        graphService = new GraphService();
    }

    @Test
    public void testFindBridges_WithSampleGraph_ReturnsCorrectBridges() {
        int vertices = 5;
        List<List<Integer>> edges = List.of(
                List.of(0, 1),
                List.of(1, 2),
                List.of(2, 0),
                List.of(1, 3),
                List.of(3, 4)
        );

        List<String> expectedBridges = List.of("1-3", "3-4");
        List<String> actualBridges = graphService.findBridges(vertices, edges);

        assertEquals(expectedBridges.size(), actualBridges.size());
        assertTrue(expectedBridges.containsAll(actualBridges));
    }

    @Test
    public void testFindArticulationPoints_WithSampleGraph_ReturnsCorrectPoints() {
        int vertices = 5;
        List<List<Integer>> edges = List.of(
                List.of(0, 1),
                List.of(1, 2),
                List.of(2, 0),
                List.of(1, 3),
                List.of(3, 4)
        );

        List<Integer> expectedPoints = List.of(1, 3);
        List<Integer> actualPoints = graphService.findArticulationPoints(vertices, edges);

        assertEquals(expectedPoints.size(), actualPoints.size());
        assertTrue(expectedPoints.containsAll(actualPoints));
    }

    @Test
    public void testFindBridges_WithNoEdges_ReturnsEmptyList() {
        int vertices = 3;
        List<List<Integer>> edges = List.of();
        List<String> actualBridges = graphService.findBridges(vertices, edges);
        assertEquals(0, actualBridges.size());
    }

    @Test
    public void testFindArticulationPoints_WithNoEdges_ReturnsEmptyList() {
        int vertices = 3;
        List<List<Integer>> edges = List.of();
        List<Integer> actualPoints = graphService.findArticulationPoints(vertices, edges);
        assertEquals(0, actualPoints.size());
    }
}
