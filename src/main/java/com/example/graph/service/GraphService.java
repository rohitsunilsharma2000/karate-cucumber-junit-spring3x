package com.example.graph.service;

import com.example.graph.exception.GraphAnalysisException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for analyzing graph structures by detecting bridges and articulation points
 * in an undirected graph using DFS-based algorithms.
 *
 * <p>
 * Premise: The graph is undirected, with nodes labeled 0 to vertices - 1. Input is expected
 * to be validated before invoking service methods.
 * </p>
 */
@Service
public class GraphService {

    private static final Logger logger = LoggerFactory.getLogger(GraphService.class);
    private int time; // Global counter for discovery time

    /**
     * Detects all bridges (critical edges) in an undirected graph.
     *
     * <p><strong>Premise:</strong></p>
     * Graph is connected or disconnected. DFS will traverse all components.
     *
     * <p><strong>Algorithm:</strong></p>
     * Tarjan's Bridge-Finding Algorithm using DFS timestamps.
     *
     * <p><strong>Time Complexity:</strong> O(V + E)</p>
     * <p><strong>Space Complexity:</strong> O(V + E)</p>
     *
     * @param vertices number of vertices (0-indexed)
     * @param edges    list of undirected edges, each represented by a list of two integers
     * @return list of bridges formatted as strings (e.g., "1-3")
     *
     * <p><strong>Pass:</strong> If input is valid and bridges are correctly detected.</p>
     * <p><strong>Fail:</strong> If internal exceptions occur or invalid edges crash the logic.</p>
     */
    public List<String> findBridges(int vertices, List<List<Integer>> edges) {
        try {
            logger.debug("Initializing bridge detection with {} vertices and edges: {}", vertices, edges);
            time = 0;

            // Create adjacency list
            List<List<Integer>> adj = new ArrayList<>();
            for (int i = 0; i < vertices; i++) adj.add(new ArrayList<>());

            // Build undirected graph
            for (List<Integer> edge : edges) {
                adj.get(edge.get(0)).add(edge.get(1));
                adj.get(edge.get(1)).add(edge.get(0));
            }
            logger.info("Adjacency list built successfully.");
            boolean[] visited = new boolean[vertices];
            int[] disc = new int[vertices]; // Discovery times
            int[] low = new int[vertices];  // Lowest reachable vertex from subtree
            List<List<Integer>> bridges = new ArrayList<>();

            // Run DFS from unvisited nodes (handles disconnected graphs)
            for (int i = 0; i < vertices; i++) {
                if (!visited[i]) {
                    logger.debug("Starting DFS from vertex {}", i);
                    dfsBridge(i, -1, visited, disc, low, bridges, adj);
                }
            }

            List<String> result = bridges.stream().map(b -> b.get(0) + "-" + b.get(1)).toList();
            logger.info("Total bridges found: {}", result.size());
            return result;

        } catch (Exception e) {
            logger.error("Error occurred while finding bridges", e);
            throw new GraphAnalysisException("Something went wrong while detecting bridges in the graph.", e);
        }
    }

    /**
     * DFS helper for bridge detection using Tarjan’s algorithm.
     *
     * @param u        current node
     * @param parent   parent node in DFS tree
     * @param visited  visited marker
     * @param disc     discovery time array
     * @param low      low-link values
     * @param bridges  result list of bridges
     * @param adj      adjacency list of graph
     */
    private void dfsBridge(int u, int parent, boolean[] visited, int[] disc, int[] low,
                           List<List<Integer>> bridges, List<List<Integer>> adj) {
        visited[u] = true;
        disc[u] = low[u] = ++time;
        logger.debug("Visiting node {}: disc = {}, low = {}", u, disc[u], low[u]);

        for (int v : adj.get(u)) {
            if (!visited[v]) {
                logger.debug("Tree edge found from {} to {}", u, v);
                dfsBridge(v, u, visited, disc, low, bridges, adj);
                low[u] = Math.min(low[u], low[v]);
                logger.debug("Backtracking to {}: updated low[{}] = {}", u, u, low[u]);

                // A bridge exists if subtree cannot reach an ancestor of u
                if (low[v] > disc[u]) {
                    logger.debug("Bridge found between {} and {}", u, v);
                    bridges.add(List.of(u, v));
                }
            } else if (v != parent) {
                logger.debug("Back edge found from {} to {}", u, v);
                low[u] = Math.min(low[u], disc[v]);
            }
        }
    }

    /**
     * Detects all articulation points (cut vertices) in an undirected graph.
     *
     * <p><strong>Premise:</strong></p>
     * Articulation points are vertices that, if removed, increase the number of connected components.
     *
     * <p><strong>Algorithm:</strong></p>
     * DFS-based approach with Tarjan’s low-link technique.
     *
     * <p><strong>Time Complexity:</strong> O(V + E)</p>
     * <p><strong>Space Complexity:</strong> O(V + E)</p>
     *
     * @param vertices the number of vertices in the graph
     * @param edges    list of undirected edges
     * @return list of articulation point indices
     *
     * <p><strong>Pass:</strong> If valid articulation points are returned.</p>
     * <p><strong>Fail:</strong> If any error occurs or invalid edge indices are provided.</p>
     */
    public List<Integer> findArticulationPoints(int vertices, List<List<Integer>> edges) {
        try {
            logger.debug("Initializing articulation point detection with {} vertices and edges: {}", vertices, edges);
            time = 0;

            List<List<Integer>> adj = new ArrayList<>();
            for (int i = 0; i < vertices; i++) adj.add(new ArrayList<>());

            for (List<Integer> edge : edges) {
                adj.get(edge.get(0)).add(edge.get(1));
                adj.get(edge.get(1)).add(edge.get(0));
            }

            logger.info("Adjacency list built successfully for articulation point detection.");

            boolean[] visited = new boolean[vertices];
            int[] disc = new int[vertices];
            int[] low = new int[vertices];
            boolean[] ap = new boolean[vertices]; // Flags for articulation points
            int[] parent = new int[vertices];
            int[] children = new int[vertices];

            // Initialize parent values and start DFS
            for (int i = 0; i < vertices; i++) {
                parent[i] = -1;
                if (!visited[i]) {
                    logger.debug("Starting DFS at articulation detection from node {}", i);
                    dfsAP(i, visited, disc, low, ap, parent, children, adj);
                }
            }

            // Collect articulation points from flags
            List<Integer> articulationPoints = new ArrayList<>();
            for (int i = 0; i < vertices; i++) {
                if (ap[i]) articulationPoints.add(i);
            }

            logger.info("Total articulation points found: {}", articulationPoints.size());
            return articulationPoints;

        } catch (Exception e) {
            logger.error("Error occurred while finding articulation points", e);
            throw new GraphAnalysisException("Something went wrong while detecting articulation points in the graph.", e);
        }
    }

    /**
     * DFS helper to detect articulation points.
     *
     * @param u        current vertex
     * @param visited  array to track visited nodes
     * @param disc     discovery times
     * @param low      low values
     * @param ap       articulation point flags
     * @param parent   parent array
     * @param children children count for root node
     * @param adj      adjacency list
     */
    private void dfsAP(int u, boolean[] visited, int[] disc, int[] low, boolean[] ap,
                       int[] parent, int[] children, List<List<Integer>> adj) {
        visited[u] = true;
        disc[u] = low[u] = ++time;
        logger.debug("Visiting node {}: disc = {}, low = {}", u, disc[u], low[u]);

        for (int v : adj.get(u)) {
            if (!visited[v]) {
                logger.debug("Tree edge found from {} to {}", u, v);
                parent[v] = u;
                children[u]++;
                dfsAP(v, visited, disc, low, ap, parent, children, adj);
                low[u] = Math.min(low[u], low[v]);

                // Case 1: u is root and has 2+ children
                if (parent[u] == -1 && children[u] > 1) {
                    logger.debug("Articulation point found at root {}", u);
                    ap[u] = true;
                }

                // Case 2: u is not root and child v can't reach an ancestor of u
                if (parent[u] != -1 && low[v] >= disc[u]) {
                    logger.debug("Articulation point found at {}", u);
                    ap[u] = true;
                }

            } else if (v != parent[u]) {
                logger.debug("Back edge found from {} to {}", u, v);
                low[u] = Math.min(low[u], disc[v]);
            }
        }
    }
}
