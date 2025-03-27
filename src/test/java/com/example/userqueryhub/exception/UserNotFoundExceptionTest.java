package com.example.userqueryhub.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link UserNotFoundException}.
 * <p>
 * This class tests the functionality of the custom {@code UserNotFoundException} to ensure that
 * the exception message is correctly passed and can be retrieved via {@link RuntimeException#getMessage()}.
 * </p>
 *
 * <h3>Key Aspects:</h3>
 * <ul>
 *   <li><strong>Message Verification:</strong> Confirms that the exception is instantiated with the expected message.</li>
 *   <li><strong>Error Propagation:</strong> Validates that the message provided during construction is properly propagated to the superclass.</li>
 * </ul>
 */
public class UserNotFoundExceptionTest {

    /**
     * Verifies that the exception message is correctly passed to the {@link RuntimeException}.
     */
    @Test
    void testUserNotFoundExceptionMessage() {
        String expectedMessage = "User not found with id 123";
        UserNotFoundException exception = new UserNotFoundException(expectedMessage);
        assertEquals(expectedMessage, exception.getMessage(), "The exception message should match the expected message.");
    }
}
