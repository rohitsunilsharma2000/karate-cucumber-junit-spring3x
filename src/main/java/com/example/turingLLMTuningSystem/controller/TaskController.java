package com.example.turingLLMTuningSystem.controller;

import com.example.turingLLMTuningSystem.dto.ScheduleResponse;
import com.example.turingLLMTuningSystem.dto.TaskRequest;
import com.example.turingLLMTuningSystem.service.TaskSchedulerService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for handling task scheduling in the Turing LLM Tuning System.
 *
 * <p><strong>Overview:</strong></p>
 * This controller exposes an endpoint to receive a list of tasks from the client and schedules them
 * using a graph coloring-based algorithm to minimize conflicts due to shared resources.
 *
 * <p><strong>Functionality:</strong></p>
 * <ul>
 *   <li>Accepts a {@link TaskRequest} with task details like task ID, duration, and resource dependencies.</li>
 *   <li>Schedules tasks optimally to avoid conflicts and returns a {@link ScheduleResponse} with execution slots.</li>
 * </ul>
 *
 * <p><strong>Pass/Fail Conditions:</strong></p>
 * <ul>
 *   <li><strong>Pass:</strong> Returns an optimal task schedule with HTTP 200 status.</li>
 *   <li><strong>Fail:</strong> Returns appropriate validation or internal server error responses if input is invalid or scheduling fails.</li>
 * </ul>
 *
 * <p><strong>Premises:</strong></p>
 * <ul>
 *   <li>Task scheduling is based on shared resource constraints.</li>
 *   <li>TaskSchedulerService handles the business logic and graph operations.</li>
 * </ul>
 */
@RestController
@RequestMapping("/tasks")
@Slf4j
public class TaskController {

    private final TaskSchedulerService taskSchedulerService;

    /**
     * Constructor for dependency injection of {@link TaskSchedulerService}.
     *
     * @param taskSchedulerService Service responsible for scheduling tasks based on resource constraints.
     */
    @Autowired
    public TaskController(TaskSchedulerService taskSchedulerService) {
        this.taskSchedulerService = taskSchedulerService;
    }

    /**
     * Schedules a list of tasks using the graph-coloring algorithm.
     *
     * <p><strong>Description:</strong></p>
     * Receives a JSON request containing task details and resource dependencies. This data is passed to
     * the {@link TaskSchedulerService} which computes an optimal execution order to minimize resource conflicts.
     *
     * <p><strong>Acceptable Values / Constraints:</strong></p>
     * <ul>
     *   <li>Each task in {@link TaskRequest} must have a unique ID and valid resource IDs.</li>
     *   <li>The number of tasks and constraints should be manageable by the server.</li>
     * </ul>
     *
     * <p><strong>Pass/Fail Conditions:</strong></p>
     * <ul>
     *   <li><strong>Pass:</strong> Returns the optimal schedule with status 200.</li>
     *   <li><strong>Fail:</strong> Returns 400 if input is invalid, 500 if internal logic fails.</li>
     * </ul>
     *
     * <p><strong>Logging:</strong></p>
     * Logs are added for incoming requests, task count, scheduling result, and errors.
     *
     * @param request The {@link TaskRequest} containing tasks to be scheduled.
     * @return {@link ScheduleResponse} with scheduling details and execution slots.
     */
    @PostMapping("/schedule")
    public ScheduleResponse scheduleTasks(@Valid @RequestBody TaskRequest request) {
        log.info("Received task scheduling request with {} tasks", request.getNumberOfTasks());

        try {
            // Delegate the scheduling logic to the service layer
            ScheduleResponse response = taskSchedulerService.schedule(request);

            log.info("Task scheduling successful. Number of slots scheduled: {}", response.getTaskAssignments().size());
            return response;
        } catch (Exception ex) {
            // Log the error and rethrow (or handle via @ControllerAdvice for global error handling)
            log.error("Task scheduling failed: {}", ex.getMessage(), ex);
            throw ex;
        }
    }
}
