package com.example.turingOnlineForumSystem.dto;


import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class ModerationDTOTest {

    @Test
    void testAllArgsConstructorAndGetters() {
        LocalDateTime now = LocalDateTime.now();

        ModerationDTO dto = new ModerationDTO(
                1L,
                "DELETE",
                "Spam content",
                now,
                2L,
                "john_doe",
                3L
        );

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getAction()).isEqualTo("DELETE");
        assertThat(dto.getReason()).isEqualTo("Spam content");
        assertThat(dto.getCreatedAt()).isEqualTo(now);
        assertThat(dto.getUserId()).isEqualTo(2L);
        assertThat(dto.getUsername()).isEqualTo("john_doe");
        assertThat(dto.getThreadId()).isEqualTo(3L);
    }

    @Test
    void testSettersAndNoArgsConstructor() {
        ModerationDTO dto = new ModerationDTO();

        dto.setId(10L);
        dto.setAction("BAN");
        dto.setReason("Toxic behavior");
        dto.setCreatedAt(LocalDateTime.of(2024, 1, 1, 12, 0));
        dto.setUserId(100L);
        dto.setUsername("alice");
        dto.setThreadId(200L);

        assertThat(dto.getId()).isEqualTo(10L);
        assertThat(dto.getAction()).isEqualTo("BAN");
        assertThat(dto.getReason()).isEqualTo("Toxic behavior");
        assertThat(dto.getCreatedAt()).isEqualTo("2024-01-01T12:00");
        assertThat(dto.getUserId()).isEqualTo(100L);
        assertThat(dto.getUsername()).isEqualTo("alice");
        assertThat(dto.getThreadId()).isEqualTo(200L);
    }
}
