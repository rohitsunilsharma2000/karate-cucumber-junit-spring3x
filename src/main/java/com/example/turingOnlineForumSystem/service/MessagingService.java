package com.example.turingOnlineForumSystem.service;


import com.example.turingOnlineForumSystem.dto.ChatMessageDTO;
import com.example.turingOnlineForumSystem.exception.ResourceNotFoundException;
import com.example.turingOnlineForumSystem.model.Message;
import com.example.turingOnlineForumSystem.model.User;
import com.example.turingOnlineForumSystem.repository.MessageRepository;
import com.example.turingOnlineForumSystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessagingService {

    private final MessageRepository messageRepo;
    private final UserRepository userRepo;
    private final NotificationService notificationService;

    /**
     * Persists and returns a message.
     */
    public Message sendMessage(ChatMessageDTO dto) {
        log.info("Sending message from {} to {}", dto.getSenderId(), dto.getReceiverId());

        User sender = userRepo.findById(dto.getSenderId())
                .orElseThrow(() -> new ResourceNotFoundException("Sender not found"));
        User receiver = userRepo.findById(dto.getReceiverId())
                .orElseThrow(() -> new ResourceNotFoundException("Receiver not found"));

        Message message = Message.builder()
                .content(dto.getContent())
                .sender(sender)
                .receiver(receiver)
                .build();

        Message saved = messageRepo.save(message);
        log.info("Message saved: ID {}", saved.getId());

        // 🔔 Create Notification
        notificationService.sendNotification(receiver, "📩 New message from " + sender.getUsername());

        return saved;
    }

    /**
     * Returns chat history between two users.
     */
    public List<Message> getChatHistory(Long user1Id, Long user2Id) {
        log.info("Fetching chat history between {} and {}", user1Id, user2Id);
        User u1 = userRepo.findById(user1Id)
                .orElseThrow(() -> new ResourceNotFoundException("User 1 not found"));
        User u2 = userRepo.findById(user2Id)
                .orElseThrow(() -> new ResourceNotFoundException("User 2 not found"));
        return messageRepo.findBySenderAndReceiver(u1, u2);
    }
}
