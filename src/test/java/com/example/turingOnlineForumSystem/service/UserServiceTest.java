package com.example.turingOnlineForumSystem.service;


import com.example.turingOnlineForumSystem.exception.ResourceNotFoundException;
import com.example.turingOnlineForumSystem.model.User;
import com.example.turingOnlineForumSystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private UserService userService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockUser = User.builder()
                       .id(1L)
                       .username("testuser")
                       .email("test@example.com")
                       .build();
    }

    @Test
    void testGetUserById_Success() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(mockUser));

        User result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userRepo).findById(1L);
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(99L));
    }

    @Test
    void testUpdateUserProfile_Success() {
        User updatedUser = User.builder().username("updated").email("updated@example.com").build();
        when(userRepo.findById(1L)).thenReturn(Optional.of(mockUser));
        when(userRepo.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.updateUserProfile(1L, updatedUser);

        assertNotNull(result);
        assertEquals("updated", result.getUsername());
        assertEquals("updated@example.com", result.getEmail());
        verify(userRepo).save(any(User.class));
    }

    @Test
    void testFindById() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(mockUser));

        Optional<User> result = userService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
    }

    @Test
    void testFindAll() {
        when(userRepo.findAll()).thenReturn(Arrays.asList(mockUser));

        List<User> result = userService.findAll();

        assertEquals(1, result.size());
    }

    @Test
    void testSaveUser() {
        when(userRepo.save(mockUser)).thenReturn(mockUser);

        User result = userService.save(mockUser);

        assertEquals("testuser", result.getUsername());
        verify(userRepo).save(mockUser);
    }
}
