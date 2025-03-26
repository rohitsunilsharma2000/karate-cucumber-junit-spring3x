package com.example.johnson.model;

import java.util.Objects;

/**
 * Helper class to represent a node in the priority queue.
 */
public class Node implements Comparable<Node> {
    private final String vertex;
    private final int distance;

    public Node(String vertex, int distance) {
        this.vertex = vertex;
        this.distance = distance;
    }

    public String getVertex() {
        return vertex;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public int compareTo(Node other) {
        return Integer.compare(this.distance, other.distance);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node node)) return false;
        return Objects.equals(vertex, node.vertex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vertex);
    }
}
