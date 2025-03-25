package com.example.turingOnlineForumSystem.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DTOs to ensure data encapsulation and object behavior.
 * Tests cover constructors, getters, setters, equals, hashCode, and builder (if applicable).
 */
public class DtoTest {

    /**
     * Test ChatMessageDTO: field assignment and retrieval.
     */
    @Test
    public void testChatMessageDTO() {
        ChatMessageDTO dto = new ChatMessageDTO();
        dto.setSenderId(1L);
        dto.setReceiverId(2L);
        dto.setContent("Hello!");

        assertEquals(1L, dto.getSenderId());
        assertEquals(2L, dto.getReceiverId());
        assertEquals("Hello!", dto.getContent());
        assertNotNull(dto.toString()); // Lombok-generated toString check
    }

    /**
     * Test ModerationDTO: all-args constructor, field access, and equals/hashCode.
     */
    @Test
    public void testModerationDTO() {
        LocalDateTime now = LocalDateTime.now();

        ModerationDTO dto = new ModerationDTO(
                100L, "BAN_USER", "Spam content", now,
                10L, "john_doe", 55L
        );

        assertEquals(100L, dto.getId());
        assertEquals("BAN_USER", dto.getAction());
        assertEquals("Spam content", dto.getReason());
        assertEquals(now, dto.getCreatedAt());
        assertEquals(10L, dto.getUserId());
        assertEquals("john_doe", dto.getUsername());
        assertEquals(55L, dto.getThreadId());
        assertEquals(dto, dto); // Test equals
        assertEquals(dto.hashCode(), dto.hashCode());
    }

    /**
     * Test PostDto: builder pattern, getters, and object behavior.
     */
    @Test
    public void testPostDto() {
        LocalDateTime createdAt = LocalDateTime.now();

        PostDto post = PostDto.builder()
                              .id(1L)
                              .content("This is a test post.")
                              .userId(42L)
                              .threadId(101L)
                              .createdAt(createdAt)
                              .build();

        assertEquals(1L, post.getId());
        assertEquals("This is a test post.", post.getContent());
        assertEquals(42L, post.getUserId());
        assertEquals(101L, post.getThreadId());
        assertEquals(createdAt, post.getCreatedAt());

        // Modify via setter to check mutability
        post.setContent("Updated content");
        assertEquals("Updated content", post.getContent());

        assertNotNull(post.toString()); // Lombok-generated method check
    }
}
