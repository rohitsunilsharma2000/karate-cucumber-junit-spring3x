package com.example.graph.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class GraphServiceTest {

    private final GraphService graphService = new GraphService();

    @Test
    public void testFindBridges_WithValidGraph_ReturnsCorrectBridges() {
        int vertices = 5;
        List<List<Integer>> edges = List.of(
                List.of(0, 1),
                List.of(1, 2),
                List.of(2, 0),
                List.of(1, 3),
                List.of(3, 4)
        );

        List<String> bridges = graphService.findBridges(vertices, edges);
        assertEquals(2, bridges.size());
        assertTrue(bridges.contains("1-3") || bridges.contains("3-1"));
        assertTrue(bridges.contains("3-4") || bridges.contains("4-3"));
    }

    @Test
    public void testFindArticulationPoints_WithValidGraph_ReturnsCorrectPoints() {
        int vertices = 5;
        List<List<Integer>> edges = List.of(
                List.of(0, 1),
                List.of(1, 2),
                List.of(2, 0),
                List.of(1, 3),
                List.of(3, 4)
        );

        List<Integer> points = graphService.findArticulationPoints(vertices, edges);
        assertEquals(2, points.size());
        assertTrue(points.contains(1));
        assertTrue(points.contains(3));
    }

    @Test
    public void testFindBridges_WithNoBridgeGraph_ReturnsEmptyList() {
        int vertices = 3;
        List<List<Integer>> edges = List.of(
                List.of(0, 1),
                List.of(1, 2),
                List.of(2, 0)
        );

        List<String> bridges = graphService.findBridges(vertices, edges);
        assertTrue(bridges.isEmpty());
    }

    @Test
    public void testFindArticulationPoints_WithNoArticulationPointGraph_ReturnsEmptyList() {
        int vertices = 3;
        List<List<Integer>> edges = List.of(
                List.of(0, 1),
                List.of(1, 2),
                List.of(2, 0)
        );

        List<Integer> points = graphService.findArticulationPoints(vertices, edges);
        assertTrue(points.isEmpty());
    }
}
