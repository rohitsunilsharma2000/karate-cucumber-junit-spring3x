package com.example.graph.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for analyzing graph integrity by detecting critical elements like
 * bridges and articulation points in an undirected graph.
 *
 * <p>
 * This service implements Depth First Search (DFS)-based algorithms for:
 * <ul>
 *   <li><strong>Bridge Detection:</strong> Identifying edges which, if removed, would increase the number of connected components.</li>
 *   <li><strong>Articulation Point Detection:</strong> Identifying vertices which, if removed, would increase the number of connected components.</li>
 * </ul>
 * </p>
 *
 * <p>
 * It supports the analysis of any undirected graph represented by an edge list and a number of vertices.
 * </p>
 *
 * <p><strong>Logging:</strong> SLF4J is used to log method executions, DFS traversal details, and outcomes at DEBUG and INFO levels.</p>
 *
 * @author
 * @since 2025-03-26
 */
@Service
public class GraphService {
    private static final Logger logger = LoggerFactory.getLogger(GraphService.class);
    private int time;

    /**
     * Detects all bridges in an undirected graph.
     *
     * @param vertices the number of vertices in the graph (indexed from 0 to vertices - 1)
     * @param edges    a list of undirected edges, where each edge is a list of two integers
     * @return a list of string representations of bridge edges (e.g., "1-3")
     */
    public List<String> findBridges(int vertices, List<List<Integer>> edges) {
        logger.debug("Initializing bridge detection with {} vertices and edges: {}", vertices, edges);
        time = 0;

        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < vertices; i++) adj.add(new ArrayList<>());

        for (List<Integer> edge : edges) {
            adj.get(edge.get(0)).add(edge.get(1));
            adj.get(edge.get(1)).add(edge.get(0));
        }
        logger.debug("Adjacency list created: {}", adj);

        boolean[] visited = new boolean[vertices];
        int[] disc = new int[vertices];
        int[] low = new int[vertices];
        List<List<Integer>> bridges = new ArrayList<>();

        for (int i = 0; i < vertices; i++) {
            if (!visited[i]) {
                logger.debug("Starting DFS for bridge detection at vertex {}", i);
                dfsBridge(i, -1, visited, disc, low, bridges, adj);
            }
        }

        List<String> result = bridges.stream().map(b -> b.get(0) + "-" + b.get(1)).toList();
        logger.info("Total bridges found: {}", result.size());
        return result;
    }

    /**
     * DFS utility for bridge detection.
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
                logger.debug("After visiting {}: updated low[{}] = {}", v, u, low[u]);
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
     * Detects all articulation points in an undirected graph.
     *
     * @param vertices the number of vertices in the graph
     * @param edges    the list of undirected edges
     * @return a list of articulation point vertex indices
     */
    public List<Integer> findArticulationPoints(int vertices, List<List<Integer>> edges) {
        logger.debug("Initializing articulation point detection with {} vertices and edges: {}", vertices, edges);
        time = 0;

        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < vertices; i++) adj.add(new ArrayList<>());

        for (List<Integer> edge : edges) {
            adj.get(edge.get(0)).add(edge.get(1));
            adj.get(edge.get(1)).add(edge.get(0));
        }
        logger.debug("Adjacency list created: {}", adj);

        boolean[] visited = new boolean[vertices];
        int[] disc = new int[vertices];
        int[] low = new int[vertices];
        boolean[] ap = new boolean[vertices];
        int[] parent = new int[vertices];
        int[] children = new int[vertices];

        for (int i = 0; i < vertices; i++) {
            parent[i] = -1;
            if (!visited[i]) {
                logger.debug("Starting DFS for articulation point detection at vertex {}", i);
                dfsAP(i, visited, disc, low, ap, parent, children, adj);
            }
        }

        List<Integer> articulationPoints = new ArrayList<>();
        for (int i = 0; i < vertices; i++) {
            if (ap[i]) articulationPoints.add(i);
        }

        logger.info("Total articulation points found: {}", articulationPoints.size());
        return articulationPoints;
    }

    /**
     * DFS utility for articulation point detection.
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
                logger.debug("After visiting {}: updated low[{}] = {}", v, u, low[u]);

                if ((parent[u] == -1 && children[u] > 1) ||
                        (parent[u] != -1 && low[v] >= disc[u])) {
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
