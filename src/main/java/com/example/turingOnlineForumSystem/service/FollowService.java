package com.example.turingOnlineForumSystem.service;

import com.example.turingOnlineForumSystem.model.Follow;
import com.example.turingOnlineForumSystem.model.User;
import com.example.turingOnlineForumSystem.repository.FollowRepository;
import com.example.turingOnlineForumSystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FollowService {

    private final FollowRepository followRepo;
    private final UserRepository userRepo;

    public void followUser(Long followerId, Long followingId) {
        User follower = userRepo.findById(followerId).orElseThrow();
        User following = userRepo.findById(followingId).orElseThrow();

        Follow follow = new Follow(null, follower, following);
        followRepo.save(follow);
    }

    public List<User> getFollowing(Long userId) {
        return followRepo.findByFollowerId(userId).stream()
                .map(Follow::getFollowing)
                .collect(Collectors.toList());
    }
}

