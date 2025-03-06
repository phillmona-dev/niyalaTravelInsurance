package com.medco.Travel.insurance.serviceImpl;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class PaymentWebhookService {

    private static final String OPENFN_WEBHOOK_URL = "http://localhost:4000/i/90360758-618c-4c77-8efd-e52d867ef5da";
    private final RestTemplate restTemplate;

    public PaymentWebhookService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void handleSuccessfulPayment(Map<String, Object> payload) {
        System.out.println("✅ Payment Successful: " + payload);
        sendToOpenFn(payload);
        // TODO: Update order status in the database
    }

    public void handleFailedPayment(Map<String, Object> payload) {
        System.out.println("❌ Payment Failed: " + payload);
        sendToOpenFn(payload);
        // TODO: Notify the user and update the transaction status
    }

    public void handleRefundedPayment(Map<String, Object> payload) {
        System.out.println("🔄 Payment Refunded: " + payload);
        sendToOpenFn(payload);
        // TODO: Update refund status in the database
    }

    public void handleReversedPayment(Map<String, Object> payload) {
        System.out.println("↩ Payment Reversed: " + payload);
        sendToOpenFn(payload);
        // TODO: Rollback the transaction and notify the user
    }

    public void handleSuccessfulPayout(Map<String, Object> payload) {
        System.out.println("✅ Payout Successful: " + payload);
        sendToOpenFn(payload);
        // TODO: Update payout records in the database
    }

    public void handleFailedPayout(Map<String, Object> payload) {
        System.out.println("❌ Payout Failed: " + payload);
        sendToOpenFn(payload);
        // TODO: Notify admin and take necessary actions
    }

    private void sendToOpenFn(Map<String, Object> payload) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(payload, headers);

            ResponseEntity<String> response = restTemplate.exchange(OPENFN_WEBHOOK_URL, HttpMethod.POST, requestEntity, String.class);

            System.out.println("📡 Sent to OpenFn: " + response.getBody());
        } catch (Exception e) {
            System.err.println("❌ Error sending to OpenFn: " + e.getMessage());
        }
    }
}