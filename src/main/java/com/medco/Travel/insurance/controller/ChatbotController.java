package com.medco.Travel.insurance.controller;

import com.medco.Travel.insurance.dto.Request.ChatRequest;
import com.medco.Travel.insurance.serviceImpl.ChatbotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chatbot")
@RequiredArgsConstructor
public class ChatbotController {

    private final ChatbotService chatbotService;

    @PostMapping("/ask")
    public ResponseEntity<String> chat(@RequestBody ChatRequest chatRequest) {
        String response = chatbotService.askChatbot(chatRequest.getMessage(), chatRequest.getPhoneNumber());
        return ResponseEntity.ok(response);
    }
}

