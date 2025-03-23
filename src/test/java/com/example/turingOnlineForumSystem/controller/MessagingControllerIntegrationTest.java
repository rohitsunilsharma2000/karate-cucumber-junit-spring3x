package com.example.turingOnlineForumSystem.controller;


import com.example.turingOnlineForumSystem.model.Message;
import com.example.turingOnlineForumSystem.model.User;
import com.example.turingOnlineForumSystem.repository.MessageRepository;
import com.example.turingOnlineForumSystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Nested
@SpringBootTest
@AutoConfigureMockMvc
class MessagingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    private User sender;
    private User receiver;

    @BeforeEach
    public void setup() {
        messageRepository.deleteAll();
        userRepository.deleteAll();

        sender = User.builder().username("sender").email("sender@example.com").password("pass").createdAt(LocalDateTime.now()).build();
        receiver = User.builder().username("receiver").email("receiver@example.com").password("pass").createdAt(LocalDateTime.now()).build();

        sender = userRepository.save(sender);
        receiver = userRepository.save(receiver);

        Message message = Message.builder()
                .sender(sender)
                .receiver(receiver)
                .content("Hello!")
                .timestamp(LocalDateTime.now())
                .build();

        messageRepository.save(message);
    }

    @Test
    public void testGetMessageHistory() throws Exception {
        mockMvc.perform(get("/api/messages/history")
                        .param("senderId", sender.getId().toString())
                        .param("receiverId", receiver.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value("Hello!"));
    }
}


