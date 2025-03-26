package com.example.graph.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link GraphRequest} DTO to validate constraint annotations.
 *
 * <p><strong>Purpose:</strong> Ensure that input validation constraints such as @Min, @NotNull, and @NotEmpty
 * are properly enforced before hitting service or controller logic.</p>
 */
public class GraphRequestTest {

    private static Validator validator;

    @BeforeAll
    public static void setupValidatorInstance() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * Test to validate a fully correct GraphRequest instance.
     *
     * <p><strong>Expectation:</strong> No constraint violations should occur.</p>
     */
    @Test
    public void testValidGraphRequest_NoViolations() {
        GraphRequest request = new GraphRequest();
        request.setVertices(3);
        request.setEdges(List.of(List.of(0, 1), List.of(1, 2)));

        Set<ConstraintViolation<GraphRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty(), "There should be no validation errors for a valid input");
    }

    /**
     * Test to validate a GraphRequest with vertices set to zero.
     *
     * <p><strong>Expectation:</strong> One violation due to @Min constraint on vertices.</p>
     */
    @Test
    public void testInvalidGraphRequest_ZeroVertices_ShouldFailMinValidation() {
        GraphRequest request = new GraphRequest();
        request.setVertices(0);
        request.setEdges(List.of(List.of(0, 1)));

        Set<ConstraintViolation<GraphRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty(), "Validation should fail for vertices < 1");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("vertices")));
    }

    /**
     * Test to validate a GraphRequest with null edges list.
     *
     * <p><strong>Expectation:</strong> One violation due to @NotNull on edges field.</p>
     */
    @Test
    public void testInvalidGraphRequest_NullEdges_ShouldFailNotNullValidation() {
        GraphRequest request = new GraphRequest();
        request.setVertices(3);
        request.setEdges(null);

        Set<ConstraintViolation<GraphRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty(), "Validation should fail for null edges list");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("edges")));
    }

    /**
     * Test to validate a GraphRequest with an empty list of edges.
     *
     * <p><strong>Expectation:</strong> One violation due to @NotEmpty on edges field.</p>
     */
    @Test
    public void testInvalidGraphRequest_EmptyEdgesList_ShouldFailNotEmptyValidation() {
        GraphRequest request = new GraphRequest();
        request.setVertices(3);
        request.setEdges(new ArrayList<>());

        Set<ConstraintViolation<GraphRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty(), "Validation should fail for empty edges list");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("edges")));
    }

    /**
     * Test to validate a GraphRequest with null vertex inside an edge.
     *
     * <p><strong>Expectation:</strong> One violation due to @NotNull on inner list elements.</p>
     */
    @Test
    public void testInvalidGraphRequest_NullVertexInsideEdge_ShouldFailElementValidation() {
        GraphRequest request = new GraphRequest();
        request.setVertices(3);
        request.setEdges(List.of(List.of(0, null)));

        Set<ConstraintViolation<GraphRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty(), "Validation should fail for null vertex in edge list");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().contains("edges")));
    }
}

