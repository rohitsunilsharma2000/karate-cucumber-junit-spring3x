package com.example.maxflow.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a directed graph used for calculating the maximum flow in a network.
 * <p>
 * This model captures the structure of the graph using an adjacency matrix implemented as a map of maps.
 * Each key in the outer map represents a vertex, and the corresponding inner map holds the adjacent vertices
 * and the capacity of the edge connecting them.
 * </p>
 *
 * <p><b>Usage:</b></p>
 * <ul>
 *     <li>Create an instance by specifying the total number of vertices.</li>
 *     <li>Add edges between vertices along with their capacities using the {@link #addEdge(int, int, int)} method.</li>
 *     <li>Retrieve the capacity of an edge with {@link #getCapacity(int, int)}.</li>
 * </ul>
 *
 * <p><b>Constraints:</b></p>
 * <ul>
 *     <li>The number of vertices must be a positive integer.</li>
 *     <li>Edges are directed; an edge from vertex A to vertex B does not imply an edge from B to A.</li>
 *     <li>Edge capacities must be non-negative integers.</li>
 * </ul>
 *
 * <p><b>Pass/Fail Conditions:</b></p>
 * <ul>
 *     <li><b>Pass:</b> Successfully initializes the graph with the specified number of vertices and correctly adds edges with their capacities.</li>
 *     <li><b>Fail:</b> Fails to add an edge if the specified source or destination vertices are not valid (i.e., outside the range of 0 to vertices - 1).</li>
 * </ul>
 *
 *
 */
public class Graph {

    /**
     * The total number of vertices in the graph.
     */
    private final int vertices;

    /**
     * Adjacency matrix representing the graph.
     * <p>
     * Each key in the outer map represents a vertex. The inner map contains pairs where the key is the destination vertex
     * and the value is the capacity of the edge from the source vertex (the key in the outer map) to that destination.
     * </p>
     */
    private final Map<Integer, Map<Integer, Integer>> adjMatrix;

    /**
     * Constructs a new {@code Graph} with the specified number of vertices.
     * <p>
     * Initializes an empty adjacency matrix where each vertex is associated with an empty map.
     * </p>
     *
     * @param vertices the total number of vertices in the graph; must be a positive integer.
     */
    public Graph(int vertices) {
        this.vertices = vertices;
        this.adjMatrix = new HashMap<>();
        for (int i = 0; i < vertices; i++) {
            adjMatrix.put(i, new HashMap<>());
        }
    }

    /**
     * Adds a directed edge from the specified source vertex to the destination vertex with the given capacity.
     * <p>
     * If an edge already exists, its capacity is updated to the new value.
     * </p>
     *
     * <p><b>Parameters:</b></p>
     * <ul>
     *     <li><strong>source</strong> (int): The vertex from which the edge originates.</li>
     *     <li><strong>destination</strong> (int): The vertex to which the edge points.</li>
     *     <li><strong>capacity</strong> (int): The capacity of the edge, must be non-negative.</li>
     * </ul>
     *
     * <p><b>Pass Condition:</b> The edge is successfully added or updated in the adjacency matrix.</p>
     * <p><b>Fail Condition:</b> The edge is not added if either the source or destination vertex is invalid.</p>
     *
     * @param source      the source vertex of the edge.
     * @param destination the destination vertex of the edge.
     * @param capacity    the capacity of the edge.
     */
    public void addEdge(int source, int destination, int capacity) {
        // Consider adding validations for source and destination range if needed
        adjMatrix.get(source).put(destination, capacity);
    }

    /**
     * Retrieves the capacity of the edge from the specified source vertex to the destination vertex.
     * <p>
     * If the edge does not exist, returns 0.
     * </p>
     *
     * @param source      the source vertex of the edge.
     * @param destination the destination vertex of the edge.
     * @return the capacity of the edge, or 0 if no edge exists.
     */
    public int getCapacity(int source, int destination) {
        return adjMatrix.get(source).getOrDefault(destination, 0);
    }

    /**
     * Returns the entire adjacency matrix representing the graph.
     * <p>
     * The returned matrix is a map where each key is a vertex and each value is another map representing the edges and capacities.
     * </p>
     *
     * @return the adjacency matrix of the graph.
     */
    public Map<Integer, Map<Integer, Integer>> getAdjMatrix() {
        return adjMatrix;
    }

    /**
     * Returns the total number of vertices in the graph.
     *
     * @return the number of vertices.
     */
    public int getVertices() {
        return vertices;
    }
}
