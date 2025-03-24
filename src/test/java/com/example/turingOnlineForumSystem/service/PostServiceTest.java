package com.example.turingOnlineForumSystem.service;


import com.example.turingOnlineForumSystem.dto.PostDto;
import com.example.turingOnlineForumSystem.exception.ResourceNotFoundException;
import com.example.turingOnlineForumSystem.model.Post;
import com.example.turingOnlineForumSystem.model.Threads;
import com.example.turingOnlineForumSystem.model.User;
import com.example.turingOnlineForumSystem.repository.PostRepository;
import com.example.turingOnlineForumSystem.repository.ThreadRepository;
import com.example.turingOnlineForumSystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostServiceTest {

    @Mock private PostRepository postRepository;
    @Mock private ThreadRepository threadRepository;
    @Mock private UserRepository userRepository;

    @InjectMocks private PostService postService;

    private Threads thread;
    private User user;
    private Post post;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = User.builder().id(1L).username("user1").build();
        thread = Threads.builder().id(1L).title("Thread Title").build();
        post = Post.builder().id(100L).content("Sample post").user(user).thread(thread).createdAt(LocalDateTime.now()).build();
    }

    @Test
    void testCreatePost_Success() {
        when(threadRepository.findById(1L)).thenReturn(Optional.of(thread));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(postRepository.save(any(Post.class))).thenReturn(post);

        Post inputPost = new Post();
        inputPost.setContent("Sample post");
        inputPost.setUser(user);

        Post saved = postService.createPost(inputPost, 1L);

        assertNotNull(saved);
        assertEquals("Sample post", saved.getContent());
        verify(postRepository).save(any(Post.class));
    }

    @Test
    void testCreatePost_ThreadNotFound() {
        when(threadRepository.findById(99L)).thenReturn(Optional.empty());

        Post inputPost = new Post();
        inputPost.setUser(user);
        inputPost.setContent("X");

        assertThrows(ResourceNotFoundException.class,
                     () -> postService.createPost(inputPost, 99L));
    }

    @Test
    void testCreatePost_UserNotFound() {
        when(threadRepository.findById(1L)).thenReturn(Optional.of(thread));
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        Post inputPost = new Post();
        inputPost.setContent("Hello");
        inputPost.setUser(User.builder().id(99L).build());

        assertThrows(ResourceNotFoundException.class,
                     () -> postService.createPost(inputPost, 1L));
    }

    @Test
    void testGetPostsByThread() {
        when(postRepository.findByThreadId(1L)).thenReturn(List.of(post));

        List<PostDto> dtos = postService.getPostsByThread(1L);

        assertEquals(1, dtos.size());
        assertEquals(post.getContent(), dtos.get(0).getContent());
    }

    @Test
    void testDeletePostsByThread() {
        when(postRepository.findByThreadId(1L)).thenReturn(List.of(post));

        postService.deletePostsByThread(1L);

        verify(postRepository).deleteAll(List.of(post));
    }
}
