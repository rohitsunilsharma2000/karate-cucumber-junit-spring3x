package com.example.turingOnlineForumSystem.controller;

import com.example.turingOnlineForumSystem.model.Post;
import com.example.turingOnlineForumSystem.model.Threads;
import com.example.turingOnlineForumSystem.model.User;
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

        private String url(String path) {
            return "http://localhost:" + port + path;
        }

        @Test
        void testFullForumFlow() {
            // Step 1: Create User
            User newUser = new User();
            newUser.setUsername("alice");
            newUser.setPassword("secret");
            newUser.setEmail("alice@example.com");
            newUser.setRole("USER");

            ResponseEntity<User> userResponse = restTemplate.postForEntity(
                    url("/api/users"),
                    newUser,
                    User.class
            );

            assertThat(userResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
            User createdUser = userResponse.getBody();
            assertThat(createdUser).isNotNull();
            assertThat(createdUser.getId()).isNotNull();

            // Step 2: Create Thread with that user
            Threads thread = new Threads();
            thread.setTitle("Spring Boot Tips");
            thread.setContent("Let's discuss Spring Boot best practices.");
            thread.setUser(createdUser);

            ResponseEntity<Threads> threadResponse = restTemplate.postForEntity(
                    url("/api/threads"),
                    thread,
                    Threads.class
            );

            assertThat(threadResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
            Threads createdThread = threadResponse.getBody();
            assertThat(createdThread).isNotNull();
            assertThat(createdThread.getId()).isNotNull();

            // Step 3: Create Post in that thread
            Post post = new Post();
            post.setContent("I love using @Slf4j in services!");
            post.setUser(createdUser);

            ResponseEntity<Post> postResponse = restTemplate.postForEntity(
                    url("/api/posts/thread/" + createdThread.getId()),
                    post,
                    Post.class
            );

            assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
            Post createdPost = postResponse.getBody();
            assertThat(createdPost).isNotNull();
            assertThat(createdPost.getId()).isNotNull();

            // Step 4: Fetch Posts by thread
            ResponseEntity<Post[]> getPostsResponse = restTemplate.getForEntity(
                    url("/api/posts/thread/" + createdThread.getId()),
                    Post[].class
            );

            assertThat(getPostsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
            Post[] posts = getPostsResponse.getBody();
            assertThat(posts).isNotNull();
            assertThat(posts.length).isGreaterThanOrEqualTo(1);


        }
    }
