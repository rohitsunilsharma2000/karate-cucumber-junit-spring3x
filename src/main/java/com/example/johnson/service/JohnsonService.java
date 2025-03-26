package com.example.johnson.service;

import com.example.johnson.dto.GraphResponseDTO;
import com.example.johnson.model.Graph;
import com.example.johnson.model.Node;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Service class that implements Johnson's algorithm.
 */
@Service
public class JohnsonService {

    /**
     * Computes all-pairs shortest paths using Johnson's algorithm.
     *
     * @param graph The input graph.
     * @return A DTO containing shortest path distances.
     */
    public GraphResponseDTO computeAllPairsShortestPaths(Graph graph) {
        List<String> vertices = graph.getVertices();
        Map<String, Map<String, Integer>> edges = graph.getEdges();
        Map<String, Integer> h = new HashMap<>();

        // Add dummy vertex
        String dummy = "__DUMMY__";
        Map<String, Map<String, Integer>> augmentedEdges = new HashMap<>(edges);
        augmentedEdges.put(dummy, new HashMap<>());
        for (String v : vertices) {
            augmentedEdges.get(dummy).put(v, 0);
        }

        // Bellman-Ford to compute h(v)
        h = bellmanFord(augmentedEdges, dummy);
        if (h == null) throw new RuntimeException("Graph contains negative cycles");

        // Reweight edges
        Map<String, Map<String, Integer>> reweighted = new HashMap<>();
        for (String u : edges.keySet()) {
            reweighted.put(u, new HashMap<>());
            for (String v : edges.get(u).keySet()) {
                int newWeight = edges.get(u).get(v) + h.get(u) - h.get(v);
                reweighted.get(u).put(v, newWeight);
            }
        }

        // Dijkstra for all vertices
        Map<String, Map<String, Integer>> result = new HashMap<>();
        for (String u : vertices) {
            Map<String, Integer> dijkstra = dijkstra(reweighted, u);
            Map<String, Integer> adjusted = new HashMap<>();
            for (String v : dijkstra.keySet()) {
                int adjustedWeight = dijkstra.get(v) - h.get(u) + h.get(v);
                adjusted.put(v, adjustedWeight);
            }
            result.put(u, adjusted);
        }

        GraphResponseDTO response = new GraphResponseDTO();
        response.setShortestPaths(result);
        return response;
    }

    private Map<String, Integer> bellmanFord(Map<String, Map<String, Integer>> edges, String source) {
        Map<String, Integer> distance = new HashMap<>();
        for (String v : edges.keySet()) distance.put(v, Integer.MAX_VALUE / 2);
        distance.put(source, 0);

        int V = edges.keySet().size();
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

        // Check for negative-weight cycles
        for (String u : edges.keySet()) {
            for (Map.Entry<String, Integer> e : edges.get(u).entrySet()) {
                String v = e.getKey();
                int w = e.getValue();
                if (distance.get(u) + w < distance.get(v)) {
                    return null;
                }
            }
        }

        return distance;
    }

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

            if (graph.get(u) == null) continue;

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
