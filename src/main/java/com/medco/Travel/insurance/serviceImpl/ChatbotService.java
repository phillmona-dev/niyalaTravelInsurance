package com.medco.Travel.insurance.serviceImpl;

import com.medco.Travel.insurance.entity.Claim;
import com.medco.Travel.insurance.repository.ClaimRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatbotService {

    private final RestTemplate restTemplate;
    private final ClaimRepository claimRepository;

    @Value("${openai.api.key}")
    private String openAiApiKey;

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    public String askChatbot(String userMessage, String phoneNumber) {
        // Retrieve claim details based on phone number
        Claim claim = (Claim) claimRepository.findByTelephoneNumber(phoneNumber)
                .orElse(null);

        String claimInfo = claim != null ? formatClaimDetails(claim) : "No claim found.";

        // Prepare message for GPT
        String fullMessage = "User query: " + userMessage + "\n\nClaim Details:\n" + claimInfo;

        // Create OpenAI API request
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openAiApiKey);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-4");
        requestBody.put("messages", List.of(Map.of("role", "user", "content", fullMessage)));
        requestBody.put("temperature", 0.7);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(OPENAI_API_URL, request, Map.class);

        // Extract response text
        List<Map<String, String>> choices = (List<Map<String, String>>) response.getBody().get("choices");
        return choices.get(0).get("message");
    }

    private String formatClaimDetails(Claim claim) {
        return "Claim ID: " + claim.getId() + "\n" +
                "Status: " + claim.getStatus() + "\n" +
                "Policy Number: " + claim.getPolicy().getPolicyNumber() + "\n" +
                "Submitted At: " + claim.getSubscriptionDate() + "\n" +
                "Decision: " + claim.getRejectionReason();
    }
}

