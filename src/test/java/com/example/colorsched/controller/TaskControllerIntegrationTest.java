package com.example.colorsched.controller;

import com.example.colorsched.dto.TaskRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Full-stack integration tests for {@link TaskController}.
 *
 * These tests ensure that the controller works correctly within the full Spring context,
 * including HTTP endpoint validation, request deserialization, service logic execution,
 * and response serialization.
 */
@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc; // MockMvc is used to perform HTTP requests and assert the results.

    @Autowired
    private ObjectMapper objectMapper; // ObjectMapper helps convert Java objects to JSON and vice versa.

    /**
     * Verifies that a fully connected graph with 3 tasks results in each task being assigned a unique color.
     */
    @Test
    @DisplayName("Integration Test: Should return a valid schedule from actual service logic")
    void testIntegrationScheduling() throws Exception {
        // Construct a fully connected triangle graph
        TaskRequest request = new TaskRequest();
        request.setNumberOfTasks(3);
        request.setSharedResources(new int[][]{{0, 1}, {1, 2}, {0, 2}});

        // Submit the request and expect each task to have a unique slot (0, 1, 2)
        mockMvc.perform(post("/tasks/schedule")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isOk()) // Should return 200 OK
               .andExpect(jsonPath("$.totalColorsUsed", is(3))) // Expect 3 different colors used
               .andExpect(jsonPath("$.taskAssignments").isMap()) // Response contains task-slot mapping
               .andExpect(jsonPath("$.taskAssignments.0").value(anyOf(is(0), is(1), is(2))))
               .andExpect(jsonPath("$.taskAssignments.1").value(anyOf(is(0), is(1), is(2))))
               .andExpect(jsonPath("$.taskAssignments.2").value(anyOf(is(0), is(1), is(2))));
    }

    /**
     * Validates input where the number of tasks is zero.
     * Should trigger @Min(1) constraint failure.
     */
    @Test
    @DisplayName("Integration Test: Should return 400 for invalid request with 0 tasks")
    void testIntegrationValidationFail() throws Exception {
        TaskRequest request = new TaskRequest();
        request.setNumberOfTasks(0); // Invalid input
        request.setSharedResources(new int[][]{{0, 1}}); // Edge present, but ignored

        mockMvc.perform(post("/tasks/schedule")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isBadRequest()); // Constraint violation should trigger 400 Bad Request
    }

    /**
     * Simulates task indices that are out of bounds. Should result in an internal server error.
     */
    @Test
    @DisplayName("Integration Test: Should return 500 for invalid task indices")
    void testIntegrationServerErrorForInvalidIndices() throws Exception {
        TaskRequest request = new TaskRequest();
        request.setNumberOfTasks(2); // Tasks 0 and 1 are valid
        request.setSharedResources(new int[][]{{0, 5}}); // Task 5 is out of range

        mockMvc.perform(post("/tasks/schedule")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isInternalServerError()); // Expect error thrown by service
    }

    /**
     * Validates that all tasks can be assigned the same slot when there are no conflicts.
     */
    @Test
    @DisplayName("Integration Test: Should return a valid schedule when no conflicts exist")
    void testIntegrationNoConflicts() throws Exception {
        TaskRequest request = new TaskRequest();
        request.setNumberOfTasks(3); // Tasks are independent
        request.setSharedResources(new int[][]{}); // No edges

        // All tasks should get slot 0
        mockMvc.perform(post("/tasks/schedule")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.totalColorsUsed", is(1)))
               .andExpect(jsonPath("$.taskAssignments.0", is(0)))
               .andExpect(jsonPath("$.taskAssignments.1", is(0)))
               .andExpect(jsonPath("$.taskAssignments.2", is(0)));
    }

    /**
     * Verifies the algorithm handles duplicates, self-loops, and incomplete edges.
     */
    @Test
    @DisplayName("Integration Test: Should skip duplicate and invalid edges gracefully")
    void testIntegrationDuplicateAndInvalidEdges() throws Exception {
        TaskRequest request = new TaskRequest();
        request.setNumberOfTasks(3);
        request.setSharedResources(new int[][]{
                {0, 1},  // valid
                {1, 0},  // duplicate
                {2, 2},  // self-loop
                {1}      // incomplete
        });

        mockMvc.perform(post("/tasks/schedule")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.totalColorsUsed").value(greaterThanOrEqualTo(2)))
               .andExpect(jsonPath("$.taskAssignments.0").exists())
               .andExpect(jsonPath("$.taskAssignments.1").exists())
               .andExpect(jsonPath("$.taskAssignments.2").exists());
    }

    /**
     * Tests an empty input scenario with 0 tasks and empty edges.
     */
    @Test
    @DisplayName("Integration Test: No Tasks Submitted (Empty Input)")
    void testEmptyInput() throws Exception {
        TaskRequest request = new TaskRequest();
        request.setNumberOfTasks(0);
        request.setSharedResources(new int[][]{}); // Empty graph

        mockMvc.perform(post("/tasks/schedule")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isBadRequest()); // Should fail validation
    }

    /**
     * Confirms that with no shared resources, all tasks receive the same color.
     */
    @Test
    @DisplayName("Integration Test: Tasks with No Conflicts")
    void testNoConflicts() throws Exception {
        TaskRequest request = new TaskRequest();
        request.setNumberOfTasks(3);
        request.setSharedResources(new int[][]{}); // No conflict

        mockMvc.perform(post("/tasks/schedule")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.totalColorsUsed", is(1)))
               .andExpect(jsonPath("$.taskAssignments.0", is(0)))
               .andExpect(jsonPath("$.taskAssignments.1", is(0)))
               .andExpect(jsonPath("$.taskAssignments.2", is(0)));
    }

    /**
     * Tests a fully connected 4-node graph (complete graph).
     * Each task must be in a separate time slot.
     */
    @Test
    @DisplayName("Integration Test: All Tasks Fully Connected")
    void testFullyConnectedGraph() throws Exception {
        TaskRequest request = new TaskRequest();
        request.setNumberOfTasks(4);
        request.setSharedResources(new int[][]{
                {0, 1}, {0, 2}, {0, 3}, {1, 2}, {1, 3}, {2, 3} // Complete graph
        });

        mockMvc.perform(post("/tasks/schedule")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.totalColorsUsed", is(4))) // 4 distinct colors
               .andExpect(jsonPath("$.taskAssignments").isMap());
    }

    /**
     * Validates proper alternating color assignment in a chain-structured conflict graph.
     */
    @Test
    @DisplayName("Integration Test: Chain Conflict")
    void testChainConflict() throws Exception {
        TaskRequest request = new TaskRequest();
        request.setNumberOfTasks(4);
        request.setSharedResources(new int[][]{{0, 1}, {1, 2}, {2, 3}}); // Chain pattern

        mockMvc.perform(post("/tasks/schedule")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.totalColorsUsed", is(2))) // Should alternate colors
               .andExpect(jsonPath("$.taskAssignments").isMap());
    }

    /**
     * Ensures a ring structure with an odd number of tasks uses 3 colors.
     */
    @Test
    @DisplayName("Integration Test: Ring Conflict (Odd Number of Tasks)")
    void testRingConflict() throws Exception {
        TaskRequest request = new TaskRequest();
        request.setNumberOfTasks(3);
        request.setSharedResources(new int[][]{{0, 1}, {1, 2}, {2, 0}}); // Ring structure

        mockMvc.perform(post("/tasks/schedule")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.totalColorsUsed", is(3))) // Requires 3 colors to avoid conflict
               .andExpect(jsonPath("$.taskAssignments").isMap());
    }
}
