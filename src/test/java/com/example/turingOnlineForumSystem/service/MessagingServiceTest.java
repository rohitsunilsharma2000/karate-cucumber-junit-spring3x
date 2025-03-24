package com.example.turingOnlineForumSystem.service;


import com.example.turingOnlineForumSystem.dto.ChatMessageDTO;
import com.example.turingOnlineForumSystem.exception.ResourceNotFoundException;
import com.example.turingOnlineForumSystem.model.Message;
import com.example.turingOnlineForumSystem.model.User;
import com.example.turingOnlineForumSystem.repository.MessageRepository;
import com.example.turingOnlineForumSystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MessagingServiceTest {

    @Mock
    private MessageRepository messageRepo;

    @Mock
    private UserRepository userRepo;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private MessagingService messagingService;

    private User sender;
    private User receiver;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sender = new User();
        sender.setId(1L);
        sender.setUsername("Alice");

        receiver = new User();
        receiver.setId(2L);
        receiver.setUsername("Bob");
    }

    @Test
    void testSendMessage_Success() {
        ChatMessageDTO dto = new ChatMessageDTO();
        dto.setSenderId(1L);
        dto.setReceiverId(2L);
        dto.setContent("Hello!");

        when(userRepo.findById(1L)).thenReturn(Optional.of(sender));
        when(userRepo.findById(2L)).thenReturn(Optional.of(receiver));

        Message saved = Message.builder()
                               .id(10L)
                               .content("Hello!")
                               .sender(sender)
                               .receiver(receiver)
                               .build();

        when(messageRepo.save(any(Message.class))).thenReturn(saved);

        Message result = messagingService.sendMessage(dto);

        assertNotNull(result);
        assertEquals("Hello!", result.getContent());
        assertEquals("Alice", result.getSender().getUsername());

        verify(notificationService).sendNotification(receiver, "📩 New message from Alice");
    }

    @Test
    void testSendMessage_SenderNotFound() {
        ChatMessageDTO dto = new ChatMessageDTO();
        dto.setSenderId(99L);
        dto.setReceiverId(2L);
        dto.setContent("Hi");

        when(userRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> messagingService.sendMessage(dto));
    }

    @Test
    void testGetChatHistory_ReturnsMessages() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(sender));
        when(userRepo.findById(2L)).thenReturn(Optional.of(receiver));

        Message msg = Message.builder()
                             .id(1L)
                             .content("Hey Bob")
                             .sender(sender)
                             .receiver(receiver)
                             .build();

        when(messageRepo.findBySenderAndReceiver(sender, receiver)).thenReturn(List.of(msg));

        List<Message> history = messagingService.getChatHistory(1L, 2L);

        assertEquals(1, history.size());
        assertEquals("Hey Bob", history.get(0).getContent());
    }

    @Test
    void testGetChatHistory_UserNotFound() {
        when(userRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> messagingService.getChatHistory(1L, 2L));
    }
}

