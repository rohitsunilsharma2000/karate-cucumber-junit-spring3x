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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MissingServletRequestParameterException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for {@link GlobalExceptionHandler} within the context of {@link GraphController}.
 *
 * <p>
 * This test class verifies that application exceptions are correctly mapped to standardized HTTP responses.
 * The tests validate:
 * <ul>
 *     <li>Validation failures return 400 Bad Request with detailed error messages</li>
 *     <li>Malformed JSON input results in 400 Bad Request with a specific error message</li>
 *     <li>Custom exceptions (e.g., {@link GraphAnalysisException}) return 500 Internal Server Error with the exception's message</li>
 *     <li>Unhandled exceptions return a generic 500 Internal Server Error with fallback messaging</li>
 * </ul>
 * </p>
 *
 * @since 2025-03-27
 */
@WebMvcTest(controllers = GraphController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
@ContextConfiguration(classes = {GraphController.class, GlobalExceptionHandler.class})
public class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc; // Used to simulate HTTP requests in integration tests

    @Autowired
    private ObjectMapper objectMapper; // Converts Java objects to JSON strings for request payloads

    @MockBean
    private com.example.graph.service.GraphService graphService; // Mocks the GraphService dependency

    /**
     * Verifies that validation errors on a {@link GraphRequest} result in a structured 400 Bad Request.
     *
     * <p>
     * <strong>Scenario:</strong> An invalid GraphRequest (with missing required fields) is sent.
     * The endpoint should return HTTP 400 along with a message indicating that validation failed and include error details.
     * </p>
     *
     * @throws Exception if request processing fails.
     */
    @Test
    @DisplayName("Should return 400 for invalid GraphRequest (validation error)")
    void testHandleValidationErrors() throws Exception {
        // GIVEN: Create an invalid GraphRequest instance with missing required fields.
        GraphRequest invalidRequest = new GraphRequest(); // Required fields are not set

        // WHEN & THEN: Perform a POST request to /graph/bridges and expect HTTP 400 with a validation error message.
        mockMvc.perform(post("/graph/bridges")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidRequest)))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.message").value("Validation failed"))
               .andExpect(jsonPath("$.errors").exists());
    }

    /**
     * Verifies that malformed JSON input triggers a 400 Bad Request response.
     *
     * <p>
     * <strong>Scenario:</strong> A POST request is sent with improperly formatted JSON (e.g., missing closing brackets).
     * The endpoint should detect the JSON parsing error and return HTTP 400 with a message indicating a malformed JSON request.
     * </p>
     *
     * @throws Exception if request processing fails.
     */
    @Test
    @DisplayName("Should return 400 for malformed JSON input")
    void testHandleJsonParseErrors() throws Exception {
        // GIVEN: Define a malformed JSON string (missing closing bracket).
        String malformedJson = "{\"vertices\": 5, \"edges\": [[0,1], [1,2]"; // Malformed JSON

        // WHEN & THEN: Perform a POST request to /graph/bridges using the malformed JSON and expect HTTP 400.
        mockMvc.perform(post("/graph/bridges")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(malformedJson))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.message").value("Malformed JSON request"));
    }

    /**
     * Verifies that a custom {@link GraphAnalysisException} thrown by the service layer is mapped to a 500 Internal Server Error.
     *
     * <p>
     * <strong>Scenario:</strong> The GraphService throws a GraphAnalysisException when processing a valid GraphRequest.
     * The endpoint should catch the exception and return HTTP 500 with the exception's message.
     * </p>
     *
     * @throws Exception if request processing fails.
     */
    @Test
    @DisplayName("Should return 500 for custom GraphAnalysisException")
    void testHandleGraphAnalysisException() throws Exception {
        // GIVEN: Create a valid GraphRequest with proper vertices and edges.
        GraphRequest request = new GraphRequest();
        request.setVertices(3);
        request.setEdges(List.of(List.of(0, 1), List.of(1, 2)));

        // AND: Configure the mocked GraphService to throw a GraphAnalysisException when findBridges is called.
        doThrow(new GraphAnalysisException("Failed to analyze graph"))
                .when(graphService).findBridges(request.getVertices(), request.getEdges());

        // WHEN & THEN: Perform a POST request to /graph/bridges and expect HTTP 500 with the exception's message.
        mockMvc.perform(post("/graph/bridges")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isInternalServerError())
               .andExpect(jsonPath("$.message").value("Failed to analyze graph"));
    }

    /**
     * Verifies that unhandled runtime exceptions are caught and result in a generic 500 Internal Server Error.
     *
     * <p>
     * <strong>Scenario:</strong> The GraphService throws a generic RuntimeException when processing a valid GraphRequest.
     * The endpoint should return HTTP 500 with a generic error message indicating that an unexpected error occurred.
     * </p>
     *
     * @throws Exception if request processing fails.
     */
    @Test
    @DisplayName("Should return 500 for unexpected exception")
    void testHandleGenericException() throws Exception {
        // GIVEN: Create a valid GraphRequest with proper vertices and edges.
        GraphRequest request = new GraphRequest();
        request.setVertices(3);
        request.setEdges(List.of(List.of(0, 1), List.of(1, 2)));

        // AND: Configure the mocked GraphService to throw a RuntimeException when findBridges is called.
        doThrow(new RuntimeException("Unexpected error"))
                .when(graphService).findBridges(request.getVertices(), request.getEdges());

        // WHEN & THEN: Perform a POST request to /graph/bridges and expect HTTP 500 with a generic error message.
        mockMvc.perform(post("/graph/bridges")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isInternalServerError())
               .andExpect(jsonPath("$.message").value("An unexpected error occurred. Please try again later."));
    }

    @Test
    @DisplayName("Should return 400 for missing servlet request parameter")
    void testHandleMissingServletRequestParameterException() throws Exception {
        // GIVEN: Simulate a request to a real endpoint that requires a query param (we'll trigger the exception manually)
        String missingParamName = "param";
        String missingParamType = "String";

        // Create the exception manually
        MissingServletRequestParameterException exception = new MissingServletRequestParameterException(missingParamName, missingParamType);

        // WHEN: Call the handler method directly
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        ResponseEntity<Object> response = handler.handleMissingParams(exception);

        // THEN: Assert the response is 400 and contains the correct message
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Required request parameter 'param'"));
    }

}
