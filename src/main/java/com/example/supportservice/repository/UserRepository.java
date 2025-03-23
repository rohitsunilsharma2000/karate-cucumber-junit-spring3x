package com.example.supportservice.repository;


import com.example.supportservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByUsernameContainingIgnoreCase(String keyword);

    Optional<User> findByEmail ( String email );
}