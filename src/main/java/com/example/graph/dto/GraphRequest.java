package com.example.graph.dto;

import com.example.graph.validation.ValidGraphRequest;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Data Transfer Object (DTO) representing the input request for graph operations.
 *
 * <p>
 * This class is used to receive input from clients via REST APIs for detecting
 * bridges and articulation points in an undirected graph. It ensures that
 * the data is well-formed and adheres to expected constraints before being
 * processed by the service layer.
 * </p>
 *
 * <p><strong>Structure:</strong></p>
 * <ul>
 *   <li><code>vertices</code>: Total number of nodes in the graph (must be ≥ 1)</li>
 *   <li><code>edges</code>: List of edges, where each edge is represented as a pair of connected vertices</li>
 * </ul>
 *
 * <p><strong>Validation Constraints:</strong></p>
 * <ul>
 *   <li><code>@Min(1)</code>: Ensures the number of vertices is at least 1.</li>
 *   <li><code>@NotNull</code> and <code>@NotEmpty</code>: Ensure the edges list is not null or empty.</li>
 *   <li><code>@ValidGraphRequest</code>: Ensures that each edge contains exactly 2 valid vertex indices (0 ≤ vertex < vertices).</li>
 * </ul>
 *
 * <p><strong>Usage Example (JSON):</strong></p>
 * <pre>
 * {
 *   "vertices": 5,
 *   "edges": [[0, 1], [1, 2], [2, 0], [1, 3], [3, 4]]
 * }
 * </pre>
 *
 * <p><strong>Pass/Fail Conditions:</strong></p>
 * <ul>
 *   <li><strong>Pass:</strong> All required fields are provided and meet the validation criteria.</li>
 *   <li><strong>Fail:</strong> Any field is missing, null, malformed, or violates a validation rule.</li>
 * </ul>
 *
 * @author
 */
@Getter
@Setter
@ValidGraphRequest // Class-level validation: checks edges have valid size (2) and vertex bounds [0, vertices-1]
public class GraphRequest {

    /**
     * The number of vertices in the graph.
     * Must be greater than or equal to 1.
     */
    @Min(value = 1, message = "Number of vertices must be at least 1")
    private int vertices;

    /**
     * The list of undirected edges in the graph.
     * Each edge is represented as a list of exactly two integers: [u, v].
     *
     * <ul>
     *   <li><code>@NotNull</code>: Edges list must not be null.</li>
     *   <li><code>@NotEmpty</code>: Edges list must contain at least one edge.</li>
     *   <li><code>@NotNull</code> on inner list items: Each vertex index in each edge must be non-null.</li>
     * </ul>
     */
    @NotNull(message = "Edges list cannot be null")
    @NotEmpty(message = "Edges list cannot be empty")
    private List<List<@NotNull(message = "Edge vertex cannot be null") Integer>> edges;

}
