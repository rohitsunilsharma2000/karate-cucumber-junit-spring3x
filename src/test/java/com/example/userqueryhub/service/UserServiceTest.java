package com.example.userqueryhub.service;

import com.example.userqueryhub.exception.UserNotFoundException;
import com.example.userqueryhub.model.User;
import com.example.userqueryhub.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link UserService} covering user saving and retrieval by username.
 *
 * <p>
 * This test class verifies the behavior of the {@code save} and {@code getUserByUsername} methods in the
 * {@code UserService}. The repository is mocked using Mockito to isolate the service logic.
 * </p>
 *
 * <h3>Test Cases:</h3>
 * <ul>
 *   <li><strong>testSaveUser:</strong> Verifies that a user is correctly saved and returned.</li>
 *   <li><strong>testGetUserByUsernameFound:</strong> Verifies that an existing user is retrieved successfully by username.</li>
 *   <li><strong>testGetUserByUsernameNotFound:</strong> Verifies that a {@link UserNotFoundException} is thrown when the user is not found.</li>
 * </ul>
 *
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepo; // Mocked repository to simulate data persistence

    @InjectMocks
    private UserService userService; // Service under test with injected mock dependencies

    private User user; // Sample user used for testing

    /**
     * Initializes a sample user before each test.
     */
    @BeforeEach
    public void setUp() {
        // GIVEN: Create a sample user with predefined properties
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        user.setPassword("securepassword");
    }

    /**
     * Tests that the {@code save} method persists a user correctly.
     *
     * <p>
     * <strong>GIVEN:</strong> A valid {@link User} instance.
     * <br>
     * <strong>WHEN:</strong> The {@code save} method of {@link UserService} is called.
     * <br>
     * <strong>THEN:</strong> The returned user should match the input with an assigned ID.
     * </p>
     */
    @Test
    public void testSaveUser() {
        // WHEN: Simulate the repository saving the user by returning the same user instance.
        when(userRepo.save(any(User.class))).thenReturn(user);

        // Execute the service's save method.
        User savedUser = userService.save(user);

        // THEN: Verify that the saved user is not null and all properties match.
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isEqualTo(1L);
        assertThat(savedUser.getUsername()).isEqualTo("testuser");
        assertThat(savedUser.getEmail()).isEqualTo("testuser@example.com");
    }

    /**
     * Tests that the {@code getUserByUsername} method retrieves an existing user.
     *
     * <p>
     * <strong>GIVEN:</strong> A username for which a user exists.
     * <br>
     * <strong>WHEN:</strong> The {@code getUserByUsername} method is called with that username.
     * <br>
     * <strong>THEN:</strong> The service should return the correct {@link User} instance.
     * </p>
     */
    @Test
    public void testGetUserByUsernameFound() {
        // GIVEN: Configure the repository mock to return the sample user when queried by username.
        when(userRepo.findByUsername("testuser")).thenReturn(user);

        // WHEN: Retrieve the user by username using the service.
        User foundUser = userService.getUserByUsername("testuser");

        // THEN: Assert that the retrieved user is not null and matches expected values.
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUsername()).isEqualTo("testuser");
        assertThat(foundUser.getEmail()).isEqualTo("testuser@example.com");
    }

    /**
     * Tests that the {@code getUserByUsername} method throws a {@link UserNotFoundException}
     * when the specified username is not found.
     *
     * <p>
     * <strong>GIVEN:</strong> A non-existing username.
     * <br>
     * <strong>WHEN:</strong> The {@code getUserByUsername} method is called with that username.
     * <br>
     * <strong>THEN:</strong> A {@link UserNotFoundException} should be thrown with an appropriate message.
     * </p>
     */
    @Test
    public void testGetUserByUsernameNotFound() {
        // GIVEN: Configure the repository mock to return null for a non-existent username.
        when(userRepo.findByUsername("nonexistent")).thenReturn(null);

        // WHEN & THEN: Assert that calling getUserByUsername throws a UserNotFoundException.
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.getUserByUsername("nonexistent"),
                "Expected getUserByUsername to throw, but it didn't"
        );

        // Verify that the exception message is as expected.
        assertThat(exception.getMessage()).isEqualTo("User with username 'nonexistent' not found");
    }
}
