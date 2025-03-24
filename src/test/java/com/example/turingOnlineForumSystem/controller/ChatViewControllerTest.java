package com.example.turingOnlineForumSystem.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChatViewController.class)
@Import(ChatViewControllerTest.TestSecurityConfig.class)
public class ChatViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testChatPageLoadsWithUserId() throws Exception {
        mockMvc.perform(get("/chat")
                                .param("userId", "123"))
               .andExpect(status().isOk())
               .andExpect(view().name("chat"))
               .andExpect(model().attribute("userId", 123L));
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
