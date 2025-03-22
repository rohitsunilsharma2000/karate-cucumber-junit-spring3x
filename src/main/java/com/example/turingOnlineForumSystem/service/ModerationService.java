package com.example.turingOnlineForumSystem.service;


import com.example.turingOnlineForumSystem.exception.ResourceNotFoundException;
import com.example.turingOnlineForumSystem.model.*;
import com.example.turingOnlineForumSystem.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.example.turingOnlineForumSystem.repository.ThreadRepository;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ModerationService {

    private final ModerationRepository moderationRepository;
    private final ThreadRepository threadsRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    /**
     * Delete a thread and log moderation action.
     */
    @Transactional
    public void deleteThread(Long threadId, Long moderatorId, String reason) {
        // Use proxy reference to avoid TransientObjectException
        Threads threadRef = threadsRepository.getReferenceById(threadId);

        // Fetch thread owner safely
        User threadOwner = userRepository.getReferenceById(threadRef.getUser().getId());

        // Save moderation log BEFORE deleting thread
        Moderation moderation = Moderation.builder()
                .action("DELETE_THREAD")
                .reason(reason)
                .user(threadOwner)
                .thread(threadRef)
                .createdAt(LocalDateTime.now())
                .build();

        moderationRepository.save(moderation);
        moderationRepository.flush(); // ensure it persists before delete

        // Delete posts
        List<Post> posts = postRepository.findByThreadId(threadId);
        postRepository.deleteAll(posts);

        // Delete the thread
        threadsRepository.deleteById(threadId);

        log.info("Deleted thread ID {} by moderator {}", threadId, moderatorId);
    }




    /**
     * Delete a post and log moderation action.
     */
    public void deletePost(Long postId, Long moderatorId, String reason) {
        Post post = postRepository.findById(postId).orElseThrow(() -> {
            log.error("Post with ID {} not found for moderation", postId);
            return new ResourceNotFoundException("Post not found");
        });

        postRepository.deleteById(postId);

        Moderation moderation = Moderation.builder()
                .action("DELETE_POST")
                .reason(reason)
                .user(post.getUser())
                .thread(post.getThread())
                .createdAt(LocalDateTime.now())
                .build();

        moderationRepository.save(moderation);
        log.info("Moderator {} deleted post ID {} with reason: {}", moderatorId, postId, reason);
    }

    /**
     * Ban a user from posting and log moderation action.
     */
    public void banUser(Long userId, String reason) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.error("User with ID {} not found for banning", userId);
            return new ResourceNotFoundException("User not found");
        });

        user.setBanned(true);
        userRepository.save(user);

        Moderation moderation = Moderation.builder()
                .action("BAN_USER")
                .reason(reason)
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();

        moderationRepository.save(moderation);
        log.info("User ID {} has been banned for reason: {}", userId, reason);
    }

    /**
     * Get moderation history for a user.
     */
    public List<Moderation> getModerationHistory(Long userId) {
        log.info("Fetching moderation history for user ID {}", userId);
        return moderationRepository.findByUserId(userId);
    }
}
