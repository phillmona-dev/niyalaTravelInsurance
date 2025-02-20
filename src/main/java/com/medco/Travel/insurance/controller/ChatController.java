package com.medco.Travel.insurance.controller;

import com.medco.Travel.insurance.serviceImpl.OpenAIService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chatbot/gpt")
public class ChatController {

    private final OpenAIService openAIService;

    public ChatController(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    @PostMapping
    public String chatWithGPT(@RequestParam String message) {
        return openAIService.chatWithGPT(message);
    }
}

