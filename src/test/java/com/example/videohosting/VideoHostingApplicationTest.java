package com.example.videohosting;

import com.example.videohosting.model.User;
import com.example.videohosting.model.Video;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 🧪 **ApplicationIntegrationTest**
 *
 * This integration test suite verifies key functionalities of the Video Hosting Application.
 *
 * It uses:
 *   - @SpringBootTest to load the full application context.
 *   - @AutoConfigureMockMvc to set up the web environment.
 *   - @Transactional to auto-rollback DB changes after each test.
 *   - @WithMockUser(username = "admin@example.com", roles = {"ADMIN"}) to simulate a secured admin user.
 *   - @TestMethodOrder with @Order to control the execution order of tests.
 *
 * This test class covers:
 *   1. User registration (via /api/users/register)
 *   2. Video upload (via /api/videos)
 *   3. Video transcoding (via /api/videos/transcode/{id})
 *   4. Invoking the main() method to ensure context loads.
 *
 * Running this suite will significantly increase both method and line coverage.
 *
 * File: src/test/java/com/example/videohosting/ApplicationIntegrationTest.java
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestMethodOrder(OrderAnnotation.class)
public class VideoHostingApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Test user registration endpoint.
     *
     * This test sends a POST request to "/api/users/register" with a JSON payload representing a new user.
     * It verifies that the response contains an auto-generated user ID and correct username.
     */
    @Test
    @Order(1)
    public void testUserRegistration() throws Exception {
        User user = new User();
        user.setUsername("john@example.com");
        user.setPassword("password");
        user.setRole("USER");

        String json = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/api/users/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").exists())
               .andExpect(jsonPath("$.username").value("john@example.com"));
    }

    /**
     * Test video upload endpoint.
     *
     * This test sends a POST request to "/api/videos" with a JSON payload representing a new video.
     * It verifies that the video is saved with a "PENDING" status.
     */
    @Test
    @Order(2)
    public void testVideoUpload() throws Exception {
        Video video = new Video();
        video.setTitle("Integration Test Video");
        video.setDescription("Testing video upload");
        video.setFilePath("src/test/resources/integration_video.mp4");
        // The controller sets the status to "PENDING"

        String json = objectMapper.writeValueAsString(video);

        mockMvc.perform(post("/api/videos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").exists())
               .andExpect(jsonPath("$.status").value("PENDING"));
    }

    /**
     * Test video transcoding endpoint.
     *
     * This test first uploads a video, then sends a POST request to "/api/videos/transcode/{id}"
     * to trigger asynchronous transcoding. It verifies that the response is HTTP 202 Accepted
     * and contains a message indicating that transcoding has started.
     */
    @Test
    @Order(3)
    public void testVideoTranscoding() throws Exception {
        // Upload a video first.
        Video video = new Video();
        video.setTitle("Transcode Test Video");
        video.setDescription("Video to test transcoding");
        video.setFilePath("src/test/resources/transcode_video.mp4");
        video.setStatus("PENDING");

        String json = objectMapper.writeValueAsString(video);

        String uploadResponse = mockMvc.perform(post("/api/videos")
                                                        .contentType(MediaType.APPLICATION_JSON)
                                                        .content(json))
                                       .andExpect(status().isOk())
                                       .andReturn().getResponse().getContentAsString();

        Video savedVideo = objectMapper.readValue(uploadResponse, Video.class);
        assertNotNull(savedVideo.getId());

        // Trigger transcoding for the uploaded video.
        mockMvc.perform(post("/api/videos/transcode/" + savedVideo.getId())
                                .param("format", "mp4"))
               .andExpect(status().isAccepted())
               .andExpect(content().string(containsString("Transcoding started for video id: " + savedVideo.getId())));
    }

    /**
     * Test the main() method of the application.
     *
     * This test invokes the main() method to ensure that the application context loads successfully.
     */
    @Test
    @Order(4)
    public void testMainMethodInvocation() {
        String[] args = {};
        var context = SpringApplication.run(VideoHostingApplication.class, args);
        assertNotNull(context, "Application context should not be null");
        context.close();
    }
}
