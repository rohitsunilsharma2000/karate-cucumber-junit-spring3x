package com.example.turingOnlineForumSystem.repository;

import com.example.turingOnlineForumSystem.model.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<Follow> findByFollowerId(Long followerId);
}
