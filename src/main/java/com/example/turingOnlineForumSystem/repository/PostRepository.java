package com.example.turingOnlineForumSystem.repository;


import com.example.turingOnlineForumSystem.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByThreadId(Long threadId);
}