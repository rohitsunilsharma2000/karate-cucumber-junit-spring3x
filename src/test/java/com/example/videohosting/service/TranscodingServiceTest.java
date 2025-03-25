package com.example.videohosting.service;


import com.example.videohosting.model.Video;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TranscodingServiceTest {

    @Autowired
    private TranscodingService transcodingService;

    /**
     * Test the asynchronous transcoding method.
     * This test creates a dummy video and triggers the transcoding process.
     * The test asserts that the video's status is updated from "PENDING" after processing.
     */
    @Test
    @Order(1)
    public void testTranscodeVideoAsync() {
        // Create a dummy video object
        Video video = new Video();
        video.setId(1L);
        // Use a dummy file path. In a real test, ensure this file exists or adjust the logic accordingly.
        video.setFilePath("src/test/resources/dummy.mp4");
        video.setStatus("PENDING");

        // Invoke asynchronous transcoding
        CompletableFuture<Video> future = transcodingService.transcodeVideoAsync(video, "mp4");

        // Wait for the asynchronous process to complete for testing purposes.
        Video result = future.join();

        // Verify that the video's status has been updated (should no longer be "PENDING")
        assertNotEquals("PENDING", result.getStatus(), "Video status should be updated after transcoding.");

        // Optionally, check that the status is either "READY" or "FAILED" based on the FFmpeg command outcome.
        assertTrue(result.getStatus().equals("READY") || result.getStatus().equals("FAILED"),
                   "Video status should be either READY or FAILED after transcoding.");
    }
}
