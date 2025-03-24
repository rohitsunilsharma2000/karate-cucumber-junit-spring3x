package com.example.turingOnlineForumSystem.controller;


import com.example.turingOnlineForumSystem.model.User;
import com.example.turingOnlineForumSystem.service.FollowService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FollowController.class)
@Import(FollowControllerIntegrationTest.TestSecurityConfig.class)
class FollowControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FollowService followService;

    @Test
    void testFollowUser() throws Exception {
        mockMvc.perform(post("/api/follow")
                                .param("followerId", "1")
                                .param("followingId", "2"))
               .andExpect(status().isOk())
               .andExpect(content().string("Followed successfully"));

        Mockito.verify(followService, Mockito.times(1)).followUser(1L, 2L);
    }

    @Test
    void testGetFollowing() throws Exception {
        User user = User.builder().id(2L).username("bob").build();
        Mockito.when(followService.getFollowing(1L)).thenReturn(List.of(user));

        mockMvc.perform(get("/api/follow/1/following"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].username").value("bob"));
    }

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
