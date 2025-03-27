package com.example.userqueryhub.exception;

/**
 * Custom exception thrown when a user resource is not found in the system.
 * <p>
 * This exception is used throughout the UserPurge application to indicate that a requested user
 * entity could not be located in the database. It extends {@link RuntimeException} to provide an unchecked exception,
 * simplifying error propagation in the service layer.
 * </p>
 *
 * <h3>Key Features:</h3>
 * <ul>
 *   <li><strong>Clear Messaging:</strong> Accepts a detailed message describing the resource that was not found.</li>
 *   <li><strong>Runtime Exception:</strong> Inherits from {@code RuntimeException} to avoid mandatory try-catch blocks,
 *       allowing the exception to be handled globally by a dedicated exception handler.</li>
 *   <li><strong>Integration with Global Exception Handling:</strong> Designed to work with the application's global exception handling framework,
 *       providing clear error responses to clients when a user is not found.</li>
 * </ul>
 *
 * <p>
 * Use this exception in scenarios where the user entity retrieval fails, ensuring that consumers of the service are
 * informed about the absence of the expected resource in a consistent manner.
 * </p>
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * Constructs a new {@code UserNotFoundException} with the specified detail message.
     *
     * @param message Detailed message about the user that was not found.
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}
