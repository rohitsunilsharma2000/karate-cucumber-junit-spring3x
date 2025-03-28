package com.example.graph.validation;

import com.example.graph.dto.GraphRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Custom validator for {@link GraphRequest}.
 *
 * <p>
 * Ensures that:
 * <ul>
 *   <li>The number of vertices is ≥ 1 (already enforced via annotation).</li>
 *   <li>Each edge is a non-null list of exactly two integers.</li>
 *   <li>Each vertex index in an edge is within the valid range: [0, vertices - 1].</li>
 * </ul>
 * </p>
 */
public class GraphRequestValidator implements ConstraintValidator<ValidGraphRequest, GraphRequest> {

    private static final Logger logger = LoggerFactory.getLogger(GraphRequestValidator.class);

    @Override
    public boolean isValid(GraphRequest request, ConstraintValidatorContext context) {
        // Null request — let other validators handle it
        if (request == null) {
            logger.warn("GraphRequest is null. Skipping custom validation.");
            return true;
        }

        logger.debug("Starting validation for GraphRequest: vertices={}, edges={}",
                     request.getVertices(), request.getEdges());

        int vertices = request.getVertices();
        List<List<Integer>> edges = request.getEdges();

        // Ensure edge list is not null or empty
        if (edges == null || edges.isEmpty()) {
            logger.error("Validation failed: Edges list is null or empty.");
            return false;
        }

        int edgeIndex = 0;
        for (List<Integer> edge : edges) {
            logger.debug("Validating edge[{}]: {}", edgeIndex, edge);

            // Check if the edge itself is null
            if (edge == null) {
                logger.error("Validation failed: Edge[{}] is null.", edgeIndex);
                return false;
            }

            // Each edge must contain exactly 2 vertices
            if (edge.size() != 2) {
                logger.error("Validation failed: Edge[{}] does not contain exactly two vertices.", edgeIndex);
                return false;
            }

            int vertexIndex = 0;
            for (Integer vertex : edge) {
                if (vertex == null) {
                    logger.error("Validation failed: Vertex in edge[{}][{}] is null.", edgeIndex, vertexIndex);
                    return false;
                }
                if (vertex < 0 || vertex >= vertices) {
                    logger.error("Validation failed: Vertex in edge[{}][{}] = {} is out of bounds (0 to {}).",
                                 edgeIndex, vertexIndex, vertex, vertices - 1);
                    return false;
                }
                logger.debug("Vertex[{}] in edge[{}] is valid: {}", vertexIndex, edgeIndex, vertex);
                vertexIndex++;
            }

            edgeIndex++;
        }

        logger.info("GraphRequest validation passed.");
        return true;
    }
}
