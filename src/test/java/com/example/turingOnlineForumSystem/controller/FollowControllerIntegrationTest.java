package com.example.turingOnlineForumSystem.controller;

import com.example.turingOnlineForumSystem.model.User;
import com.example.turingOnlineForumSystem.repository.UserRepository;
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
public class FollowControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    private Long user1Id;
    private Long user2Id;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();

        User user1 = User.builder().username("user1").email("user1@example.com").password("pass").build();
        User user2 = User.builder().username("user2").email("user2@example.com").password("pass").build();

        user1Id = userRepository.save(user1).getId();
        user2Id = userRepository.save(user2).getId();
    }

    @Test
    public void testFollowUser() {
        String url = "http://localhost:" + port + "/api/follow?followerId=" + user1Id + "&followingId=" + user2Id;

        ResponseEntity<String> response = restTemplate.postForEntity(url, null, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Followed successfully");
    }

    @Test
    public void testGetFollowing() {
        // Follow first
        String followUrl = "http://localhost:" + port + "/api/follow?followerId=" + user1Id + "&followingId=" + user2Id;
        restTemplate.postForEntity(followUrl, null, String.class);

        // Get following list
        String getUrl = "http://localhost:" + port + "/api/follow/" + user1Id + "/following";
        ResponseEntity<User[]> response = restTemplate.getForEntity(getUrl, User[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody()[0].getUsername()).isEqualTo("user2");
    }
}