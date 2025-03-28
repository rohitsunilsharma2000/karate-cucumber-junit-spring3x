package com.example.turingLLMTuningSystem.service;

import com.example.turingLLMTuningSystem.dto.ScheduleResponse;
import com.example.turingLLMTuningSystem.dto.TaskRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service implementation that schedules tasks using greedy graph coloring to minimize resource conflicts.
 *
 * <p>Each task is treated as a node in an undirected graph where edges represent shared resources (conflicts).
 * A color (time slot) is assigned to each task such that no two connected tasks (sharing resources) have the same slot.</p>
 */
@Service
@Slf4j
public class TaskSchedulerServiceImpl implements TaskSchedulerService {

    /**
     * Assigns time slots to tasks using greedy graph coloring algorithm to avoid resource conflicts.
     *
     * @param request The task scheduling request containing number of tasks and resource conflicts
     * @return The schedule response with slot assignments and total number of slots used
     */
    @Override
    public ScheduleResponse schedule(TaskRequest request) {
        int numberOfTasks = request.getNumberOfTasks();
        int[][] sharedResources = request.getSharedResources();

        log.info("Received TaskRequest: numberOfTasks={}, sharedResources.length={}",
                numberOfTasks, sharedResources.length);

        // Step 1: Initialize an adjacency list representation of the graph
        log.debug("Initializing empty adjacency list for all tasks");
        List<List<Integer>> adjacencyList = new ArrayList<>();
        for (int i = 0; i < numberOfTasks; i++) {
            adjacencyList.add(new ArrayList<>());
        }

        // Step 2: Build the graph based on shared resources
        log.debug("Constructing the conflict graph from shared resource matrix");
        for (int[] edge : sharedResources) {
            if (edge.length < 2) {
                log.warn("Skipping invalid edge with less than 2 elements: {}", Arrays.toString(edge));
                continue;
            }

            int u = edge[0];
            int v = edge[1];

            // Validate indices are within bounds
            if (u < 0 || v < 0 || u >= numberOfTasks || v >= numberOfTasks) {
                log.error("Invalid edge indices: u={}, v={}", u, v);
                throw new IllegalArgumentException("Shared resource refers to invalid task indices");
            }

            // Add edge to both u and v
            adjacencyList.get(u).add(v);
            adjacencyList.get(v).add(u);

            log.debug("Added edge between task {} and task {}", u, v);
        }

        // Step 3: Initialize arrays for slot assignment and availability tracking
        int[] assignedSlots = new int[numberOfTasks]; // Slot assigned to each task
        Arrays.fill(assignedSlots, -1); // Initially, no slots assigned

        boolean[] slotAvailability = new boolean[numberOfTasks]; // Tracks which slots are taken during iteration

        // Always assign first task to slot 0
        assignedSlots[0] = 0;
        log.debug("Assigned slot 0 to task 0");

        // Step 4: Assign slots to the rest of the tasks
        for (int task = 1; task < numberOfTasks; task++) {
            log.debug("Attempting to schedule task {}", task);

            // Step 4.1: Mark all slots used by adjacent tasks as unavailable
            for (int neighbor : adjacencyList.get(task)) {
                int assigned = assignedSlots[neighbor];
                if (assigned != -1) {
                    slotAvailability[assigned] = true;
                    log.trace("Slot {} is unavailable due to neighbor task {}", assigned, neighbor);
                }
            }

            // Step 4.2: Find the first available slot (greedy approach)
            int chosenSlot;
            for (chosenSlot = 0; chosenSlot < numberOfTasks; chosenSlot++) {
                if (!slotAvailability[chosenSlot]) {
                    break;
                }
            }

            // Step 4.3: Assign the selected slot
            assignedSlots[task] = chosenSlot;
            log.debug("Assigned slot {} to task {}", chosenSlot, task);

            // Step 4.4: Reset slot availability for the next task
            for (int neighbor : adjacencyList.get(task)) {
                int assigned = assignedSlots[neighbor];
                if (assigned != -1) {
                    slotAvailability[assigned] = false;
                }
            }
        }

        // Step 5: Build the final response with assignments and slot count
        Map<Integer, Integer> assignmentMap = new HashMap<>();
        int maxSlotUsed = 0;

        for (int i = 0; i < numberOfTasks; i++) {
            assignmentMap.put(i, assignedSlots[i]);
            maxSlotUsed = Math.max(maxSlotUsed, assignedSlots[i]);

            log.info("Final assignment: Task {} -> Slot {}", i, assignedSlots[i]);
        }

        log.info("Total distinct slots used: {}", maxSlotUsed + 1);

        // Step 6: Construct the response object
        ScheduleResponse response = new ScheduleResponse();
        response.setTaskAssignments(assignmentMap);
        response.setTotalColorsUsed(maxSlotUsed + 1); // +1 because slots are zero-indexed

        log.debug("Returning ScheduleResponse: taskAssignments={}, totalColorsUsed={}",
                response.getTaskAssignments(), response.getTotalColorsUsed());

        return response;
    }
}
