package com.example;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class TimeUtils {

    public static boolean isTimeExceedThanLraRequired(Instant currentTimeSTamp, Instant timeStamp, int delayTime) {
        if (timeStamp == null) {
            return false;
        }
        return currentTimeSTamp.isAfter(timeStamp.plus(delayTime, ChronoUnit.MINUTES));
    }


    public static void main(String[] args) {
        TimeUtils timeUtils = new TimeUtils();

        // Test Case 1
        Instant currentTime1 = Instant.parse("2025-03-10T09:00:01Z");
        Instant timeStamp1 = Instant.parse("2025-03-05T09:00:00Z"); // 5 days ago
        int delayTime1 = 96; // 96 minutes
        boolean result1 = timeUtils.isTimeExceedThanLraRequired(currentTime1, timeStamp1, delayTime1);
        System.out.println("Test Case 1 Result: " + result1); // Expected: true

        // Test Case 2
        Instant currentTime2 = Instant.parse("2025-03-10T10:00:00Z");
        Instant timeStamp2 = Instant.parse("2025-03-09T12:00:00Z"); // 22 hours ago
        int delayTime2 = 96; // 96 minutes
        boolean result2 = timeUtils.isTimeExceedThanLraRequired(currentTime2, timeStamp2, delayTime2);
        System.out.println("Test Case 2 Result: " + result2); // Expected: true
    }
}
