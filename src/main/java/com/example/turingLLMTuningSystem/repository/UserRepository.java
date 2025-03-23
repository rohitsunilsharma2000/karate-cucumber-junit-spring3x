package com.example.turingLLMTuningSystem.repository;


import com.example.turingLLMTuningSystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByUsernameContainingIgnoreCase(String keyword);
}