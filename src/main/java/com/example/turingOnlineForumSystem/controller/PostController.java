package com.example.turingOnlineForumSystem.controller;


import com.example.turingOnlineForumSystem.dto.PostDto;
import com.example.turingOnlineForumSystem.model.Post;
import com.example.turingOnlineForumSystem.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;

    @PostMapping("/thread/{threadId}")
    public Post createPost(@PathVariable Long threadId, @RequestBody Post post) {
        return postService.createPost(post, threadId);
    }

    @GetMapping("/thread/{threadId}")
    public List<PostDto> getPostsByThread(@PathVariable Long threadId) {
        return postService.getPostsByThread(threadId);
    }
}
