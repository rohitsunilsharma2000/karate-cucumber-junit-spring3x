package com.example.turingOnlineForumSystem.controller;


import com.example.turingOnlineForumSystem.model.Threads;
import com.example.turingOnlineForumSystem.model.User;
import com.example.turingOnlineForumSystem.repository.ThreadRepository;
import com.example.turingOnlineForumSystem.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@ActiveProfiles("test") // Optional: for test-specific configs
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ThreadControllerIntegrationTest {

    private static User testUser;
    private static Long createdThreadId;
    private final RestTemplate restTemplate = new RestTemplate();
    @LocalServerPort
    private int port;
    private String baseUrl;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ThreadRepository threadRepository;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/threads";

        if (testUser == null) {
            testUser = userRepository.save(User.builder()
                    .username("testUser")
                    .email("test@example.com")
                    .password("pass123")
                    .createdAt(LocalDateTime.now())
                    .build());
        }
    }

    @Test
    @Order(1)
    void testCreateThread() {
        Threads thread = Threads.builder()
                .title("Test Thread")
                .content("Thread content here.")
                .createdAt(LocalDateTime.now())
                .user(testUser)
                .build();

        ResponseEntity<Threads> response = restTemplate.postForEntity(baseUrl, thread, Threads.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());

        createdThreadId = response.getBody().getId();
    }

    @Test
    @Order(2)
    void testGetThreadById() {
        ResponseEntity<Threads> response = restTemplate.getForEntity(baseUrl + "/" + createdThreadId, Threads.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Thread", response.getBody().getTitle());
    }

    @Test
    @Order(3)
    void testUpdateThread() {
        Threads updated = Threads.builder()
                .title("Updated Thread Title")
                .content("Updated content")
                .user(testUser)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Threads> entity = new HttpEntity<>(updated, headers);

        ResponseEntity<Threads> response = restTemplate.exchange(
                baseUrl + "/" + createdThreadId,
                HttpMethod.PUT,
                entity,
                Threads.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated Thread Title", response.getBody().getTitle());
    }

    @Test
    @Order(4)
    void testGetAllThreads() {
        ResponseEntity<Threads[]> response = restTemplate.getForEntity(baseUrl, Threads[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().length > 0);
    }

    @Test
    @Order(5)
    void testDeleteThread() {
        restTemplate.delete(baseUrl + "/" + createdThreadId);
        boolean exists = threadRepository.existsById(createdThreadId);
        assertFalse(exists);
    }
}

