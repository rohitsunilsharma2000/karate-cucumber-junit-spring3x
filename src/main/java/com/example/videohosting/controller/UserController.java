package com.example.videohosting.controller;


import com.example.videohosting.model.User;
import com.example.videohosting.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;


    /**
     * Create a new user.
     */
    @PostMapping("/register")
    public User registerUser( @RequestBody User user) {
        // Add logic for saving user (e.g., hashing password)
        return userService.save(user);
    }



}