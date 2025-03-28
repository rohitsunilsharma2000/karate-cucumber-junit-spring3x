package com.example.graph.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Custom constraint annotation for validating a {@link com.example.graph.dto.GraphRequest}.
 *
 * <p><strong>Purpose:</strong></p>
 * Ensures that:
 * <ul>
 *   <li>Each edge in the graph contains exactly two vertices.</li>
 *   <li>Each vertex index is non-null and within the range [0, vertices - 1].</li>
 * </ul>
 *
 * <p><strong>How it works:</strong></p>
 * This annotation delegates validation to the {@link GraphRequestValidator} class,
 * which implements the logic for checking structure and range of vertex indices.
 *
 * <p><strong>Usage:</strong></p>
 * <pre>
 * {@code
 * @ValidGraphRequest
 * public class GraphRequest {
 *     ...
 * }
 * }
 * </pre>
 *
 * <p><strong>Target:</strong> Classes (i.e., DTOs)</p>
 * <p><strong>Retention:</strong> Runtime, required for runtime validation.</p>
 *
 * @see com.example.graph.validation.GraphRequestValidator
 * @see jakarta.validation.ConstraintValidator
 */
@Documented
@Constraint(validatedBy = GraphRequestValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidGraphRequest {

    /**
     * Default error message if validation fails.
     */
    String message() default "Invalid graph request: edge vertices must be within range and non-negative.";

    /**
     * Allows specification of validation groups.
     */
    Class<?>[] groups() default {};

    /**
     * Payloads can be used by clients to assign custom payload objects to a constraint.
     */
    Class<? extends Payload>[] payload() default {};
}
