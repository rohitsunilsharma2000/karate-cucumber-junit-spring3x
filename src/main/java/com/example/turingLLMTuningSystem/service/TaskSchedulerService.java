package com.example.turingLLMTuningSystem.service;

import com.example.turingLLMTuningSystem.dto.ScheduleResponse;
import com.example.turingLLMTuningSystem.dto.TaskRequest;

/**
 * Service interface for scheduling tasks based on shared resource constraints.
 *
 * <p><strong>Overview:</strong></p>
 * Provides a contract for task scheduling logic, where tasks must be assigned
 * to timeslots such that no two tasks that share a resource are scheduled simultaneously.
 *
 * <p><strong>Implementation Note:</strong></p>
 * A typical implementation uses a graph coloring algorithm to represent tasks and their conflicts.
 * Each task is a node, and an edge between two nodes indicates a shared resource.
 *
 * <p><strong>Expected Behavior:</strong></p>
 * <ul>
 *   <li>Constructs a conflict graph based on the sharedResources matrix.</li>
 *   <li>Assigns time slots (colors) such that no two connected tasks share the same slot.</li>
 *   <li>Returns the final slot assignments and the total number of slots (colors) used.</li>
 * </ul>
 */
public interface TaskSchedulerService {

    /**
     * Schedules tasks using a resource-aware conflict resolution strategy (e.g., graph coloring).
     *
     * @param request A {@link TaskRequest} containing the number of tasks and the sharedResources matrix.
     * @return A {@link ScheduleResponse} containing the task-to-slot mapping and the total number of slots used.
     *
     * <p><strong>Validation Assumptions:</strong></p>
     * <ul>
     *   <li>The input request must be validated before invocation (e.g., using {@code @Valid} in controller).</li>
     *   <li>The sharedResources matrix should be square and match the number of tasks.</li>
     * </ul>
     */
    ScheduleResponse schedule(TaskRequest request);
}
