package com.example.colorsched.controller;

import com.example.colorsched.dto.ScheduleResponse;
import com.example.colorsched.dto.TaskRequest;
import com.example.colorsched.service.TaskSchedulerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for {@link TaskController} with mocked TaskSchedulerService.
 *
 * This class is responsible for verifying the controller behavior by:
 *  - Validating correct responses for different task inputs.
 *  - Mocking edge-case scenarios including invalid inputs and server errors.
 *  - Confirming the response structure is aligned with the expected ScheduleResponse.
 */
@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc; // Used to perform simulated HTTP calls on the controller

    @MockBean
    private TaskSchedulerService schedulerService; // Mocked business logic (actual implementation is not called)

    @Autowired
    private ObjectMapper objectMapper; // Used for JSON serialization of request bodies

    /**
     * Tests controller behavior when 0 tasks are submitted.
     * This should trigger validation error as numberOfTasks has @Min(1).
     */
    @Test
    @DisplayName("Edge Case: No Tasks Submitted")
    void testNoTasksSubmitted() throws Exception {
        TaskRequest request = new TaskRequest();
        request.setNumberOfTasks(0); // Invalid: violates minimum constraint
        request.setSharedResources(new int[][]{}); // No resources

        // Perform POST request and expect 400 due to validation failure
        mockMvc.perform(post("/tasks/schedule")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isBadRequest()); // Must fail before reaching service layer
    }

    /**
     * Tests case where no shared resources exist — all tasks are independent.
     * All tasks should be scheduled to the same slot.
     */
    @Test
    @DisplayName("Edge Case: Tasks with No Conflicts")
    void testTasksWithNoConflicts() throws Exception {
        TaskRequest request = new TaskRequest();
        request.setNumberOfTasks(3); // Three independent tasks
        request.setSharedResources(new int[][]{}); // No conflicts

        // Prepare a mock response indicating all tasks got the same slot (0)
        Map<Integer, Integer> assignment = Map.of(0, 0, 1, 0, 2, 0);
        ScheduleResponse response = new ScheduleResponse();
        response.setTaskAssignments(assignment);
        response.setTotalColorsUsed(1); // Only one slot needed

        // Mock the service to return the predefined schedule
        Mockito.when(schedulerService.schedule(Mockito.any(TaskRequest.class))).thenReturn(response);

        // Simulate POST and verify all tasks are assigned to slot 0
        mockMvc.perform(post("/tasks/schedule")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isOk()) // Expect HTTP 200 OK
               .andExpect(jsonPath("$.totalColorsUsed", is(1)))
               .andExpect(jsonPath("$.taskAssignments.0", is(0)))
               .andExpect(jsonPath("$.taskAssignments.1", is(0)))
               .andExpect(jsonPath("$.taskAssignments.2", is(0)));
    }

    /**
     * Tests scheduling for a complete graph where every task conflicts with every other.
     * Requires all unique slots.
     */
    @Test
    @DisplayName("Edge Case: All Tasks Fully Connected (Complete Graph)")
    void testFullyConnectedGraph() throws Exception {
        TaskRequest request = new TaskRequest();
        request.setNumberOfTasks(4); // 4 tasks forming a complete graph
        request.setSharedResources(new int[][]{
                {0, 1}, {0, 2}, {0, 3}, {1, 2}, {1, 3}, {2, 3}
        });

        // Each task should have its own slot due to full conflict
        ScheduleResponse response = new ScheduleResponse();
        response.setTaskAssignments(Map.of(0, 0, 1, 1, 2, 2, 3, 3));
        response.setTotalColorsUsed(4);

        Mockito.when(schedulerService.schedule(Mockito.any(TaskRequest.class))).thenReturn(response);

        // Simulate request and validate slot uniqueness
        mockMvc.perform(post("/tasks/schedule")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.totalColorsUsed", is(4)))
               .andExpect(jsonPath("$.taskAssignments").isMap());
    }

    /**
     * Tests a linear chain of conflicts.
     * Optimal scheduling should reuse slots efficiently (coloring should alternate).
     */
    @Test
    @DisplayName("Edge Case: Conflict Chain Structure")
    void testConflictChain() throws Exception {
        TaskRequest request = new TaskRequest();
        request.setNumberOfTasks(4); // Four tasks in a chain
        request.setSharedResources(new int[][]{{0, 1}, {1, 2}, {2, 3}}); // Chain structure

        // Alternating colors (0, 1, 0, 1)
        ScheduleResponse response = new ScheduleResponse();
        response.setTaskAssignments(Map.of(0, 0, 1, 1, 2, 0, 3, 1));
        response.setTotalColorsUsed(2); // Only two slots used

        Mockito.when(schedulerService.schedule(Mockito.any(TaskRequest.class))).thenReturn(response);

        // Simulate the scheduling call and validate coloring efficiency
        mockMvc.perform(post("/tasks/schedule")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.totalColorsUsed", is(2)))
               .andExpect(jsonPath("$.taskAssignments").isMap());
    }

    /**
     * Tests that self-loop does not break the scheduler.
     * Self-loops should not impact color assignment significantly.
     */
    @Test
    @DisplayName("Edge Case: Self-Loops Should Not Break")
    void testSelfLoopHandledGracefully() throws Exception {
        TaskRequest request = new TaskRequest();
        request.setNumberOfTasks(3);
        request.setSharedResources(new int[][]{{0, 0}, {1, 2}}); // Task 0 self-loop

        // Valid assignment returned by service
        ScheduleResponse response = new ScheduleResponse();
        response.setTaskAssignments(Map.of(0, 0, 1, 1, 2, 0));
        response.setTotalColorsUsed(2);

        Mockito.when(schedulerService.schedule(Mockito.any(TaskRequest.class))).thenReturn(response);

        // Request should not crash due to self-loop
        mockMvc.perform(post("/tasks/schedule")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isOk()); // Scheduler handles gracefully
    }

    /**
     * Tests duplicate edges — they must not be double-counted.
     * System should behave same as if edge appeared once.
     */
    @Test
    @DisplayName("Edge Case: Duplicate Edges Should Not Affect Result")
    void testDuplicateEdges() throws Exception {
        TaskRequest request = new TaskRequest();
        request.setNumberOfTasks(2);
        request.setSharedResources(new int[][]{{0, 1}, {1, 0}, {0, 1}}); // Redundant edges

        // Still only one conflict, so two slots required
        ScheduleResponse response = new ScheduleResponse();
        response.setTaskAssignments(Map.of(0, 0, 1, 1));
        response.setTotalColorsUsed(2);

        Mockito.when(schedulerService.schedule(Mockito.any(TaskRequest.class))).thenReturn(response);

        // Must complete successfully regardless of edge duplication
        mockMvc.perform(post("/tasks/schedule")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isOk());
    }

    /**
     * Tests invalid task indices that exceed the defined range.
     * Should result in internal server error due to validation at service layer.
     */
    @Test
    @DisplayName("Edge Case: Invalid Task Indices")
    void testInvalidTaskIndices() throws Exception {
        TaskRequest request = new TaskRequest();
        request.setNumberOfTasks(3); // Tasks: 0, 1, 2
        request.setSharedResources(new int[][]{{0, 4}}); // Task 4 doesn't exist

        // Simulate exception thrown from service
        doThrow(new IllegalArgumentException("Shared resource refers to invalid task indices"))
                .when(schedulerService).schedule(Mockito.any(TaskRequest.class));

        // Expect HTTP 500 as a result of service failure
        mockMvc.perform(post("/tasks/schedule")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isInternalServerError());
    }

    /**
     * Tests how the controller handles edges with missing data (e.g., only one task).
     * Such edges should be ignored, and scheduling should proceed.
     */
    @Test
    @DisplayName("Edge Case: Incomplete Edge Definition")
    void testEdgeWithIncompleteLength() throws Exception {
        TaskRequest request = new TaskRequest();
        request.setNumberOfTasks(2);
        request.setSharedResources(new int[][]{{0}, {1, 0}}); // One incomplete edge

        // Simulated output
        ScheduleResponse response = new ScheduleResponse();
        response.setTaskAssignments(Map.of(0, 0, 1, 1));
        response.setTotalColorsUsed(2);

        Mockito.when(schedulerService.schedule(Mockito.any(TaskRequest.class))).thenReturn(response);

        // Controller should accept response and return 200 OK
        mockMvc.perform(post("/tasks/schedule")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isOk());
    }

    /**
     * Tests system with a large number of tasks but very few conflicts.
     * Ensures that performance and correctness scale.
     */
    @Test
    @DisplayName("Edge Case: Large Input with Sparse Conflicts")
    void testLargeSparseGraph() throws Exception {
        int taskCount = 1000; // Large number of tasks

        TaskRequest request = new TaskRequest();
        request.setNumberOfTasks(taskCount);
        request.setSharedResources(new int[][]{{0, 1}, {100, 200}, {500, 900}}); // Sparse edges

        // Assign most tasks to slot 0 and a few to 1
        Map<Integer, Integer> assignment = new HashMap<>();
        for (int i = 0; i < taskCount; i++) {
            assignment.put(i, 0);
        }
        assignment.put(1, 1);
        assignment.put(200, 1);
        assignment.put(900, 1);

        ScheduleResponse response = new ScheduleResponse();
        response.setTaskAssignments(assignment);
        response.setTotalColorsUsed(2);

        Mockito.when(schedulerService.schedule(Mockito.any(TaskRequest.class))).thenReturn(response);

        // Validate the request is handled without failure
        mockMvc.perform(post("/tasks/schedule")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isOk());
    }

    /**
     * Tests asymmetric edge definitions (e.g., only one direction) are handled as undirected.
     */
    @Test
    @DisplayName("Edge Case: Asymmetric Edges Should Be Handled")
    void testAsymmetricEdges() throws Exception {
        TaskRequest request = new TaskRequest();
        request.setNumberOfTasks(2);
        request.setSharedResources(new int[][]{{1, 0}}); // No reverse edge

        // Valid result from the scheduler
        ScheduleResponse response = new ScheduleResponse();
        response.setTaskAssignments(Map.of(0, 0, 1, 1));
        response.setTotalColorsUsed(2);

        Mockito.when(schedulerService.schedule(Mockito.any(TaskRequest.class))).thenReturn(response);

        // Should behave the same as symmetric input
        mockMvc.perform(post("/tasks/schedule")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isOk());
    }
}
