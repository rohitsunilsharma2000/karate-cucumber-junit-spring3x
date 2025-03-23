package com.example.turingOnlineForumSystem.controller;


import com.example.turingOnlineForumSystem.model.Post;
import com.example.turingOnlineForumSystem.model.Threads;
import com.example.turingOnlineForumSystem.model.User;
import com.example.turingOnlineForumSystem.repository.PostRepository;
import com.example.turingOnlineForumSystem.repository.ThreadRepository;
import com.example.turingOnlineForumSystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ModerationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ThreadRepository threadRepository;

    @Autowired
    private PostRepository postRepository;

    private User moderator;
    private User regularUser;
    private Threads thread;
    private Post post;

    @BeforeEach
    void setup() {
        postRepository.deleteAll();
        threadRepository.deleteAll();
        userRepository.deleteAll();

        moderator = userRepository.save(User.builder().username("moderator").email("mod@example.com").banned(false).createdAt(LocalDateTime.now()).build());
        regularUser = userRepository.save(User.builder().username("user").email("user@example.com").banned(false).createdAt(LocalDateTime.now()).build());

        thread = threadRepository.save(Threads.builder()
                .title("Test Thread")
                .content("Thread Content")
                .createdAt(LocalDateTime.now())
                .user(regularUser)
                .posts(Collections.emptyList())
                .build());

        post = postRepository.save(Post.builder()
                .content("Test Post")
                .createdAt(LocalDateTime.now())
                .user(regularUser)
                .thread(thread)
                .build());
    }

    @Test
    void testDeletePost() throws Exception {
        mockMvc.perform(delete("/api/moderation/post/" + post.getId())
                        .param("moderatorId", moderator.getId().toString())
                        .param("reason", "Violation of rules"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteThread() throws Exception {
        mockMvc.perform(delete("/api/moderation/thread/" + thread.getId())
                        .param("moderatorId", moderator.getId().toString())
                        .param("reason", "Duplicate content"))
                .andExpect(status().isOk());
    }

    @Test
    void testBanUser() throws Exception {
        mockMvc.perform(post("/api/moderation/ban-user/" + regularUser.getId())
                        .param("reason", "Spamming"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetModerationHistory() throws Exception {
        mockMvc.perform(get("/api/moderation/history/" + regularUser.getId()))
                .andExpect(status().isOk());
    }
}


