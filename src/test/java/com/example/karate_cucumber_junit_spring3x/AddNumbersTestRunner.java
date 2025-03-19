package com.example.karate_cucumber_junit_spring3x;

import com.intuit.karate.Results;
import com.intuit.karate.Runner;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddNumbersTestRunner {

    @Test
    public void testAddNumbers() {
        // Running Karate tests with specific parameters
        Results results = Runner.path("classpath:features/add_numbers.feature")  // Path to the feature file
                                .outputCucumberJson(true)  // Output results in Cucumber JSON format
                                .tags("@AddNumbersTest")  // Only run scenarios with this tag
                                .parallel(1);  // Run tests in parallel (only 1 thread for simplicity)

        // Assert that there are no failures in the results
        assertEquals(0, results.getFailCount(), results.getErrorMessages());
    }
}
