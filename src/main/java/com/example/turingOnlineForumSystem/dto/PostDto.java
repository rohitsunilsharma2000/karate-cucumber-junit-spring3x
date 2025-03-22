package com.example.turingOnlineForumSystem.dto;


import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDto {
    private Long id;
    private String content;
    private Long userId;
    private Long threadId;
    private LocalDateTime createdAt;
}
