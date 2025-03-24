package com.example.turingOnlineForumSystem.service;

import com.example.turingOnlineForumSystem.dto.ModerationDTO;
import com.example.turingOnlineForumSystem.exception.ResourceNotFoundException;
import com.example.turingOnlineForumSystem.model.*;
import com.example.turingOnlineForumSystem.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ModerationServiceTest {

    @Mock private ModerationRepository moderationRepository;
    @Mock private ThreadRepository threadRepository;
    @Mock private PostRepository postRepository;
    @Mock private UserRepository userRepository;

    @InjectMocks private ModerationService moderationService;

    private User user;
    private Threads thread;
    private Post post;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = User.builder().id(1L).username("Alice").build();
        thread = Threads.builder().id(10L).title("Sample Thread").user(user).build();
        post = Post.builder().id(100L).content("Sample post").thread(thread).user(user).build();
    }

    @Test
    void testDeleteThread() {
        when(threadRepository.getReferenceById(10L)).thenReturn(thread);
        when(userRepository.getReferenceById(1L)).thenReturn(user);
        when(postRepository.findByThreadId(10L)).thenReturn(List.of(post));

        moderationService.deleteThread(10L, 99L, "Inappropriate content");

        verify(moderationRepository).save(any(Moderation.class));
        verify(postRepository).deleteAll(List.of(post));
        verify(threadRepository).deleteById(10L);
    }

    @Test
    void testDeletePost_Success() {
        when(postRepository.findById(100L)).thenReturn(Optional.of(post));

        moderationService.deletePost(100L, 88L, "Spam");

        verify(postRepository).deleteById(100L);
        verify(moderationRepository).save(any(Moderation.class));
    }

    @Test
    void testDeletePost_NotFound() {
        when(postRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                moderationService.deletePost(100L, 88L, "Spam"));
    }

    @Test
    void testBanUser_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        moderationService.banUser(1L, "Toxic behavior");

        assertTrue(user.getBanned());
        verify(userRepository).save(user);
        verify(moderationRepository).save(any(Moderation.class));
    }

    @Test
    void testBanUser_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                moderationService.banUser(1L, "Toxic behavior"));
    }

    @Test
    void testGetModerationHistory() {
        Moderation moderation = Moderation.builder()
                                          .id(1L)
                                          .action("DELETE_POST")
                                          .reason("Bad content")
                                          .createdAt(LocalDateTime.now())
                                          .user(user)
                                          .thread(thread)
                                          .build();

        when(moderationRepository.findByUserId(1L)).thenReturn(List.of(moderation));

        List<ModerationDTO> result = moderationService.getModerationHistory(1L);

        assertEquals(1, result.size());
        assertEquals("DELETE_POST", result.get(0).getAction());
        assertEquals(user.getId(), result.get(0).getUserId());
    }
}
