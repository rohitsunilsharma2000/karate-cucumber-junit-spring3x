package com.example.videohosting.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 🧪 **StreamingControllerTest**
 * <p>
 * This test class validates the StreamingController endpoints.
 * It uses:
 * - @SpringBootTest to load the full application context.
 * - @AutoConfigureMockMvc to configure the web environment.
 * - @Transactional to auto-rollback DB changes after each test.
 * - @WithMockUser to simulate a secured user context.
 * - @TestMethodOrder with @Order to control execution order.
 * <p>
 * For testing, the video directory path in the StreamingController is overridden
 * via reflection to point to a temporary directory with test video files.
 * <p>
 * File: src/test/java/com/example/videohosting/controller/StreamingControllerTest.java
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional

@TestMethodOrder(OrderAnnotation.class)
public class StreamingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StreamingController streamingController;

    private Path tempDirectory;

    /**
     * Sets up the test environment by creating a temporary directory and a dummy video file.
     * Overrides the 'videoDirectory' field in StreamingController to point to the temporary directory.
     */
    @BeforeEach
    public void setup() throws Exception {
        // Create a temporary directory for test video files.
        tempDirectory = Files.createTempDirectory("testVideos");

        // Create a temporary file "sample_video.mp4" in the temporary directory.
        File tempFile = new File(tempDirectory.toFile(), "sample_video.mp4");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("dummy content"); // Write dummy content to simulate a video file.
        }

        // Override the private final field 'videoDirectory' in StreamingController via reflection.
        Field field = StreamingController.class.getDeclaredField("videoDirectory");
        field.setAccessible(true);
        field.set(streamingController, tempDirectory.toString());
    }

    /**
     * Test case for a successful video streaming request.
     * <p>
     * This test performs a GET request for an existing video file ("sample_video.mp4") and expects:
     * - HTTP status 200 (OK)
     * - Correct Content-Disposition header with inline display
     * - Content type "video/mp4"
     * </p>
     */
    @Test
    @Order(1)
    public void testStreamVideoSuccess() throws Exception {
        mockMvc.perform(get("/api/stream/sample_video.mp4"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "inline; filename=\"sample_video.mp4\""))
                .andExpect(content().contentType("video/mp4"));
    }

    /**
     * Test case for a video streaming request where the file does not exist.
     * <p>
     * This test performs a GET request for a non-existent video file ("non_existent_video.mp4") and expects:
     * - An internal server error (HTTP status 500) due to the RuntimeException thrown by the controller.
     * </p>
     */
    @Test
    @Order(2)
    public void testStreamVideoNotFound() throws Exception {
        mockMvc.perform(get("/api/stream/non_existent_video.mp4"))
                .andExpect(status().isInternalServerError());
    }
}
