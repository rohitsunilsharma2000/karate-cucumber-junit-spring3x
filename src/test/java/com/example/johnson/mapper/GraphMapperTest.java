package com.example.johnson.mapper;

import com.example.johnson.dto.GraphRequestDTO;
import com.example.johnson.dto.GraphRequestDTO.EdgeDTO;
import com.example.johnson.model.Graph;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test class for {@link GraphMapper}.
 *
 * <p><strong>Overview:</strong></p>
 * This test verifies the behavior of the {@code GraphMapper.toDomain} method that converts
 * a {@link GraphRequestDTO} (DTO from client input) into an internal {@link Graph} model.
 *
 * <p><strong>Functionality Tested:</strong></p>
 * <ul>
 *   <li>Conversion of vertices from DTO to model.</li>
 *   <li>Conversion of edges and edge weights.</li>
 *   <li>Handling of empty or null edge lists.</li>
 * </ul>
 *
 * <p><strong>Pass/Fail Conditions:</strong></p>
 * <ul>
 *   <li><strong>Pass:</strong> Vertices and edges in the domain model exactly match the DTO input.</li>
 *   <li><strong>Fail:</strong> Any missing or incorrectly mapped data in the resulting {@link Graph} model.</li>
 * </ul>
 *
 * @version 1.0
 * @since 2025-03-26
 */
public class GraphMapperTest {

    /**
     * Test 1: Converts a valid DTO to a graph domain model correctly.
     *
     * <p><strong>Scenario:</strong> A DTO with 3 vertices and 2 edges is mapped to a Graph model.</p>
     * <ul>
     *   <li>Vertex list: ["A", "B", "C"]</li>
     *   <li>Edges: A → B (3), B → C (5)</li>
     * </ul>
     *
     * <p><strong>Assertions:</strong></p>
     * <ul>
     *   <li>All vertices must be present.</li>
     *   <li>Edges with correct weights must exist in the adjacency map.</li>
     * </ul>
     */
    @Test
    @DisplayName("Test 1: Valid DTO mapped to Graph model")
    void testToDomain_ValidInput() {
        GraphRequestDTO dto = new GraphRequestDTO();
        dto.setVertices(List.of("A", "B", "C"));

        List<EdgeDTO> edges = new ArrayList<>();
        edges.add(edge("A", "B", 3));
        edges.add(edge("B", "C", 5));
        dto.setEdges(edges);

        Graph graph = GraphMapper.toDomain(dto);

        assertEquals(3, graph.getVertices().size(), "Vertex count should be 3");
        assertTrue(graph.getEdges().containsKey("A"));
        assertEquals(3, graph.getEdges().get("A").get("B"));
        assertEquals(5, graph.getEdges().get("B").get("C"));
    }

    /**
     * Test 2: Handles an empty edge list (no edges).
     *
     * <p><strong>Scenario:</strong> A graph with 2 vertices but no connecting edges.</p>
     * <p><strong>Expected:</strong> Graph should contain the vertices and empty edge maps.</p>
     */
    @Test
    @DisplayName("Test 2: Empty edge list is handled")
    void testToDomain_EmptyEdges() {
        GraphRequestDTO dto = new GraphRequestDTO();
        dto.setVertices(List.of("X", "Y"));
        dto.setEdges(new ArrayList<>());

        Graph graph = GraphMapper.toDomain(dto);

        assertEquals(2, graph.getVertices().size());
        assertTrue(graph.getEdges().get("X").isEmpty());
        assertTrue(graph.getEdges().get("Y").isEmpty());
    }

    /**
     * Test 3: Edge list contains multiple directed connections.
     *
     * <p><strong>Scenario:</strong> A → B (2), B → A (4), B → C (6)</p>
     * <p><strong>Expected:</strong> Each edge should be properly represented with correct weights.</p>
     */
    @Test
    @DisplayName("Test 3: Bidirectional and multiple edges are correctly mapped")
    void testToDomain_MultipleEdges() {
        GraphRequestDTO dto = new GraphRequestDTO();
        dto.setVertices(List.of("A", "B", "C"));

        dto.setEdges(List.of(
                edge("A", "B", 2),
                edge("B", "A", 4),
                edge("B", "C", 6)
        ));

        Graph graph = GraphMapper.toDomain(dto);

        assertEquals(2, graph.getEdges().get("A").get("B"));
        assertEquals(4, graph.getEdges().get("B").get("A"));
        assertEquals(6, graph.getEdges().get("B").get("C"));
    }

    /**
     * Utility method to create an {@link EdgeDTO}.
     *
     * @param from   Source vertex.
     * @param to     Destination vertex.
     * @param weight Weight of the edge.
     * @return A configured {@code EdgeDTO} instance.
     */
    private EdgeDTO edge(String from, String to, int weight) {
        EdgeDTO e = new EdgeDTO();
        e.setFrom(from);
        e.setTo(to);
        e.setWeight(weight);
        return e;
    }
}
