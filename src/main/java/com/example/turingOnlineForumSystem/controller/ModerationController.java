package com.example.turingOnlineForumSystem.controller;


import com.example.turingOnlineForumSystem.model.Moderation;
import com.example.turingOnlineForumSystem.service.ModerationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/moderation")
@RequiredArgsConstructor
@Slf4j
public class ModerationController {

    private final ModerationService moderationService;

    /**
     * Delete a thread as moderator.
     */
    @DeleteMapping("/thread/{threadId}")
    public void deleteThread(@PathVariable Long threadId,
                             @RequestParam Long moderatorId,
                             @RequestParam String reason) {
        moderationService.deleteThread(threadId, moderatorId, reason);
    }

    /**
     * Delete a post as moderator.
     */
    @DeleteMapping("/post/{postId}")
    public void deletePost(@PathVariable Long postId,
                           @RequestParam Long moderatorId,
                           @RequestParam String reason) {
        moderationService.deletePost(postId, moderatorId, reason);
    }

    /**
     * Ban a user from posting.
     */
    @PostMapping("/ban-user/{userId}")
    public void banUser(@PathVariable Long userId,
                        @RequestParam String reason) {
        moderationService.banUser(userId, reason);
    }

    /**
     * Get moderation history for a user.
     */
    @GetMapping("/history/{userId}")
    public List<Moderation> getModerationHistory(@PathVariable Long userId) {
        return moderationService.getModerationHistory(userId);
    }
}

