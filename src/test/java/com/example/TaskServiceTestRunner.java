package com.example;

import com.intuit.karate.Results;
import com.intuit.karate.Runner;
import org.apache.http.protocol.HTTP;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskServiceTestRunner {


    //@Test
    public void testNoTasksToProcess() {
        // Create an instance of the TaskService class
        com.example.TaskService taskService = new com.example.TaskService();

        // Invoke the TaskService method with no tasks
        java.util.Optional<java.util.List<com.example.RamsTask>> optionalTaskList = java.util.Optional.of(java.util.Collections.emptyList());

        // Invoke the TaskService method
        taskService.processFailedTasksService(optionalTaskList);

        // Running Karate tests for BDD validation (as an alternative)
        Results results = Runner.path("classpath:features/TaskService.feature")
                                .outputCucumberJson(true)
                                .tags("@NoTasks")
                                .parallel(1);

        // Assert that there are no failures in the results
        assertEquals(0, results.getFailCount(), results.getErrorMessages());
    }

    @Test
    public void testPendingForMoreThan96Hours() {
        // Running Karate tests with the appropriate tag
        Results results = Runner.path("classpath:features/PendingForMoreOrLessThan96Hours.feature")  // Path to the feature file
                                .outputCucumberJson(true)  // Output results in Cucumber JSON format
                                .tags("@PendingForMoreThan96Hours")  // Run tests with this tag
                                .parallel(1);  // Run tests in parallel (only 1 thread for simplicity)

        // Assert that there are no failures in the results
        assertEquals(0, results.getFailCount(), results.getErrorMessages());
    }

    @Test
    public void testPendingForLessThan96Hours() {
        // Running Karate tests with the appropriate tag
        Results results = Runner.path("classpath:features/PendingForMoreOrLessThan96Hours.feature")  // Path to the feature file
                                .outputCucumberJson(true)  // Output results in Cucumber JSON format
                                .tags("@PendingForLessThan96Hours")  // Run tests with this tag
                                .parallel(1);  // Run tests in parallel (only 1 thread for simplicity)

        // Assert that there are no failures in the results
        assertEquals(0, results.getFailCount(), results.getErrorMessages());
    }
}
