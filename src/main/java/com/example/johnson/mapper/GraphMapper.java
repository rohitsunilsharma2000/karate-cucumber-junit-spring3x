package com.example.johnson.mapper;

import com.example.johnson.dto.GraphRequestDTO;
import com.example.johnson.model.Graph;

/**
 * Mapper class to convert DTOs to domain model.
 */
public class GraphMapper {

    /**
     * Converts the request DTO to the internal Graph model.
     *
     * @param dto Request data containing vertices and edges.
     * @return Graph model instance.
     */
    public static Graph toDomain(GraphRequestDTO dto) {
        Graph graph = new Graph(dto.getVertices());

        for (GraphRequestDTO.EdgeDTO edge : dto.getEdges()) {
            graph.addEdge(edge.getFrom(), edge.getTo(), edge.getWeight());
        }

        return graph;
    }
}
