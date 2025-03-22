package com.example.turingOnlineForumSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ModerationDTO {
    private Long id;
    private String action;
    private String reason;
    private LocalDateTime createdAt;
    private Long userId;
    private String username;
    private Long threadId;
}
