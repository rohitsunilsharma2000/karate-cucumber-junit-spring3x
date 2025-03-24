package com.example.turingOnlineForumSystem.service;

import com.example.turingOnlineForumSystem.exception.ResourceNotFoundException;
import com.example.turingOnlineForumSystem.model.Notification;
import com.example.turingOnlineForumSystem.model.User;
import com.example.turingOnlineForumSystem.repository.NotificationRepository;
import com.example.turingOnlineForumSystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private NotificationService notificationService;

    private AutoCloseable closeable;

    @BeforeEach
    void setup() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendNotificationById_Success() {
        User user = User.builder().id(1L).username("bob").build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        notificationService.sendNotification(1L, "Test message");

        verify(notificationRepository, times(1)).save(argThat(notification ->
                                                                      notification.getRecipient().getId().equals(1L) &&
                                                                              notification.getMessage().equals("Test message")
        ));
    }

    @Test
    void testSendNotificationById_UserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> notificationService.sendNotification(99L, "Hello"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Recipient not found");
    }

    @Test
    void testMarkAsRead_Success() {
        Notification notification = Notification.builder().id(10L).isRead(false).build();
        when(notificationRepository.findById(10L)).thenReturn(Optional.of(notification));

        notificationService.markAsRead(10L);

        verify(notificationRepository).save(notification);
        assert notification.getIsRead();
    }

    @Test
    void testMarkAsRead_NotFound() {
        when(notificationRepository.findById(100L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> notificationService.markAsRead(100L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Notification not found");
    }

    @Test
    void testDeleteNotification_Success() {
        when(notificationRepository.existsById(5L)).thenReturn(true);

        notificationService.deleteNotification(5L);

        verify(notificationRepository).deleteById(5L);
    }

    @Test
    void testDeleteNotification_NotFound() {
        when(notificationRepository.existsById(50L)).thenReturn(false);

        assertThatThrownBy(() -> notificationService.deleteNotification(50L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Notification not found");
    }

    @Test
    void testSendNotificationWithUserObject() {
        User user = User.builder().id(2L).build();

        notificationService.sendNotification(user, "Direct notification");

        verify(notificationRepository).save(argThat(notification ->
                                                            notification.getMessage().equals("Direct notification") &&
                                                                    notification.getRecipient().getId().equals(2L)
        ));
    }
}
