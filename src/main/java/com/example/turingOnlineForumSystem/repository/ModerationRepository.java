package com.example.turingOnlineForumSystem.repository;


import com.example.turingOnlineForumSystem.model.Moderation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModerationRepository extends JpaRepository<Moderation, Long> {
    List<Moderation> findByUserId(Long userId);
}
