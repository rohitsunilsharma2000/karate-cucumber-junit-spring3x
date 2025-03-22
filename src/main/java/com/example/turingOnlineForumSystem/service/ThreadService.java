package com.example.turingOnlineForumSystem.service;


import com.example.turingOnlineForumSystem.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import com.example.turingOnlineForumSystem.repository.ThreadRepository;
import com.example.turingOnlineForumSystem.model.Threads;

@Service
@RequiredArgsConstructor
@Slf4j
public class ThreadService {

    private final ThreadRepository threadRepository;

    public Threads createThread(Threads thread) {
        thread.setCreatedAt(LocalDateTime.now());
        thread.setUpdatedAt(LocalDateTime.now());
        Threads saved = threadRepository.save(thread);
        log.info("Created thread with ID {}", saved.getId());
        return saved;
    }

    public Threads updateThread(Long id, Threads updatedThread) {
        return threadRepository.findById(id).map(thread -> {
            thread.setTitle(updatedThread.getTitle());
            thread.setContent(updatedThread.getContent());
            thread.setUpdatedAt(LocalDateTime.now());
            Threads saved = threadRepository.save(thread);
            log.info("Updated thread with ID {}", saved.getId());
            return saved;
        }).orElseThrow(() -> {
            log.error("Thread with ID {} not found", id);
            return new ResourceNotFoundException("Thread not found");
        });
    }

    public void deleteThread(Long id) {
        if (!threadRepository.existsById(id)) {
            log.error("Thread with ID {} not found for deletion", id);
            throw new ResourceNotFoundException("Thread not found");
        }
        threadRepository.deleteById(id);
        log.info("Deleted thread with ID {}", id);
    }

    public Threads getThread(Long id) {
        return threadRepository.findById(id).orElseThrow(() -> {
            log.error("Thread with ID {} not found", id);
            return new ResourceNotFoundException("Thread not found");
        });
    }

    public List<Threads> getAllThreads() {
        log.info("Fetching all threads");
        return threadRepository.findAll();
    }
}
