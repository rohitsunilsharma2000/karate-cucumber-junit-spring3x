package com.example.turingOnlineForumSystem.service;


import com.example.turingOnlineForumSystem.dto.PostDto;
import com.example.turingOnlineForumSystem.exception.ResourceNotFoundException;
import com.example.turingOnlineForumSystem.model.Post;
import com.example.turingOnlineForumSystem.model.Threads;
import com.example.turingOnlineForumSystem.model.User;
import com.example.turingOnlineForumSystem.repository.PostRepository;
import com.example.turingOnlineForumSystem.repository.ThreadRepository;
import com.example.turingOnlineForumSystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final ThreadRepository threadRepository;
    private final UserRepository userRepository;

    /**
     * Create a new post under a given thread.
     *
     * @param post the post to create
     * @param threadId the ID of the thread the post belongs to
     * @return saved post
     */
    public Post createPost(Post post, Long threadId) {
        Threads thread = threadRepository.findById(threadId)
                .orElseThrow(() -> new ResourceNotFoundException("Thread not found with ID: " + threadId));

        User user = userRepository.findById(post.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + post.getUser().getId()));

        Post newPost = new Post();
        newPost.setContent(post.getContent());
        newPost.setUser(user);
        newPost.setThread(thread);
        newPost.setCreatedAt(LocalDateTime.now());

        log.info("Saving post: content={}, userId={}, threadId={}",
                newPost.getContent(),
                newPost.getUser().getId(),
                newPost.getThread().getId());


        return postRepository.save(newPost);
    }


    /**
     * Get all posts associated with a thread.
     *
     * @param threadId the thread ID
     * @return list of posts
     */
    public List<PostDto> getPostsByThread(Long threadId) {
        log.info("Fetching posts for thread ID {}", threadId);

        return postRepository.findByThreadId(threadId).stream()
                .map(post -> PostDto.builder()
                        .id(post.getId())
                        .content(post.getContent())
                        .userId(post.getUser().getId())
                        .threadId(post.getThread().getId())
                        .createdAt(post.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * (Optional extension) Delete all posts under a thread.
     */
    public void deletePostsByThread(Long threadId) {
        List<Post> posts = postRepository.findByThreadId(threadId);
        postRepository.deleteAll(posts);
        log.info("Deleted {} posts under thread ID {}", posts.size(), threadId);
    }
}
