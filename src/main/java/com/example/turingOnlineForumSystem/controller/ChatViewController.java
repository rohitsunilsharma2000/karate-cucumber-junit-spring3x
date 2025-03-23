package com.example.turingOnlineForumSystem.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ChatViewController {

    /**
     * Endpoint to render the chat UI page using Thymeleaf.
     *
     * @param userId The ID of the current user (from query param or session).
     * @param model  The model to inject into Thymeleaf view.
     * @return chat.html page.
     */
    @GetMapping("/chat")
    public String chatPage(@RequestParam Long userId, Model model) {
        model.addAttribute("userId", userId);  // Inject user ID into the frontend
        return "chat";  // Loads templates/chat.html
    }
}
