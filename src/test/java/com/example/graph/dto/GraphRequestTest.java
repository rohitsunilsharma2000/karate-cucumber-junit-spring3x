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
 * Test class for {@link GraphRequest}.
 *
 * <p>
 * This class contains unit tests to verify that the validation annotations applied to
 * the GraphRequest DTO work as expected using Jakarta Bean Validation.
 * </p>
 *
 * <p>
 * <strong>Validation Rules Tested:</strong>
 * <ul>
 *   <li>{@code @Min(1)} on {@code vertices}</li>
 *   <li>{@code @NotNull} on {@code edges} and inner vertex values</li>
 *   <li>{@code @NotEmpty} on {@code edges}</li>
 * </ul>
 * </p>
 *
 * @version 1.0
 * @since 2025-03-26
 */
public class GraphRequestTest {

    /**
     * A shared Validator instance for performing constraint validation.
     */
    private static Validator validator;

    /**
     * Sets up the validator instance before all test cases run.
     * This uses the Jakarta ValidatorFactory to build a validator.
     */
    @BeforeAll
    public static void setupValidatorInstance() {
        // Build the default ValidatorFactory and retrieve the Validator instance.
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * Tests a valid {@link GraphRequest} with 3 vertices and valid edges.
     *
     * <p>
     * <strong>Expectation:</strong> No constraint violations should occur.
     * </p>
     */
    @Test
    public void testValidGraphRequest_NoViolations() {
        // GIVEN: A valid GraphRequest with vertices set to 3 and a non-empty list of edges.
        GraphRequest request = new GraphRequest();
        request.setVertices(3);
        request.setEdges(List.of(List.of(0, 1), List.of(1, 2)));

        // WHEN: Validate the request using the shared validator instance.
        Set<ConstraintViolation<GraphRequest>> violations = validator.validate(request);

        // THEN: Assert that there are no constraint violations.
        assertTrue(violations.isEmpty(), "There should be no validation errors for a valid input");
    }

    /**
     * Tests {@code vertices = 0}, which violates {@code @Min(1)}.
     *
     * <p>
     * <strong>Expectation:</strong> Validation should fail on the 'vertices' field.
     * </p>
     */
    @Test
    public void testInvalidGraphRequest_ZeroVertices_ShouldFailMinValidation() {
        // GIVEN: A GraphRequest with vertices set to 0 (invalid) and a valid edges list.
        GraphRequest request = new GraphRequest();
        request.setVertices(0);
        request.setEdges(List.of(List.of(0, 1)));

        // WHEN: Validate the request.
        Set<ConstraintViolation<GraphRequest>> violations = validator.validate(request);

        // THEN: Assert that validation fails and a violation is reported on the 'vertices' field.
        assertFalse(violations.isEmpty(), "Validation should fail for vertices < 1");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("vertices")),
                   "Expected violation on the 'vertices' field");
    }

    /**
     * Tests with {@code edges = null}, violating {@code @NotNull}.
     *
     * <p>
     * <strong>Expectation:</strong> Validation should fail on the 'edges' field.
     * </p>
     */
    @Test
    public void testInvalidGraphRequest_NullEdges_ShouldFailNotNullValidation() {
        // GIVEN: A GraphRequest with a valid vertices value but with edges set to null.
        GraphRequest request = new GraphRequest();
        request.setVertices(3);
        request.setEdges(null);

        // WHEN: Validate the request.
        Set<ConstraintViolation<GraphRequest>> violations = validator.validate(request);

        // THEN: Assert that validation fails and a violation is reported on the 'edges' field.
        assertFalse(violations.isEmpty(), "Validation should fail for null edges list");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("edges")),
                   "Expected violation on the 'edges' field");
    }

    /**
     * Tests with an empty list of edges, violating {@code @NotEmpty}.
     *
     * <p>
     * <strong>Expectation:</strong> Validation should fail on the 'edges' field.
     * </p>
     */
    @Test
    public void testInvalidGraphRequest_EmptyEdgesList_ShouldFailNotEmptyValidation() {
        // GIVEN: A GraphRequest with vertices set to a valid value but with an empty edges list.
        GraphRequest request = new GraphRequest();
        request.setVertices(3);
        request.setEdges(new ArrayList<>()); // Empty list

        // WHEN: Validate the request.
        Set<ConstraintViolation<GraphRequest>> violations = validator.validate(request);

        // THEN: Assert that validation fails and a violation is reported on the 'edges' field.
        assertFalse(violations.isEmpty(), "Validation should fail for empty edges list");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("edges")),
                   "Expected violation on the 'edges' field");
    }
}
