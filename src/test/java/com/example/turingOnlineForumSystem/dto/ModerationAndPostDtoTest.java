package com.example.turingOnlineForumSystem.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ModerationDTO and PostDto classes.
 * These tests validate field assignments, constructor behavior,
 * Lombok-generated methods (getters/setters, equals, hashCode, toString),
 * and the builder pattern.
 */
public class ModerationAndPostDtoTest {

    /**
     * Test for the ModerationDTO class.
     * This verifies that all fields are correctly set and retrieved,
     * and that equals/hashCode/toString work as expected.
     */
    @Test
    public void testModerationDTO() {
        LocalDateTime now = LocalDateTime.now();

        // Create DTO using all-args constructor
        ModerationDTO moderation = new ModerationDTO(
                1L,
                "DELETE_POST",
                "Inappropriate language",
                now,
                101L,
                "john_doe",
                55L
        );

        // Assert all fields
        assertEquals(1L, moderation.getId());
        assertEquals("DELETE_POST", moderation.getAction());
        assertEquals("Inappropriate language", moderation.getReason());
        assertEquals(now, moderation.getCreatedAt());
        assertEquals(101L, moderation.getUserId());
        assertEquals("john_doe", moderation.getUsername());
        assertEquals(55L, moderation.getThreadId());

        // Modify a field using setter
        moderation.setAction("BAN_USER");
        assertEquals("BAN_USER", moderation.getAction());

        // toString and equals/hashCode check
        assertNotNull(moderation.toString());
        assertEquals(moderation, moderation);
        assertEquals(moderation.hashCode(), moderation.hashCode());
    }

    /**
     * Test for the PostDto class using builder pattern.
     * This ensures all fields can be set, retrieved, and mutated correctly.
     */
    @Test
    public void testPostDto() {
        LocalDateTime timestamp = LocalDateTime.now();

        // Use builder to create PostDto
        PostDto post = PostDto.builder()
                              .id(10L)
                              .content("This is a test post.")
                              .userId(200L)
                              .threadId(500L)
                              .createdAt(timestamp)
                              .build();

        // Assert values
        assertEquals(10L, post.getId());
        assertEquals("This is a test post.", post.getContent());
        assertEquals(200L, post.getUserId());
        assertEquals(500L, post.getThreadId());
        assertEquals(timestamp, post.getCreatedAt());

        // Update value
        post.setContent("Updated content");
        assertEquals("Updated content", post.getContent());

        // Test toString, equals, hashCode
        assertNotNull(post.toString());
        assertEquals(post, post);
        assertEquals(post.hashCode(), post.hashCode());
    }
}
