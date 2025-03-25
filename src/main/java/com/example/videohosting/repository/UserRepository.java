package com.example.videohosting.repository;


import com.example.videohosting.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByUsernameContainingIgnoreCase(String keyword);
    User findByUsername(String username);
}