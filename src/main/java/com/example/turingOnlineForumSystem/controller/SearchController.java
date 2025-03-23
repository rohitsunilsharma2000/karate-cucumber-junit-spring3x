package com.example.turingOnlineForumSystem.controller;

import com.example.turingOnlineForumSystem.model.Threads;
import com.example.turingOnlineForumSystem.model.User;
import com.example.turingOnlineForumSystem.repository.ThreadRepository;
import com.example.turingOnlineForumSystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
@Slf4j
public class SearchController {

    private final UserRepository userRepo;
    private final ThreadRepository threadRepo;

    @GetMapping("/users")
    public List<User> searchUsers(@RequestParam String q) {
        log.info("Searching users with keyword: {}", q);
        return userRepo.findByUsernameContainingIgnoreCase(q);
    }

    @GetMapping("/threads")
    public List<Threads> searchThreads(@RequestParam String q) {
        log.info("Searching threads with keyword: {}", q);
        return threadRepo.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(q, q);
    }
}

