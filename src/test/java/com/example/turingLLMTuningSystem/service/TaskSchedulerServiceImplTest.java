package com.example.turingLLMTuningSystem.service;

import com.example.turingLLMTuningSystem.dto.ScheduleResponse;
import com.example.turingLLMTuningSystem.dto.TaskRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link TaskSchedulerServiceImpl}.
 *
 * <p>These tests verify the correctness of the task scheduling algorithm, including edge cases
 * such as minimal input, disconnected tasks, and invalid task indices.</p>
 *
 * <p><strong>Pass/Fail Conditions:</strong></p>
 * <ul>
 *     <li>Pass: Valid scheduling result with expected time slots and colors used.</li>
 *     <li>Fail: Incorrect slot assignment or exception not thrown for invalid inputs.</li>
 * </ul>
 */
class TaskSchedulerServiceImplTest {

    private TaskSchedulerServiceImpl schedulerService;

    @BeforeEach
    void setUp() {
        schedulerService = new TaskSchedulerServiceImpl();
    }

    @Test
    @DisplayName("Should schedule non-conflicting tasks correctly")
    void testScheduleWithNoConflicts() {
        TaskRequest request = new TaskRequest();
        request.setNumberOfTasks(3);
        request.setSharedResources(new int[][]{}); // No conflicts

        ScheduleResponse response = schedulerService.schedule(request);

        assertEquals(1, response.getTotalColorsUsed(), "Expected only one slot");
        assertEquals(3, response.getTaskAssignments().size(), "All tasks should be assigned");
        assertTrue(response.getTaskAssignments().values().stream().allMatch(slot -> slot == 0),
                "All tasks should be assigned to slot 0");
    }

    @Test
    @DisplayName("Should assign different slots to conflicting tasks")
    void testScheduleWithConflicts() {
        TaskRequest request = new TaskRequest();
        request.setNumberOfTasks(3);
        request.setSharedResources(new int[][]{{0, 1}, {1, 2}}); // Chain conflict

        ScheduleResponse response = schedulerService.schedule(request);

        assertEquals(2, response.getTotalColorsUsed(), "Should use at least 2 slots");
        assertEquals(3, response.getTaskAssignments().size());
        assertNotEquals(response.getTaskAssignments().get(0), response.getTaskAssignments().get(1));
        assertNotEquals(response.getTaskAssignments().get(1), response.getTaskAssignments().get(2));
    }

    @Test
    @DisplayName("Should throw exception for invalid shared resource indices")
    void testInvalidResourceIndex() {
        TaskRequest request = new TaskRequest();
        request.setNumberOfTasks(2);
        request.setSharedResources(new int[][]{{0, 2}}); // Invalid index 2

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> schedulerService.schedule(request),
                "Expected exception for invalid indices");

        assertTrue(exception.getMessage().contains("invalid task indices"));
    }

    @Test
    @DisplayName("Should skip invalid edges with less than 2 elements")
    void testEdgeWithInsufficientLength() {
        TaskRequest request = new TaskRequest();
        request.setNumberOfTasks(2);
        request.setSharedResources(new int[][]{{0}, {1, 0}});

        ScheduleResponse response = schedulerService.schedule(request);

        assertEquals(2, response.getTotalColorsUsed());
        assertEquals(2, response.getTaskAssignments().size());
        assertNotEquals(response.getTaskAssignments().get(0), response.getTaskAssignments().get(1));
    }

    @Test
    @DisplayName("Should handle a fully connected graph")
    void testFullyConnectedGraph() {
        int n = 4;
        TaskRequest request = new TaskRequest();
        request.setNumberOfTasks(n);

        // Build complete graph (each task conflicts with every other)
        int[][] edges = new int[n * (n - 1) / 2][2];
        int index = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                edges[index++] = new int[]{i, j};
            }
        }
        request.setSharedResources(edges);

        ScheduleResponse response = schedulerService.schedule(request);

        assertEquals(n, response.getTotalColorsUsed(), "Each task needs its own slot");
        for (int i = 0; i < n; i++) {
            assertTrue(response.getTaskAssignments().containsKey(i), "Task " + i + " should be present");
        }
    }
}
