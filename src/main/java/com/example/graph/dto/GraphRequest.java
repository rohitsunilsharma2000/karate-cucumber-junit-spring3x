package com.example.graph.dto;

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
 * bridges and articulation points in an undirected graph.
 * It includes validation constraints to ensure data integrity.
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
 *   <li><code>@NotNull</code> and <code>@NotEmpty</code>: Ensure the edges list is non-null and not empty.</li>
 *   <li>Each edge vertex must also be non-null.</li>
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
 *   <li><strong>Fail:</strong> Any field is missing, null, or does not satisfy validation constraints.</li>
 * </ul>
 *
 * @author
 * @since 2025-03-26
 */
@Getter
@Setter
public class GraphRequest {

    /**
     * The number of vertices in the graph.
     * Must be greater than or equal to 1.
     */
    @Min(value = 1, message = "Number of vertices must be at least 1")
    private int vertices;

    /**
     * The list of undirected edges.
     * Each edge is represented as a list of two integers [u, v].
     * Cannot be null or empty.
     */
    @NotNull(message = "Edges list cannot be null")
    @NotEmpty(message = "Edges list cannot be empty")
    private List<List<@NotNull(message = "Edge vertex cannot be null") Integer>> edges;

}
