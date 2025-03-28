package com.example.userqueryhub.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link UserNotFoundException}.
 *
 * <p>
 * This class verifies that the custom {@code UserNotFoundException} correctly propagates
 * the error message passed during instantiation to its superclass {@link RuntimeException}.
 * </p>
 *
 * <h3>Key Aspects:</h3>
 * <ul>
 *   <li><strong>Message Verification:</strong> Ensures that the exception is created with the expected error message.</li>
 *   <li><strong>Error Propagation:</strong> Confirms that the message is properly returned by {@link RuntimeException#getMessage()}.</li>
 * </ul>
 *
 * @since 2025-03-27
 */
public class UserNotFoundExceptionTest {

    /**
     * Verifies that the exception message is correctly passed and retrieved.
     *
     * <p>
     * <strong>GIVEN:</strong> An error message string indicating that a user was not found.
     * <br>
     * <strong>WHEN:</strong> A {@code UserNotFoundException} is instantiated with the message.
     * <br>
     * <strong>THEN:</strong> The exception's {@code getMessage()} method should return the expected message.
     * </p>
     */
    @Test
    void testUserNotFoundExceptionMessage() {
        // GIVEN: Define the expected error message.
        String expectedMessage = "User not found with id 123";

        // WHEN: Instantiate the UserNotFoundException with the expected message.
        UserNotFoundException exception = new UserNotFoundException(expectedMessage);

        // THEN: Assert that the message returned by getMessage() matches the expected message.
        assertEquals(expectedMessage, exception.getMessage(),
                     "The exception message should match the expected message.");
    }
}
