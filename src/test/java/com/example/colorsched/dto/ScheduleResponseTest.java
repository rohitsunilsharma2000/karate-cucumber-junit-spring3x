package com.example.colorsched.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link ScheduleResponse} DTO.
 *
 * <p><strong>Overview:</strong>
 * This test class verifies the behavior of the ScheduleResponse DTO including
 * proper setting and retrieval of task assignments and the total number of colors used.
 *
 * <p><strong>Covered Scenarios:</strong></p>
 * <ul>
 *   <li>Successful assignment and retrieval of task-to-slot mappings.</li>
 *   <li>Correct setting of total colors used in scheduling.</li>
 *   <li>Edge cases like empty map assignments and zero color usage.</li>
 * </ul>
 *
 * <p><strong>Pass/Fail Conditions:</strong>
 * <ul>
 *   <li><strong>Pass:</strong> Field values are accurately stored and retrieved.</li>
 *   <li><strong>Fail:</strong> Mismatched assignments, null values, or incorrect total colors.</li>
 * </ul>
 */
@SpringBootTest
class ScheduleResponseTest {

    /**
     * Test to verify correct mapping of tasks to slots in the task assignments map.
     */
    @Test
    @DisplayName("Unit Test: Should correctly store and return task assignments")
    void testTaskAssignmentsMapping() {
        // Arrange: Create a new ScheduleResponse object and a map of task assignments
        ScheduleResponse response = new ScheduleResponse();
        Map<Integer, Integer> assignments = new HashMap<>();
        assignments.put(0, 0);  // Task 0 is assigned to slot 0
        assignments.put(1, 1);  // Task 1 is assigned to slot 1
        assignments.put(2, 0);  // Task 2 is assigned to slot 0

        // Act: Set the task assignments in the ScheduleResponse object
        response.setTaskAssignments(assignments);

        // Assert: Verify the task assignments are correctly set
        assertNotNull(response.getTaskAssignments(), "Task assignments map should not be null");
        assertEquals(3, response.getTaskAssignments().size(), "Map size should match the number of tasks assigned");
        assertEquals(0, response.getTaskAssignments().get(0), "Task 0 should be assigned to slot 0");
        assertEquals(1, response.getTaskAssignments().get(1), "Task 1 should be assigned to slot 1");
        assertEquals(0, response.getTaskAssignments().get(2), "Task 2 should be assigned to slot 0");
    }

    /**
     * Test to verify that the total number of colors used is correctly tracked.
     */
    @Test
    @DisplayName("Unit Test: Should correctly track total number of colors used")
    void testTotalColorsUsed() {
        // Arrange: Create a new ScheduleResponse object and set the expected number of colors
        ScheduleResponse response = new ScheduleResponse();
        int expectedColors = 2;

        // Act: Set the total colors used in the ScheduleResponse object
        response.setTotalColorsUsed(expectedColors);

        // Assert: Verify that the total colors used are correctly set
        assertEquals(expectedColors, response.getTotalColorsUsed(), "Total colors used should match the value set");
    }

    /**
     * Test to verify correct behavior when no task assignments are provided (empty map)
     * and zero colors are used.
     */
    @Test
    @DisplayName("Unit Test: Should handle empty assignments and zero colors")
    void testEmptyAssignments() {
        // Arrange: Create a new ScheduleResponse object
        ScheduleResponse response = new ScheduleResponse();

        // Act: Set empty task assignments and zero total colors used
        response.setTaskAssignments(new HashMap<>());  // No tasks assigned
        response.setTotalColorsUsed(0);  // No colors used

        // Assert: Verify that the task assignments are initialized as empty and total colors are zero
        assertNotNull(response.getTaskAssignments(), "Task assignments should be initialized and not null");
        assertTrue(response.getTaskAssignments().isEmpty(), "Task assignments map should be empty");
        assertEquals(0, response.getTotalColorsUsed(), "Total colors used should be zero for empty input");
    }
}
