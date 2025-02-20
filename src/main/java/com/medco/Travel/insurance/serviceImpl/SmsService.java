package com.medco.Travel.insurance.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class SmsService {

    private final RestTemplate restTemplate;

    @Value("${afromessage.api.url}")
    private String apiUrl;

    @Value("${afromessage.api.token}")
    private String apiToken;

    @Value("${afromessage.api.identifierId}")
    private String identifierId;

    @Autowired
    public SmsService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendSms(String phoneNumber, String message) {
        // AfroMessage API request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("identifierId", identifierId);
        requestBody.put("message", message);
        requestBody.put("phoneNumber", phoneNumber);

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiToken);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        // Send request to AfroMessage API
        ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, requestEntity, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to send SMS via AfroMessage: " + response.getBody());
        }
    }
}


