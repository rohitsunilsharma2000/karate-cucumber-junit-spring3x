package com.example.turingOnlineForumSystem.repository;

import com.example.turingOnlineForumSystem.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipientId(Long userId);
}
