package com.example.turingOnlineForumSystem.dto;


import lombok.Data;

@Data
public class ChatMessageDTO {
    private Long senderId;
    private Long receiverId;
    private String content;
}
