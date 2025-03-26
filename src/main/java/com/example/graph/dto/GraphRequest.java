package com.example.graph.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class GraphRequest {
    @Min(value = 1, message = "Number of vertices must be at least 1")
    private int vertices;

    @NotNull(message = "Edges list cannot be null")
    @NotEmpty(message = "Edges list cannot be empty")
    private List<List<@NotNull(message = "Edge vertex cannot be null") Integer>> edges;

    public int getVertices() {
        return vertices;
    }

    public void setVertices(int vertices) {
        this.vertices = vertices;
    }

    public List<List<Integer>> getEdges() {
        return edges;
    }

    public void setEdges(List<List<Integer>> edges) {
        this.edges = edges;
    }
}

