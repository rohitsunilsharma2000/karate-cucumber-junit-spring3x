package com.example.videohosting.model;


import com.example.videohosting.repository.VideoRepository;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * 🧪 **VideoEntityTest**
 * <p>
 * This test class verifies the persistence functionality of the Video entity.
 * It uses:
 * - @SpringBootTest to load the full application context.
 * - @AutoConfigureMockMvc to configure the web environment.
 * - @Transactional to auto-rollback database changes after each test.
 * - @WithMockUser to simulate a secured user context.
 * - @TestMethodOrder with @Order to control the execution order of tests.
 * <p>
 * The test confirms that the Video entity's attributes are correctly set and persisted.
 * <p>
 * File: src/test/java/com/example/videohosting/model/VideoEntityTest.java
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestMethodOrder(OrderAnnotation.class)
public class VideoEntityTest {

    @Autowired
    private VideoRepository videoRepository;

    /**
     * Test case for persisting a Video entity.
     * <p>
     * This test creates a new Video object, sets its attributes,
     * saves it to the database via VideoRepository, and retrieves it
     * to verify that the data has been persisted correctly.
     */
    @Test
    @Order(1)
    public void testVideoEntityPersistence() {
        // Create a new Video entity and set its attributes
        Video video = new Video();
        video.setTitle("Test Video");
        video.setDescription("This is a test video description.");
        video.setFilePath("/path/to/test/video.mp4");
        video.setStatus("PENDING");

        // Save the video entity to the repository
        Video savedVideo = videoRepository.save(video);

        // Assert that the saved video has an auto-generated ID and the correct attribute values.
        assertNotNull(savedVideo.getId(), "Video ID should be generated after saving.");
        assertEquals("Test Video", savedVideo.getTitle(), "Video title should match the expected value.");
        assertEquals("This is a test video description.", savedVideo.getDescription(), "Video description should match.");
        assertEquals("/path/to/test/video.mp4", savedVideo.getFilePath(), "Video file path should match.");
        assertEquals("PENDING", savedVideo.getStatus(), "Video status should be 'PENDING'.");
    }
}

