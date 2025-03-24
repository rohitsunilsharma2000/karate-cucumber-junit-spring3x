package com.example.turingOnlineForumSystem.repository;


import com.example.turingOnlineForumSystem.model.Moderation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ModerationRepository extends JpaRepository<Moderation, Long> {
    List<Moderation> findByUserId(Long userId);
}
