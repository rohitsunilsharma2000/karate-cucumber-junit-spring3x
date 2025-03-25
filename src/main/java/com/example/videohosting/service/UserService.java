package com.example.videohosting.service;


import com.example.videohosting.exception.ResourceNotFoundException;
import com.example.videohosting.model.User;
import com.example.videohosting.repository.UserRepository;
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


    public User save(User user) {
        return userRepo.save(user);
    }


    public User findByUsername(String username) {

        return userRepo.findByUsername(username);

    }
}