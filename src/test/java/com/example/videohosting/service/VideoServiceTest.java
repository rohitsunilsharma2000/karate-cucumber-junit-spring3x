package com.example.videohosting.service;


import com.example.videohosting.model.Video;
import com.example.videohosting.repository.VideoRepository;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 🧪 **VideoServiceTest**
 *
 * This test class validates the functionality of VideoService.
 * It uses @SpringBootTest to load the full application context,
 * @AutoConfigureMockMvc to set up the web environment,
 * @Transactional to roll back DB changes after each test,
 * @WithMockUser to simulate a secured user, and @Order to control the execution order.
 *
 * File: src/test/java/com/example/videohosting/service/VideoServiceTest.java
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestMethodOrder(OrderAnnotation.class)
public class VideoServiceTest {

    @Autowired
    private VideoService videoService;

    @Autowired
    private VideoRepository videoRepository;

    /**
     * Test case for saving a video and retrieving it by ID.
     * <p>
     * This test creates a new Video object, saves it using VideoService,
     * and then retrieves it by its ID to verify that the save operation was successful.
     * </p>
     */
    @Test
    @Order(1)
    public void testSaveAndRetrieveVideo() {
        // Create a dummy video object
        Video video = new Video();
        video.setTitle("Test Video");
        video.setDescription("A test video description.");
        video.setFilePath("src/test/resources/test_video.mp4");
        video.setStatus("PENDING");

        // Save the video
        Video savedVideo = videoService.saveVideo(video);
        assertNotNull(savedVideo.getId(), "Saved video should have an auto-generated ID.");

        // Retrieve the video by its ID
        Video retrievedVideo = videoService.getVideoById(savedVideo.getId());
        assertNotNull(retrievedVideo, "Retrieved video should not be null.");
        assertEquals("Test Video", retrievedVideo.getTitle(), "Video title should match the saved title.");
    }

    /**
     * Test case for retrieving all videos.
     * <p>
     * This test saves a sample video and then retrieves the list of all videos
     * to ensure that the getAllVideos method returns a non-empty list.
     * </p>
     */
    @Test
    @Order(2)
    public void testGetAllVideos() {
        // Create and save a sample video
        Video video = new Video();
        video.setTitle("Sample Video");
        video.setDescription("A sample video description.");
        video.setFilePath("src/test/resources/sample_video.mp4");
        video.setStatus("PENDING");
        videoService.saveVideo(video);

        // Retrieve all videos from the repository
        List<Video> videos = videoService.getAllVideos();
        assertFalse(videos.isEmpty(), "The list of videos should not be empty.");
    }
}
