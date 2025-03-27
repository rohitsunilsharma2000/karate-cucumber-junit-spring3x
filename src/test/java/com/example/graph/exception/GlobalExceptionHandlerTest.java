package com.example.graph.exception;

import com.example.graph.dto.GraphRequest;
import com.example.graph.controller.GraphController;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for {@link GlobalExceptionHandler} within the context of {@link GraphController}.
 *
 * <p>This test class verifies the correct mapping of application exceptions to standardized HTTP responses.
 * It ensures:
 * <ul>
 *     <li>Validation failures return 400 Bad Request</li>
 *     <li>Malformed JSON results in 400 Bad Request</li>
 *     <li>Custom exceptions like {@link GraphAnalysisException} return 500 Internal Server Error</li>
 *     <li>Unhandled exceptions return a generic 500 Internal Server Error with fallback messaging</li>
 * </ul>
 *
 * @since 2025-03-27
 */
@WebMvcTest(controllers = GraphController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
@ContextConfiguration(classes = {GraphController.class, GlobalExceptionHandler.class})
public class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private com.example.graph.service.GraphService graphService;

    /**
     * Verifies that validation errors on {@link GraphRequest} return a structured 400 Bad Request
     * with descriptive error messages.
     */
    @Test
    @DisplayName("Should return 400 for invalid GraphRequest (validation error)")
    void testHandleValidationErrors() throws Exception {
        GraphRequest invalidRequest = new GraphRequest(); // missing required fields

        mockMvc.perform(post("/graph/bridges")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidRequest)))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.message").value("Validation failed"))
               .andExpect(jsonPath("$.errors").exists());
    }

    /**
     * Verifies that malformed JSON input triggers a 400 Bad Request response with appropriate message.
     */
    @Test
    @DisplayName("Should return 400 for malformed JSON input")
    void testHandleJsonParseErrors() throws Exception {
        String malformedJson = "{\"vertices\": 5, \"edges\": [[0,1], [1,2]"; // missing closing ]

        mockMvc.perform(post("/graph/bridges")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(malformedJson))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.message").value("Malformed JSON request"));
    }

    /**
     * Verifies that a thrown {@link GraphAnalysisException} results in a 500 Internal Server Error response
     * with the exact exception message.
     */
    @Test
    @DisplayName("Should return 500 for custom GraphAnalysisException")
    void testHandleGraphAnalysisException() throws Exception {
        GraphRequest request = new GraphRequest();
        request.setVertices(3);
        request.setEdges(List.of(List.of(0, 1), List.of(1, 2)));

        doThrow(new GraphAnalysisException("Failed to analyze graph"))
                .when(graphService).findBridges(request.getVertices(), request.getEdges());

        mockMvc.perform(post("/graph/bridges")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isInternalServerError())
               .andExpect(jsonPath("$.message").value("Failed to analyze graph"));
    }

    /**
     * Verifies that any unhandled runtime exceptions are caught and return a generic 500 Internal Server Error
     * with fallback error messaging.
     */
    @Test
    @DisplayName("Should return 500 for unexpected exception")
    void testHandleGenericException() throws Exception {
        GraphRequest request = new GraphRequest();
        request.setVertices(3);
        request.setEdges(List.of(List.of(0, 1), List.of(1, 2)));

        doThrow(new RuntimeException("Unexpected error"))
                .when(graphService).findBridges(request.getVertices(), request.getEdges());

        mockMvc.perform(post("/graph/bridges")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isInternalServerError())
               .andExpect(jsonPath("$.message").value("An unexpected error occurred. Please try again later."));
    }
}
