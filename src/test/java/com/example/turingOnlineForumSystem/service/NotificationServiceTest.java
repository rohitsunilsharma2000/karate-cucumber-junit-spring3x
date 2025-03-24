package com.example.turingOnlineForumSystem.service;


import com.example.turingOnlineForumSystem.exception.ResourceNotFoundException;
import com.example.turingOnlineForumSystem.model.Notification;
import com.example.turingOnlineForumSystem.model.User;
import com.example.turingOnlineForumSystem.repository.NotificationRepository;
import com.example.turingOnlineForumSystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationServiceTest {

    @Mock private NotificationRepository notificationRepo;
    @Mock private NotificationRepository notificationRepository;
    @Mock private UserRepository userRepository;

    @InjectMocks private NotificationService notificationService;

    private User user;
    private Notification notification;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = User.builder().id(1L).username("testUser").build();
        notification = Notification.builder()
                                   .id(10L)
                                   .recipient(user)
                                   .message("Test Message")
                                   .isRead(false)
                                   .build();
    }

//    @Test
    void sendNotification_ByUserId_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        notificationService.sendNotification(1L, "Hello!");

        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    void sendNotification_ByUserId_ThrowsIfUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                notificationService.sendNotification(1L, "Hello"));
    }

    //@Test
    void sendNotification_ByUserObject_Success() {
        notificationService.sendNotification(user, "Welcome");
        verify(notificationRepo).save(any(Notification.class));
    }

   // @Test
    void markAsRead_Success() {
        when(notificationRepository.findById(10L)).thenReturn(Optional.of(notification));

        notificationService.markAsRead(10L);

        assertTrue(notification.getIsRead());
        verify(notificationRepository).save(notification);
    }

    @Test
    void markAsRead_ThrowsIfNotFound() {
        when(notificationRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                notificationService.markAsRead(10L));
    }

    //@Test
    void deleteNotification_Success() {
        when(notificationRepository.existsById(10L)).thenReturn(true);

        notificationService.deleteNotification(10L);

        verify(notificationRepository).deleteById(10L);
    }

    @Test
    void deleteNotification_NotFound() {
        when(notificationRepository.existsById(10L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () ->
                notificationService.deleteNotification(10L));
    }

    //@Test
    void getNotificationsForUser() {
        when(notificationRepository.findByRecipientId(1L)).thenReturn(List.of(notification));

        List<Notification> result = notificationService.getNotificationsForUser(1L);

        assertEquals(1, result.size());
        assertEquals("Test Message", result.get(0).getMessage());
    }
}
