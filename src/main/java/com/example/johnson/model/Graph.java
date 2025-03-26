package com.example.johnson.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a directed graph with weighted edges.
 */
public class Graph {
    private final List<String> vertices;
    private final Map<String, Map<String, Integer>> edges;

    public Graph(List<String> vertices) {
        this.vertices = vertices;
        this.edges = new HashMap<>();
        for (String v : vertices) {
            edges.put(v, new HashMap<>());
        }
    }

    public void addEdge(String from, String to, int weight) {
        edges.get(from).put(to, weight);
    }

    public List<String> getVertices() {
        return vertices;
    }

    public Map<String, Map<String, Integer>> getEdges() {
        return edges;
    }
}
