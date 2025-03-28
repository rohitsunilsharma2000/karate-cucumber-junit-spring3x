package com.example.turingLLMTuningSystem.controller;

import com.example.turingLLMTuningSystem.dto.ScheduleResponse;
import com.example.turingLLMTuningSystem.dto.TaskRequest;
import com.example.turingLLMTuningSystem.service.TaskSchedulerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit test class for {@link TaskController}.
 * <p>
 * This test class verifies the behavior of the /tasks/schedule endpoint
 * under both valid and invalid request conditions using a mocked service layer.
 * </p>
 *
 * @since 2025-03-28
 */
@WebMvcTest(TaskController.class)
class TaskControllerTest {

    /**
     * MockMvc instance to simulate HTTP requests and assertions on responses.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Mocked TaskSchedulerService injected into the controller.
     */
    @MockBean
    private TaskSchedulerService schedulerService;

    /**
     * ObjectMapper to serialize and deserialize Java objects to/from JSON.
     */
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Initializes Mockito annotations before each test.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    /**
     * Tests that a valid task scheduling request returns HTTP 200 with a correct response body.
     *
     * @throws Exception if MockMvc fails to process the request
     */
    @Test
    @DisplayName("Unit Test: Should return schedule for valid task request")
    void testScheduleTasksSuccess() throws Exception {
        // Prepare a mock response from the scheduler service
        ScheduleResponse mockResponse = new ScheduleResponse();
        mockResponse.setTaskAssignments(Map.of(0, 0, 1, 1)); // Task 0 → slot 0, Task 1 → slot 1
        mockResponse.setTotalColorsUsed(2); // Two distinct colors/slots used

        // Configure mocked service behavior
        Mockito.when(schedulerService.schedule(Mockito.any(TaskRequest.class)))
                .thenReturn(mockResponse);

        // Create a valid request object with 2 tasks sharing one resource
        TaskRequest request = new TaskRequest();
        request.setNumberOfTasks(2);
        request.setSharedResources(new int[][]{{0, 1}});

        // Perform the POST request and verify response content
        mockMvc.perform(post("/tasks/schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()) // HTTP 200 OK
                .andExpect(jsonPath("$.totalColorsUsed", is(2))) // Verify total colors used
                .andExpect(jsonPath("$.taskAssignments.0", is(0))) // Task 0 → slot 0
                .andExpect(jsonPath("$.taskAssignments.1", is(1))); // Task 1 → slot 1
    }

    /**
     * Tests that an invalid request with numberOfTasks = 0 fails with HTTP 400.
     *
     * @throws Exception if MockMvc fails to process the request
     */
    @Test
    @DisplayName("Unit Test: Should return 400 for invalid input")
    void testValidationError() throws Exception {
        // Create an invalid request where numberOfTasks is zero (violates @Min(1))
        TaskRequest request = new TaskRequest();
        request.setNumberOfTasks(0); // Invalid input
        request.setSharedResources(new int[][]{{0, 1}});

        // Perform the POST request and expect HTTP 400 Bad Request
        mockMvc.perform(post("/tasks/schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest()); // Validation error
    }
}
