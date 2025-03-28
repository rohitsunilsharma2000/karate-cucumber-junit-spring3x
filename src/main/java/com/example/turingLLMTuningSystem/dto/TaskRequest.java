package com.example.turingLLMTuningSystem.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Data Transfer Object (DTO) representing the client request to schedule tasks
 * in the Turing LLM Tuning System.
 *
 * <p><strong>Overview:</strong></p>
 * This request includes:
 * <ul>
 *   <li>Total number of tasks that need to be scheduled.</li>
 *   <li>A matrix indicating shared resource relationships between tasks.</li>
 * </ul>
 *
 * <p><strong>Usage Context:</strong></p>
 * Consumed by the {@code TaskSchedulerService} to construct a task conflict graph and
 * apply graph coloring to determine optimal execution slots.
 *
 * <p><strong>Validation Rules:</strong></p>
 * <ul>
 *   <li>{@code numberOfTasks} must be at least 1.</li>
 *   <li>{@code sharedResources} must not be null. It should be a square matrix representing
 *       mutual exclusivity between tasks based on shared resources.</li>
 * </ul>
 *
 * <p><strong>Example:</strong></p>
 * For 4 tasks, a 2D matrix such as:
 * <pre>
 * sharedResources = {
 *     {0, 1, 0, 0},
 *     {1, 0, 1, 0},
 *     {0, 1, 0, 1},
 *     {0, 0, 1, 0}
 * };
 * </pre>
 * implies that:
 * <ul>
 *   <li>Task 0 shares a resource with Task 1.</li>
 *   <li>Task 1 shares a resource with Task 0 and Task 2.</li>
 *   <li>Task 2 shares with Task 1 and Task 3, and so on.</li>
 * </ul>
 */
@Data
public class TaskRequest {

    /**
     * Total number of tasks to be scheduled.
     * Must be at least 1.
     */
    @Min(value = 1, message = "There must be at least one task.")
    private int numberOfTasks;

    /**
     * 2D matrix representing shared resources between tasks.
     *
     * <p>Each row i and column j indicate whether task i and task j
     * share a resource (1 = yes, 0 = no).</p>
     * <p>Must not be null. Should be symmetric and of size {@code numberOfTasks x numberOfTasks}.</p>
     */
    @NotNull(message = "Shared resources list must not be null.")
    private int[][] sharedResources;
}
