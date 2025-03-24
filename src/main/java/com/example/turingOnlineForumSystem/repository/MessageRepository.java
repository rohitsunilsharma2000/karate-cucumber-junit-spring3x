package com.example.turingOnlineForumSystem.repository;


import com.example.turingOnlineForumSystem.model.Message;
import com.example.turingOnlineForumSystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderAndReceiver(User sender, User receiver);
}
