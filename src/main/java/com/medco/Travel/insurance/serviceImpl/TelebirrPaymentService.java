package com.medco.Travel.insurance.serviceImpl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

@Service
public class TelebirrPaymentService {

    @Value("${telebirr.api.key}")
    private String apiKey;

    @Value("${telebirr.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    public TelebirrPaymentService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Method to initiate a payment
    public String initiatePayment(double amount, String phoneNumber) {
        // Construct the request body
        String requestBody = String.format("{\"amount\": %.2f, \"phone_number\": \"%s\"}", amount, phoneNumber);

        // Set up headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create the HTTP request
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        // Send the request to Telebirr API
        ResponseEntity<String> response = restTemplate.exchange(apiUrl + "/initiate_payment", HttpMethod.POST, entity, String.class);

        // Return the response (you can process this further based on the API response)
        return response.getBody();
    }

    // Method to check the payment status
    public String checkPaymentStatus(String transactionId) {
        String url = apiUrl + "/check_payment_status/" + transactionId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        return response.getBody();
    }
}