package com.example.videohosting.service;

import com.example.videohosting.model.Video;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class TranscodingService {

    private static final Logger logger = LoggerFactory.getLogger(TranscodingService.class);

    @Async
    public CompletableFuture<Video> transcodeVideoAsync(Video video, String targetFormat) {
        logger.info("Starting transcoding for video id: {}", video.getId());
        try {
            String inputFile = video.getFilePath();
            String outputFile = inputFile; // Adjust as necessary, e.g., inputFile + "." + targetFormat;
//            String ffmpegExecutable = "/opt/homebrew/bin/ffmpeg"; // Make sure the path is correct
            String ffmpegExecutable = "/opt/homebrew/bin/ffmpeg"; // Adjust accordingly

            ProcessBuilder processBuilder = new ProcessBuilder(
                    ffmpegExecutable, "-i", inputFile, "-f", targetFormat, outputFile
            );
            Process process = processBuilder.start();
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                logger.info("Transcoding successful for video id: {}", video.getId());
                video.setStatus("READY");
                video.setFilePath(outputFile);
            } else {
                logger.error("Transcoding failed for video id: {}. Exit code: {}", video.getId(), exitCode);
                video.setStatus("FAILED");
            }
        } catch (Exception e) {
            logger.error("Exception during transcoding for video id: {}: {}", video.getId(), e.getMessage());
            video.setStatus("FAILED");
            throw new RuntimeException("Transcoding error", e);
        }
        return CompletableFuture.completedFuture(video);
    }
}
