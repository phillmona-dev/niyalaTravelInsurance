package com.medco.Travel.insurance.controller;

import com.medco.Travel.insurance.serviceImpl.PaymentWebhookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Map;

@RestController
@RequestMapping("/webhook")
public class ChapaWebhookController {

    private static final String SECRET_KEY = "p6446_ch4444_pa455646";
    private final PaymentWebhookService paymentWebhookService;

    public ChapaWebhookController(PaymentWebhookService paymentWebhookService) {
        this.paymentWebhookService = paymentWebhookService;
    }

    @PostMapping("/handlewebhook")
    public ResponseEntity<String> handleWebhook(
            @RequestBody Map<String, Object> payload,
            @RequestHeader(value = "x-chapa-signature", required = false) String chapaSignature) {

        // Validate signature
        if (chapaSignature == null || !verifySignature(payload, chapaSignature)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid signature");
        }

        // Extract event type
        String eventType = (String) payload.get("event");

        // Handle different webhook events
        switch (eventType) {
            case "charge.success":
                paymentWebhookService.handleSuccessfulPayment(payload);
                break;
            case "charge.failed":
                paymentWebhookService.handleFailedPayment(payload);
                break;
            case "charge.refunded":
                paymentWebhookService.handleRefundedPayment(payload);
                break;
            case "charge.reversed":
                paymentWebhookService.handleReversedPayment(payload);
                break;
            case "payout.success":
                paymentWebhookService.handleSuccessfulPayout(payload);
                break;
            case "payout.failed":
                paymentWebhookService.handleFailedPayout(payload);
                break;
            default:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unknown event type: " + eventType);
        }

        return ResponseEntity.ok("Webhook received successfully");
    }

    private boolean verifySignature(Map<String, Object> payload, String chapaSignature) {
        try {
            Mac sha256Hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "HmacSHA256");
            sha256Hmac.init(secretKeySpec);

            String payloadString = payload.toString();
            byte[] hashBytes = sha256Hmac.doFinal(payloadString.getBytes());
            String computedSignature = Base64.getEncoder().encodeToString(hashBytes);

            return computedSignature.equals(chapaSignature);
        } catch (Exception e) {
            return false;
        }
    }
}
