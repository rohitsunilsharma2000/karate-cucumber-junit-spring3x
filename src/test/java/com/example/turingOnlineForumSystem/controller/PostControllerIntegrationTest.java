package com.example.turingOnlineForumSystem.controller;

import com.example.turingOnlineForumSystem.model.Post;
import com.example.turingOnlineForumSystem.model.Threads;
import com.example.turingOnlineForumSystem.repository.PostRepository;
import com.example.turingOnlineForumSystem.repository.ThreadRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ThreadRepository threadRepository;

    @Autowired
    private PostRepository postRepository;

    private Threads testThread;

    @BeforeEach
    public void setup() {
        postRepository.deleteAll();
        threadRepository.deleteAll();

        Threads thread = new Threads();
        thread.setTitle("Test Thread");
        thread.setContent("Thread Content");
        testThread = threadRepository.save(thread);
    }

    @Test
    public void testCreatePost() {
        Post post = new Post();
        post.setContent("This is a test post");

        ResponseEntity<Post> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/posts/thread/" + testThread.getId(),
                post,
                Post.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getContent()).isEqualTo("This is a test post");
    }

    @Test
    public void testGetPostsByThread() {
        Post post = new Post();
        post.setContent("Another test post");
        post.setThread(testThread);
        postRepository.save(post);

        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/posts/thread/" + testThread.getId(),
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Another test post");
    }
}
