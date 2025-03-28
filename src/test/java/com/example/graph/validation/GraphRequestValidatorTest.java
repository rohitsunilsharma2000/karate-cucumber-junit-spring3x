package com.example.graph.validation;

import com.example.graph.dto.GraphRequest;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for {@link GraphRequestValidator}.
 *
 * <p><strong>Premise:</strong> The validator enforces that:
 * <ul>
 *     <li>Each edge must contain exactly 2 vertices</li>
 *     <li>Each vertex must be in range [0, vertices - 1]</li>
 *     <li>Vertices must not be null or negative</li>
 * </ul>
 *
 * <p><strong>Assertions:</strong> The validation result matches expected validity.
 *
 * <p><strong>Pass/Fail Conditions:</strong>
 * <ul>
 *     <li><strong>Pass:</strong> All edge lists are valid and vertices are within bounds.</li>
 *     <li><strong>Fail:</strong> If any edge is null, malformed, or references invalid vertex indices.</li>
 * </ul>
 */
class GraphRequestValidatorTest {


    // Create a validator instance and mock context for testing
    private final GraphRequestValidator validator = new GraphRequestValidator();
    private final ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);

    @Test
    @DisplayName("Valid graph with correct edge format and in-range vertices")
    void testValidGraph () {
        // Setup: valid graph with 4 vertices and 2 well-formed edges
        GraphRequest request = new GraphRequest();
        request.setVertices(4);
        request.setEdges(List.of(List.of(0 , 1), List.of(2 , 3)));

        // Expect the validator to return true
        assertTrue(validator.isValid(request, context));
    }

    @Test
    @DisplayName("Edge contains more than two vertices")
    void testEdgeWithTooManyVertices () {
        // Setup: edge has 3 vertices instead of 2
        GraphRequest request = new GraphRequest();
        request.setVertices(4);
        request.setEdges(List.of(List.of(0 , 1 , 2)));

        // Expect the validator to return false
        assertFalse(validator.isValid(request, context));
    }

    @Test
    @DisplayName("Edge contains only one vertex")
    void testEdgeWithOneVertex () {
        // Setup: edge has only 1 vertex
        GraphRequest request = new GraphRequest();
        request.setVertices(4);
        request.setEdges(List.of(List.of(0)));

        // Expect the validator to return false
        assertFalse(validator.isValid(request, context));
    }

    @Test
    @DisplayName("Edge contains null vertex")
    void testEdgeWithNullVertex () {
        // Setup: one vertex in the edge is null
        GraphRequest request = new GraphRequest();
        request.setVertices(3);
        List<List<Integer>> edges = new ArrayList<>();
        edges.add(Arrays.asList(0 , null));
        request.setEdges(edges);

        // Expect the validator to return false
        assertFalse(validator.isValid(request, context));
    }

    @Test
    @DisplayName("Vertex index out of bounds (negative)")
    void testVertexNegative () {
        // Setup: one vertex is negative
        GraphRequest request = new GraphRequest();
        request.setVertices(3);
        request.setEdges(List.of(List.of(-1 , 1)));

        // Expect the validator to return false
        assertFalse(validator.isValid(request, context));
    }

    @Test
    @DisplayName("Vertex index out of bounds (greater than max)")
    void testVertexTooLarge () {
        // Setup: one vertex index exceeds the allowed maximum
        GraphRequest request = new GraphRequest();
        request.setVertices(3);
        request.setEdges(List.of(List.of(0 , 4))); // Max index should be 2

        // Expect the validator to return false
        assertFalse(validator.isValid(request, context));
    }

    @Test
    @DisplayName("Edge list is null")
    void testEdgesListNull () {
        // Setup: edge list itself is null
        GraphRequest request = new GraphRequest();
        request.setVertices(3);
        request.setEdges(null);

        // Expect the validator to return false
        assertFalse(validator.isValid(request, context));
    }

    @Test
    @DisplayName("Edge list is empty")
    void testEdgesListEmpty () {
        // Setup: edge list is empty
        GraphRequest request = new GraphRequest();
        request.setVertices(3);
        request.setEdges(List.of());

        // Expect the validator to return false
        assertFalse(validator.isValid(request, context));
    }

    @Test
    @DisplayName("Edge object is null in list")
    void testEdgeNullInList () {
        // Setup: edge list contains a null edge object
        GraphRequest request = new GraphRequest();
        request.setVertices(3);

        List<List<Integer>> edges = new ArrayList<>();
        edges.add(null);
        request.setEdges(edges);

        // Expect the validator to return false
        assertFalse(validator.isValid(request, context));
    }

    @Test
    @DisplayName("GraphRequest is null")
    void testNullGraphRequest () {
        // Setup: null input request
        // Expect true, as per convention (Bean Validation spec handles nulls as valid unless explicitly disallowed)
        assertTrue(validator.isValid(null, context));
    }
}
