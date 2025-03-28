package com.example.colorsched.dto;

import lombok.Data;

import java.util.Map;

/**
 * Data Transfer Object (DTO) representing the response returned after scheduling tasks
 * using the graph coloring algorithm.
 *
 * <p><strong>Overview:</strong></p>
 * This response includes:
 * <ul>
 *   <li>A map of task IDs to their assigned execution slots (time slots/colors).</li>
 *   <li>The total number of distinct slots (colors) used in the scheduling process.</li>
 * </ul>
 *
 * <p><strong>Usage Context:</strong></p>
 * Returned by the {@code TaskSchedulerService} and exposed through the REST API to the client
 * as a response to the scheduling request.
 *
 * <p><strong>Pass/Fail Conditions:</strong></p>
 * <ul>
 *   <li><strong>Pass:</strong> Proper task-to-slot assignments with the minimum possible number of slots (colors).</li>
 *   <li><strong>Fail:</strong> Should never be returned empty unless no tasks were present in the request.</li>
 * </ul>
 */
@Data
public class ScheduleResponse {

    /**
     * A map of task IDs to their assigned execution time slots (colors).
     * Key: Task ID (Integer) <br>
     * Value: Assigned Slot (Time Slot / Color)
     *
     * <p>Used to indicate which task is scheduled in which time slot, such that no conflicting tasks share the same slot.</p>
     */
    private Map<Integer, Integer> taskAssignments;

    /**
     * Total number of distinct slots (colors) used to schedule all tasks.
     * <p>Represents the minimum number of parallel execution slots required given resource constraints.</p>
     */
    private int totalColorsUsed;
}
