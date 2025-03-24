package com.example.turingOnlineForumSystem.controller;


import com.example.turingOnlineForumSystem.model.Threads;
import com.example.turingOnlineForumSystem.model.User;
import com.example.turingOnlineForumSystem.repository.ThreadRepository;
import com.example.turingOnlineForumSystem.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SearchController.class)
@Import(SearchControllerIntegrationTest.TestSecurityConfig.class)
class SearchControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepo;

    @MockBean
    private ThreadRepository threadRepo;

    @Test
    void testSearchUsers() throws Exception {
        User user = User.builder().id(1L).username("testuser").email("user@example.com").build();
        Mockito.when(userRepo.findByUsernameContainingIgnoreCase("test")).thenReturn(List.of(user));

        mockMvc.perform(get("/api/search/users")
                                .param("q", "test"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].username").value("testuser"));
    }

    @Test
    void testSearchThreads() throws Exception {
        Threads thread = Threads.builder().id(1L).title("Interesting Topic").content("Something cool").build();
        Mockito.when(threadRepo.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase("test", "test"))
               .thenReturn(List.of(thread));

        mockMvc.perform(get("/api/search/threads")
                                .param("q", "test"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].title").value("Interesting Topic"));
    }

    // ✅ Disable security just for tests
    @TestConfiguration
    static class TestSecurityConfig {
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http.csrf().disable()
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
            return http.build();
        }
    }
}
