package com.example.johnson.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Data Transfer Object representing the input graph data.
 * <p>
 * Contains a list of vertices and a list of edges that define the graph.
 * The vertices list must not be empty. Each edge must be valid with non-null endpoints.
 * </p>
 */
public class GraphRequestDTO {

    @NotEmpty(message = "Vertices list must not be empty")
    private List<@NotNull(message = "Vertex identifier must not be null") String> vertices;

    @NotEmpty(message = "Edges list must not be empty")
    @Valid
    private List<@NotNull(message = "Edge must not be null") EdgeDTO> edges;

    public List<String> getVertices() {
        return vertices;
    }

    public void setVertices(List<String> vertices) {
        this.vertices = vertices;
    }

    public List<EdgeDTO> getEdges() {
        return edges;
    }

    public void setEdges(List<EdgeDTO> edges) {
        this.edges = edges;
    }

    /**
     * Nested DTO representing an edge between two vertices in the graph.
     */
    public static class EdgeDTO {

        @NotEmpty(message = "Source vertex must not be empty")
        private String from;

        @NotEmpty(message = "Destination vertex must not be empty")
        private String to;

        // Note: Depending on the algorithm, negative weights may be allowed.
        private int weight;

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }
    }
}
