package com.example.videohosting.controller;

import com.example.videohosting.model.Video;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 🧪 **VideoControllerTest**
 * <p>
 * This test class validates the endpoints in VideoController.
 * It uses:
 * - @SpringBootTest to load the full application context.
 * - @AutoConfigureMockMvc to configure the web environment.
 * - @Transactional to auto-rollback DB changes after each test.
 * - @WithMockUser to simulate a secured user context.
 * - @TestMethodOrder with @Order to control the execution order of tests.
 * <p>
 * File: src/test/java/com/example/videohosting/controller/VideoControllerTest.java
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestMethodOrder(OrderAnnotation.class)
public class VideoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Test case for uploading a new video.
     * <p>
     * This test sends a POST request to "/api/videos" with a JSON payload representing
     * a new video. It verifies that the video is saved with the "PENDING" status.
     */
    @Test
    @Order(1)
    public void testUploadVideo() throws Exception {
        Video video = new Video();
        video.setTitle("Test Video");
        video.setDescription("Test video description");
        video.setFilePath("src/test/resources/test_video.mp4");
        // Initial status will be set by the controller to "PENDING"

        String videoJson = objectMapper.writeValueAsString(video);

        mockMvc.perform(post("/api/videos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(videoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    /**
     * Test case for retrieving all videos.
     * <p>
     * This test performs a GET request to "/api/videos" and verifies that the response is OK.
     * It assumes that at least one video exists in the repository (e.g., uploaded in previous tests).
     */
    @Test
    @Order(2)
    public void testGetAllVideos() throws Exception {
        // First, upload a video so that the list is not empty.
        Video video = new Video();
        video.setTitle("Another Test Video");
        video.setDescription("Another test video description");
        video.setFilePath("src/test/resources/another_test_video.mp4");
        String videoJson = objectMapper.writeValueAsString(video);
        mockMvc.perform(post("/api/videos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(videoJson))
                .andExpect(status().isOk());

        // Then, retrieve all videos.
        mockMvc.perform(get("/api/videos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    /**
     * Test case for triggering video transcoding.
     * <p>
     * This test performs a POST request to "/api/videos/transcode/{id}" with a target format.
     * It verifies that the endpoint responds with an accepted status and a message indicating
     * that transcoding has started.
     * <p>
     * Note: Since the transcoding is asynchronous, this test only verifies the immediate response.
     */
    @Test
    @Order(3)
    public void testTranscodeVideo() throws Exception {
        // First, upload a video to obtain an ID.
        Video video = new Video();
        video.setTitle("Video for Transcoding");
        video.setDescription("Video description for transcoding test");
        video.setFilePath("src/test/resources/transcode_test_video.mp4");
        String videoJson = objectMapper.writeValueAsString(video);

        String uploadResponse = mockMvc.perform(post("/api/videos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(videoJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Video savedVideo = objectMapper.readValue(uploadResponse, Video.class);

        // Trigger transcoding for the uploaded video.
        mockMvc.perform(post("/api/videos/transcode/" + savedVideo.getId())
                        .param("format", "mp4"))
                .andExpect(status().isAccepted())
                .andExpect(content().string(containsString("Transcoding started for video id: " + savedVideo.getId())));
    }
}
