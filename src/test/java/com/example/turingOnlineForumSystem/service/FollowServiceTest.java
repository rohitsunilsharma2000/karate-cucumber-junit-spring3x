package com.example.turingOnlineForumSystem.service;


import com.example.turingOnlineForumSystem.model.Follow;
import com.example.turingOnlineForumSystem.model.User;
import com.example.turingOnlineForumSystem.repository.FollowRepository;
import com.example.turingOnlineForumSystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FollowServiceTest {

    @Mock
    private FollowRepository followRepo;

    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private FollowService followService;

    private User follower;
    private User following;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        follower = new User();
        follower.setId(1L);
        follower.setUsername("user1");

        following = new User();
        following.setId(2L);
        following.setUsername("user2");
    }

    @Test
    void testFollowUser_Success() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(follower));
        when(userRepo.findById(2L)).thenReturn(Optional.of(following));

        followService.followUser(1L, 2L);

        verify(followRepo, times(1)).save(any(Follow.class));
    }

    @Test
    void testGetFollowing_ReturnsListOfUsers() {
        Follow follow = new Follow(1L, follower, following);
        when(followRepo.findByFollowerId(1L)).thenReturn(List.of(follow));

        List<User> result = followService.getFollowing(1L);

        assertEquals(1, result.size());
    }

    @Test
    void testFollowUser_UserNotFound() {
        when(userRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> followService.followUser(1L, 2L));
    }
}
