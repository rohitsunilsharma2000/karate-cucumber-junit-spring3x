package com.example.turingOnlineForumSystem.controller;

import com.example.turingOnlineForumSystem.model.Threads;
import com.example.turingOnlineForumSystem.service.ThreadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/threads")
@RequiredArgsConstructor
@Slf4j
public class ThreadController {

    private final ThreadService threadsService;

    @PostMapping
    public Threads createThreads(@RequestBody Threads Threads) {
        log.debug("Request to create Threads: {}", Threads.getTitle());
        return threadsService.createThread(Threads);
    }

    @GetMapping("/{id}")
    public Threads getThreads(@PathVariable Long id) {
        return threadsService.getThread(id);
    }

    @GetMapping
    public List<Threads> getAllThreadss() {
        return threadsService.getAllThreads();
    }

    @PutMapping("/{id}")
    public Threads updateThreads(@PathVariable Long id, @RequestBody Threads Threads) {
        return threadsService.updateThread(id, Threads);
    }

    @DeleteMapping("/{id}")
    public void deleteThreads(@PathVariable Long id) {
        threadsService.deleteThread(id);
    }
}
