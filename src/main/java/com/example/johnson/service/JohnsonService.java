package com.example.johnson.service;

import com.example.johnson.dto.GraphResponseDTO;
import com.example.johnson.model.Graph;
import com.example.johnson.model.Node;
import com.example.johnson.exception.NegativeCycleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service class that implements Johnson's algorithm for all-pairs shortest paths in sparse graphs.
 *
 * <p><strong>Features:</strong></p>
 * <ul>
 *     <li>Efficient computation of shortest paths using Johnson’s Algorithm.</li>
 *     <li>Reweights graph using Bellman-Ford to handle negative weights (no negative cycles allowed).</li>
 *     <li>Applies Dijkstra’s algorithm from each vertex for final result.</li>
 *     <li>Uses SLF4J logging for traceability.</li>
 *     <li>Throws {@link NegativeCycleException} when graph contains negative-weight cycles.</li>
 * </ul>
 *
 * <p><strong>Input Validation:</strong> Expected to be enforced at Controller/DTO level using validation annotations.</p>
 *
 * <p><strong>Logging Levels:</strong></p>
 * <ul>
 *     <li>INFO – Algorithm phases and completion checkpoints</li>
 *     <li>DEBUG – Intermediate steps, edge reweighting, distances</li>
 *     <li>ERROR – Cycle detection or graph inconsistency</li>
 * </ul>
 */
@Service
public class JohnsonService {

    private static final Logger log = LoggerFactory.getLogger(JohnsonService.class);

    /**
     * Computes all-pairs shortest paths using Johnson's algorithm.
     *
     * @param graph The input graph model with vertices and weighted edges.
     * @return A DTO containing shortest path distances from every vertex to all others.
     * @throws NegativeCycleException If the graph contains a negative-weight cycle.
     */
    public GraphResponseDTO computeAllPairsShortestPaths(Graph graph) {
        log.info("Starting Johnson’s algorithm on graph with {} vertices", graph.getVertices().size());

        List<String> vertices = graph.getVertices();
        Map<String, Map<String, Integer>> edges = graph.getEdges();

        // Add dummy vertex for Bellman-Ford phase
        String dummy = "__DUMMY__";
        Map<String, Map<String, Integer>> augmentedEdges = new HashMap<>();
        for (Map.Entry<String, Map<String, Integer>> entry : edges.entrySet()) {
            augmentedEdges.put(entry.getKey(), new HashMap<>(entry.getValue()));
        }
        augmentedEdges.put(dummy, new HashMap<>());
        for (String v : vertices) {
            augmentedEdges.get(dummy).put(v, 0);
        }

        // Compute h(v) using Bellman-Ford from dummy
        log.info("Running Bellman-Ford to compute vertex potentials h(v)");
        Map<String, Integer> h = bellmanFord(augmentedEdges, dummy);
        if (h == null) {
            log.error("Negative-weight cycle detected – aborting");
            throw new NegativeCycleException("Graph contains negative cycles");
        }

        log.debug("Vertex potentials h(v): {}", h);

        // Reweight all edges: w'(u,v) = w(u,v) + h(u) - h(v)
        log.info("Reweighting all graph edges");
        Map<String, Map<String, Integer>> reweighted = new HashMap<>();
        for (String u : edges.keySet()) {
            reweighted.put(u, new HashMap<>());
            for (String v : edges.get(u).keySet()) {
                int newWeight = edges.get(u).get(v) + h.get(u) - h.get(v);
                reweighted.get(u).put(v, newWeight);
                log.debug("Edge ({}, {}) reweighted from {} to {}", u, v, edges.get(u).get(v), newWeight);
            }
        }

        // Run Dijkstra from each vertex
        log.info("Running Dijkstra’s algorithm from each vertex");
        Map<String, Map<String, Integer>> result = new HashMap<>();
        for (String u : vertices) {
            log.info("Running Dijkstra from '{}'", u);
            Map<String, Integer> dijkstraResult = dijkstra(reweighted, u);
            Map<String, Integer> adjusted = new HashMap<>();

            for (String v : dijkstraResult.keySet()) {
                int adjustedWeight = dijkstraResult.get(v) - h.get(u) + h.get(v);
                adjusted.put(v, adjustedWeight);
                log.debug("Final distance from {} to {} = {}", u, v, adjustedWeight);
            }

            result.put(u, adjusted);
        }

        log.info("Johnson’s algorithm completed successfully");

        GraphResponseDTO response = new GraphResponseDTO();
        response.setShortestPaths(result);
        return response;
    }

    /**
     * Runs Bellman-Ford algorithm to compute shortest paths from a source vertex.
     * Used to detect negative-weight cycles and compute vertex potentials for reweighting.
     *
     * @param edges  Map representation of graph (including dummy vertex).
     * @param source Source vertex (usually the dummy vertex).
     * @return Distance map or null if a negative-weight cycle is detected.
     */
    private Map<String, Integer> bellmanFord(Map<String, Map<String, Integer>> edges, String source) {
        Map<String, Integer> distance = new HashMap<>();
        for (String v : edges.keySet()) distance.put(v, Integer.MAX_VALUE / 2);
        distance.put(source, 0);

        int V = edges.size();
        for (int i = 0; i < V - 1; i++) {
            for (String u : edges.keySet()) {
                for (Map.Entry<String, Integer> e : edges.get(u).entrySet()) {
                    String v = e.getKey();
                    int w = e.getValue();
                    if (distance.get(u) + w < distance.get(v)) {
                        distance.put(v, distance.get(u) + w);
                    }
                }
            }
        }

        // Negative cycle check
        for (String u : edges.keySet()) {
            for (Map.Entry<String, Integer> e : edges.get(u).entrySet()) {
                String v = e.getKey();
                int w = e.getValue();
                if (distance.get(u) + w < distance.get(v)) {
                    return null; // Negative cycle detected
                }
            }
        }

        return distance;
    }

    /**
     * Runs Dijkstra’s algorithm from a given source on a non-negative weighted graph.
     *
     * @param graph  Reweighted graph (non-negative edge weights).
     * @param source Starting vertex.
     * @return Map of shortest distances from source to all reachable vertices.
     */
    private Map<String, Integer> dijkstra(Map<String, Map<String, Integer>> graph, String source) {
        Map<String, Integer> dist = new HashMap<>();
        PriorityQueue<Node> pq = new PriorityQueue<>();

        for (String v : graph.keySet()) {
            dist.put(v, Integer.MAX_VALUE / 2);
        }
        dist.put(source, 0);
        pq.add(new Node(source, 0));

        while (!pq.isEmpty()) {
            Node current = pq.poll();
            String u = current.getVertex();

            if (!graph.containsKey(u)) continue;

            for (Map.Entry<String, Integer> entry : graph.get(u).entrySet()) {
                String v = entry.getKey();
                int weight = entry.getValue();

                if (dist.get(u) + weight < dist.get(v)) {
                    dist.put(v, dist.get(u) + weight);
                    pq.add(new Node(v, dist.get(v)));
                }
            }
        }

        return dist;
    }
}
