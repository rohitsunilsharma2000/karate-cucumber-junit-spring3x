package com.example.turingOnlineForumSystem.repository;


import com.example.turingOnlineForumSystem.model.Threads;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ThreadRepository extends JpaRepository<Threads, Long> {
    List<Threads> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String title, String content);

}