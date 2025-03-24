package com.example.turingOnlineForumSystem.dto;


import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class PostDtoTest {

    @Test
    void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();

        PostDto dto = new PostDto(1L, "This is a post", 2L, 3L, now);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getContent()).isEqualTo("This is a post");
        assertThat(dto.getUserId()).isEqualTo(2L);
        assertThat(dto.getThreadId()).isEqualTo(3L);
        assertThat(dto.getCreatedAt()).isEqualTo(now);
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        LocalDateTime created = LocalDateTime.of(2024, 5, 1, 10, 0);

        PostDto dto = new PostDto();
        dto.setId(100L);
        dto.setContent("Test content");
        dto.setUserId(10L);
        dto.setThreadId(20L);
        dto.setCreatedAt(created);

        assertThat(dto.getId()).isEqualTo(100L);
        assertThat(dto.getContent()).isEqualTo("Test content");
        assertThat(dto.getUserId()).isEqualTo(10L);
        assertThat(dto.getThreadId()).isEqualTo(20L);
        assertThat(dto.getCreatedAt()).isEqualTo(created);
    }

    @Test
    void testBuilder() {
        LocalDateTime timestamp = LocalDateTime.now();

        PostDto dto = PostDto.builder()
                             .id(5L)
                             .content("Hello builder")
                             .userId(55L)
                             .threadId(77L)
                             .createdAt(timestamp)
                             .build();

        assertThat(dto.getId()).isEqualTo(5L);
        assertThat(dto.getContent()).isEqualTo("Hello builder");
        assertThat(dto.getUserId()).isEqualTo(55L);
        assertThat(dto.getThreadId()).isEqualTo(77L);
        assertThat(dto.getCreatedAt()).isEqualTo(timestamp);
    }
}
