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
 * Unit tests for {@link UserService} covering user save and retrieval by username.
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
 * @since 2025-03-27
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private UserService userService;

    private User user;

    /**
     * Initializes a sample user before each test.
     */
    @BeforeEach
    public void setUp() {
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
     * The test mocks the repository's {@code save} method to return the same user instance, and then asserts that the
     * returned user matches the input.
     * </p>
     */
    @Test
    public void testSaveUser() {
        when(userRepo.save(any(User.class))).thenReturn(user);

        User savedUser = userService.save(user);
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isEqualTo(1L);
        assertThat(savedUser.getUsername()).isEqualTo("testuser");
        assertThat(savedUser.getEmail()).isEqualTo("testuser@example.com");
    }

    /**
     * Tests that the {@code getUserByUsername} method retrieves an existing user.
     *
     * <p>
     * The test mocks the repository's {@code findByUsername} method to return a sample user when a valid username is provided.
     * The returned user is then validated for correctness.
     * </p>
     */
    @Test
    public void testGetUserByUsernameFound() {
        when(userRepo.findByUsername("testuser")).thenReturn(user);

        User foundUser = userService.getUserByUsername("testuser");
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUsername()).isEqualTo("testuser");
        assertThat(foundUser.getEmail()).isEqualTo("testuser@example.com");
    }

    /**
     * Tests that the {@code getUserByUsername} method throws a {@link UserNotFoundException}
     * when the specified username is not found.
     *
     * <p>
     * The test mocks the repository's {@code findByUsername} method to return {@code null} for a non-existing username,
     * and verifies that a {@link UserNotFoundException} is thrown.
     * </p>
     */
    @Test
    public void testGetUserByUsernameNotFound() {
        when(userRepo.findByUsername("nonexistent")).thenReturn(null);

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                                                       () -> userService.getUserByUsername("nonexistent"),
                                                       "Expected getUserByUsername to throw, but it didn't");

        assertThat(exception.getMessage()).isEqualTo("User with username 'nonexistent' not found");
    }
}
