package com.example;

import com.intuit.karate.Results;
import com.intuit.karate.Runner;
import com.intuit.karate.junit5.Karate;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class KarateTestRunner {

//    @Test
    public void testPendingForMoreThan96Hours() {
        // Running Karate tests with the appropriate tag
        Results results = Runner.path("classpath:features/isTimeExceedThanLraRequired.feature")  // Path to the feature file
                                .outputCucumberJson(true)  // Output results in Cucumber JSON format
                                .tags("@EligibilityPendingMoreThan96Hours")  // Run tests with this tag
                                .parallel(1);  // Run tests in parallel (only 1 thread for simplicity)

        // Assert that there are no failures in the results
        assertEquals(0, results.getFailCount(), results.getErrorMessages());
    }

    @Test
    public void testPendingForLessThan96Hours() {
        // Running Karate tests with the appropriate tag
        Results results = Runner.path("classpath:features/isTimeExceedThanLraRequired.feature")  // Path to the feature file
                                .outputCucumberJson(true)  // Output results in Cucumber JSON format
//                                .tags("@EligibilityPendingLessThan96Hours")  // Run tests with this tag
                                .parallel(1);  // Run tests in parallel (only 1 thread for simplicity)

        // Assert that there are no failures in the results
        assertEquals(0, results.getFailCount(), results.getErrorMessages());
    }

//    @Test
    public void TestInputData() {
        // Running Karate tests with the appropriate tag
        Results results = Runner.path("classpath:features/isTimeExceedThanLraRequired.feature")  // Path to the feature file
                                .outputCucumberJson(true)  // Output results in Cucumber JSON format
                                .tags("@TestInputData")  // Run tests with this tag
                                .parallel(1);  // Run tests in parallel (only 1 thread for simplicity)

        // Assert that there are no failures in the results
        assertEquals(0, results.getFailCount(), results.getErrorMessages());
    }

    @Test
    public void test() {
        Results results = Runner.path("classpath:features/time.feature")
                                .outputCucumberJson(true)
//                                .tags("@EligibilityPendingLessThan96Hours")
                                .parallel(1);

        assertEquals(0, results.getFailCount(), results.getErrorMessages());
    }

}
