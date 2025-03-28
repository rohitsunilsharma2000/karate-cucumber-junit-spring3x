package com.example.turingLLMTuningSystem.controller;
import com.example.turingLLMTuningSystem.dto.TaskRequest;
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
 * Integration test class for {@link TaskController}.
 *
 * <p>
 * This class verifies that the controller interacts with the full Spring context,
 * including validation, service logic, and response formatting.
 * </p>
 *
 * <p>
 * <strong>Scenarios Covered:</strong>
 * <ul>
 *     <li>Valid request resulting in correct task scheduling output</li>
 *     <li>Invalid request resulting in validation failure (HTTP 400)</li>
 * </ul>
 * </p>
 */
@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Test: Full integration of scheduling flow with a valid request
     *
     * <p>
     * Sends a request to schedule 3 tasks with mutual conflicts (fully connected graph),
     * which should result in using 3 distinct colors.
     * </p>
     *
     * @throws Exception if request or response processing fails
     */
    @Test
    @DisplayName("Integration Test: Should return a valid schedule from actual service logic")
    void testIntegrationScheduling() throws Exception {
        // Prepare a valid request with 3 tasks connected to each other
        TaskRequest request = new TaskRequest();
        request.setNumberOfTasks(3);
        request.setSharedResources(new int[][]{
                {0, 1}, {1, 2}, {0, 2}
        });

        // Perform POST /tasks/schedule and verify success and structure
        mockMvc.perform(post("/tasks/schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()) // Expect HTTP 200 OK
                .andExpect(jsonPath("$.totalColorsUsed", is(3))) // Expect 3 unique time slots
                .andExpect(jsonPath("$.taskAssignments").isMap()) // Response should be a map
                // Ensure each task has a valid color assigned (0, 1, or 2)
                .andExpect(jsonPath("$.taskAssignments.0").value(anyOf(is(0), is(1), is(2))))
                .andExpect(jsonPath("$.taskAssignments.1").value(anyOf(is(0), is(1), is(2))))
                .andExpect(jsonPath("$.taskAssignments.2").value(anyOf(is(0), is(1), is(2))));
    }

    /**
     * Test: Ensures validation failure results in HTTP 400 Bad Request
     *
     * <p>
     * Submits a request with 0 tasks, which violates @Min(1) constraint in TaskRequest.
     * </p>
     *
     * @throws Exception if request fails
     */
    @Test
    @DisplayName("Integration Test: Should return 400 for invalid request with 0 tasks")
    void testIntegrationValidationFail() throws Exception {
        // Prepare an invalid request where numberOfTasks is below allowed minimum
        TaskRequest invalidRequest = new TaskRequest();
        invalidRequest.setNumberOfTasks(0); // Invalid: violates @Min(1)
        invalidRequest.setSharedResources(new int[][]{{0, 1}});

        // Perform POST /tasks/schedule and expect validation error
        mockMvc.perform(post("/tasks/schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest()); // Expect HTTP 400 Bad Request
    }
}
