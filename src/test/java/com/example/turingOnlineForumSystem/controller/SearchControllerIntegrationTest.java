package com.example.turingOnlineForumSystem.controller;


import com.example.turingOnlineForumSystem.model.Threads;
import com.example.turingOnlineForumSystem.model.User;
import com.example.turingOnlineForumSystem.repository.ThreadRepository;
import com.example.turingOnlineForumSystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SearchControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ThreadRepository threadRepository;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
        threadRepository.deleteAll();

        User user = new User();
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        user.setPassword("password");
        userRepository.save(user);

        Threads thread = new Threads();
        thread.setTitle("Interesting Topic");
        thread.setContent("This thread is all about testing");
        threadRepository.save(thread);
    }

    @Test
    public void testSearchUsers() throws Exception {
        mockMvc.perform(get("/api/search/users")
                        .param("q", "test")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].username", containsString("testuser")));
    }

    @Test
    public void testSearchThreads() throws Exception {
        mockMvc.perform(get("/api/search/threads")
                        .param("q", "testing")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", containsString("Interesting")));
    }
}