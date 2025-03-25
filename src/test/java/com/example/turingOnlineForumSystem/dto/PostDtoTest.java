package com.example.turingOnlineForumSystem.dto;


import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for the PostDto class.
 * Validates object construction via builder, field access,
 * mutation via setters, and Lombok-generated methods like
 * toString(), equals(), and hashCode().
 */
public class PostDtoTest {

    /**
     * Test PostDto using builder pattern.
     * Ensures all fields are correctly set and retrieved.
     */
    @Test
    public void testPostDtoBuilder() {
        LocalDateTime timestamp = LocalDateTime.now();

        // Create PostDto using builder
        PostDto post = PostDto.builder()
                              .id(101L)
                              .content("This is a post in a thread.")
                              .userId(55L)
                              .threadId(999L)
                              .createdAt(timestamp)
                              .build();

        // Assertions to verify field values
        assertEquals(101L, post.getId());
        assertEquals("This is a post in a thread.", post.getContent());
        assertEquals(55L, post.getUserId());
        assertEquals(999L, post.getThreadId());
        assertEquals(timestamp, post.getCreatedAt());

        // Verify toString is not null (Lombok-generated)
        assertNotNull(post.toString());

        // Verify equals and hashCode behavior
        assertEquals(post, post);
        assertEquals(post.hashCode(), post.hashCode());
    }

    /**
     * Test PostDto setters and getters directly (non-builder path).
     */
    @Test
    public void testPostDtoSettersAndGetters() {
        PostDto post = new PostDto();

        LocalDateTime now = LocalDateTime.now();

        post.setId(1L);
        post.setContent("Hello world!");
        post.setUserId(123L);
        post.setThreadId(456L);
        post.setCreatedAt(now);

        assertEquals(1L, post.getId());
        assertEquals("Hello world!", post.getContent());
        assertEquals(123L, post.getUserId());
        assertEquals(456L, post.getThreadId());
        assertEquals(now, post.getCreatedAt());

        assertNotNull(post.toString());
    }
}
