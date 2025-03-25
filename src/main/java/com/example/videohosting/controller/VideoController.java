package com.example.videohosting.controller;

import com.example.videohosting.model.Video;
import com.example.videohosting.service.TranscodingService;
import com.example.videohosting.service.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/api/videos")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @Autowired
    private TranscodingService transcodingService;

    @GetMapping
    public List<Video> getAllVideos() {
        return videoService.getAllVideos();
    }

    @PostMapping
    public Video uploadVideo(@RequestBody Video video) {
        // Logic for handling file upload can be added here
        video.setStatus("PENDING");
        return videoService.saveVideo(video);
    }

    @PostMapping("/transcode/{id}")
    public ResponseEntity<String> transcodeVideo( @PathVariable Long id, @RequestParam String format) {
        Video video = videoService.getVideoById(id);
        video.setStatus("TRANSCODING");
        videoService.saveVideo(video); // Update status before processing

        transcodingService.transcodeVideoAsync(video, format)
                          .thenAccept(updatedVideo -> {
                              videoService.saveVideo(updatedVideo);
                              log.info("Transcoding process updated for video id: {}", updatedVideo.getId());
                          })
                          .exceptionally(ex -> {
                              log.error("Error during asynchronous transcoding: {}", ex.getMessage());
                              return null;
                          });

        // Respond immediately while transcoding continues in the background.
        return ResponseEntity.accepted().body("Transcoding started for video id: " + id);
    }

}
