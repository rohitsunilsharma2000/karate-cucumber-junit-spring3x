package com.example.turingLLMTuningSystem.service;


import com.example.turingLLMTuningSystem.exception.ResourceNotFoundException;
import com.example.turingLLMTuningSystem.model.User;
import com.example.turingLLMTuningSystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {


    private final UserRepository userRepo;

    public User getUserById( Long id) {
        return userRepo.findById(id)
                       .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
    }

    public User updateUserProfile(Long id, User updatedUser) {
        User existing = getUserById(id);
        existing.setUsername(updatedUser.getUsername());
        existing.setEmail(updatedUser.getEmail());
        return userRepo.save(existing);
    }

    public Optional<User> findById(Long id) {
        return userRepo.findById(id);
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public User save(User user) {
        return userRepo.save(user);
    }
}