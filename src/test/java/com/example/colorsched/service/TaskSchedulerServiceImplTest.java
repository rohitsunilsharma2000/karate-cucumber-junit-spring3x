package com.example.colorsched.service;

import com.example.colorsched.dto.ScheduleResponse;
import com.example.colorsched.dto.TaskRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link TaskSchedulerServiceImpl}.
 *
 * <p>
 * These tests verify the correctness of the task scheduling algorithm using greedy graph coloring.
 * Covers edge cases such as:
 * <ul>
 *     <li>No conflicts</li>
 *     <li>Partial conflicts</li>
 *     <li>Fully connected graphs</li>
 *     <li>Invalid indices and malformed edges</li>
 * </ul>
 * </p>
 *
 * <p><strong>Pass Conditions:</strong> Valid results or appropriate exceptions.</p>
 * <p><strong>Fail Conditions:</strong> Incorrect scheduling or missing validation.</p>
 *
 */
class TaskSchedulerServiceImplTest {

    private TaskSchedulerServiceImpl schedulerService;

    @BeforeEach
    void setUp() {
        // Initialize the real service implementation
        schedulerService = new TaskSchedulerServiceImpl();
    }

    /**
     * Test Case: No resource conflicts
     * <p>All tasks should be scheduled in the same slot (slot 0).</p>
     */
    @Test
    @DisplayName("Should schedule non-conflicting tasks correctly")
    void testScheduleWithNoConflicts() {
        TaskRequest request = new TaskRequest();
        request.setNumberOfTasks(3); // Three tasks
        request.setSharedResources(new int[][]{}); // No shared resources

        ScheduleResponse response = schedulerService.schedule(request);

        assertEquals(1, response.getTotalColorsUsed(), "Expected only one slot");
        assertEquals(3, response.getTaskAssignments().size(), "All tasks should be assigned");
        assertTrue(response.getTaskAssignments().values().stream().allMatch(slot -> slot == 0),
                   "All tasks should be in the same slot since there are no conflicts");
    }

    /**
     * Test Case: Tasks with sequential (chain) conflicts
     * <p>Ensures adjacent tasks get different slots but minimal slots are used overall.</p>
     */
    @Test
    @DisplayName("Should assign different slots to conflicting tasks")
    void testScheduleWithConflicts() {
        TaskRequest request = new TaskRequest();
        request.setNumberOfTasks(3);
        request.setSharedResources(new int[][]{{0, 1}, {1, 2}}); // Task 0 ↔ Task 1 ↔ Task 2

        ScheduleResponse response = schedulerService.schedule(request);

        assertEquals(2, response.getTotalColorsUsed(), "Expected two distinct slots due to chain conflict");
        assertEquals(3, response.getTaskAssignments().size());
        assertNotEquals(response.getTaskAssignments().get(0), response.getTaskAssignments().get(1));
        assertNotEquals(response.getTaskAssignments().get(1), response.getTaskAssignments().get(2));
    }

    /**
     * Test Case: Invalid task index in the shared resource matrix
     * <p>Should throw an IllegalArgumentException for out-of-bounds task reference.</p>
     */
    @Test
    @DisplayName("Should throw exception for invalid shared resource indices")
    void testInvalidResourceIndex() {
        TaskRequest request = new TaskRequest();
        request.setNumberOfTasks(2); // Only tasks 0 and 1 are valid
        request.setSharedResources(new int[][]{{0, 2}}); // Invalid index 2

        // Verify that the method throws the expected exception
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                                                          () -> schedulerService.schedule(request),
                                                          "Expected exception for invalid indices");

        assertTrue(exception.getMessage().contains("invalid task indices"), "Exception message should mention invalid indices");
    }

    /**
     * Test Case: Malformed edge (less than 2 elements)
     * <p>Edges with insufficient length should be ignored, and scheduling should proceed with valid edges.</p>
     */
    @Test
    @DisplayName("Should skip invalid edges with less than 2 elements")
    void testEdgeWithInsufficientLength() {
        TaskRequest request = new TaskRequest();
        request.setNumberOfTasks(2);
        request.setSharedResources(new int[][]{{0}, {1, 0}}); // Only one valid edge

        ScheduleResponse response = schedulerService.schedule(request);

        assertEquals(2, response.getTotalColorsUsed(), "Should require 2 slots due to valid edge");
        assertEquals(2, response.getTaskAssignments().size(), "Both tasks must be scheduled");
        assertNotEquals(response.getTaskAssignments().get(0), response.getTaskAssignments().get(1),
                        "Tasks 0 and 1 must be in different slots due to conflict");
    }

    /**
     * Test Case: Fully connected graph (complete graph)
     * <p>Each task conflicts with every other task, so each must have a unique slot.</p>
     */
    @Test
    @DisplayName("Should handle a fully connected graph")
    void testFullyConnectedGraph() {
        int n = 4; // Number of tasks
        TaskRequest request = new TaskRequest();
        request.setNumberOfTasks(n);

        // Generate all pairs of tasks (complete graph)
        int[][] edges = new int[n * (n - 1) / 2][2];
        int index = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                edges[index++] = new int[]{i, j};
            }
        }
        request.setSharedResources(edges);

        ScheduleResponse response = schedulerService.schedule(request);

        assertEquals(n, response.getTotalColorsUsed(), "Each task should use a unique slot");
        for (int i = 0; i < n; i++) {
            assertTrue(response.getTaskAssignments().containsKey(i), "Task " + i + " must be assigned a slot");
        }
    }
}
