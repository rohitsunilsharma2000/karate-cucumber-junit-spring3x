package com.example.turingOnlineForumSystem.controller;


import com.example.turingOnlineForumSystem.model.Notification;
import com.example.turingOnlineForumSystem.model.User;
import com.example.turingOnlineForumSystem.repository.NotificationRepository;
import com.example.turingOnlineForumSystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class NotificationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        user.setPassword("password");
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);

        Notification notification = Notification.builder()
                .message("Test Notification")
                .recipient(user)
                .isRead(false)
                .timestamp(LocalDateTime.now())
                .build();
        notificationRepository.save(notification);
    }

    @Test
    public void testGetUserNotifications() throws Exception {
        mockMvc.perform(get("/api/notifications/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].message").value("Test Notification"));
    }

    @Test
    public void testMarkNotificationAsRead() throws Exception {
        Notification notification = notificationRepository.findByRecipientId(user.getId()).get(0);
        mockMvc.perform(put("/api/notifications/read/" + notification.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteNotification() throws Exception {
        Notification notification = notificationRepository.findByRecipientId(user.getId()).get(0);
        mockMvc.perform(delete("/api/notifications/" + notification.getId()))
                .andExpect(status().isOk());
    }
}