package com.medco.Travel.insurance.controller;

import com.medco.Travel.insurance.dto.Request.PaymentRequest;
import com.medco.Travel.insurance.serviceImpl.TelebirrPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/travel/payments")
public class PaymentController {

    private final TelebirrPaymentService telebirrPaymentService;

    @Autowired
    public PaymentController(TelebirrPaymentService telebirrPaymentService) {
        this.telebirrPaymentService = telebirrPaymentService;
    }

    @PostMapping("/initiate")
    public ResponseEntity<String> initiatePayment(@RequestBody PaymentRequest paymentRequest) {
        try {
            // Call the Telebirr service to initiate the payment
            String response = telebirrPaymentService.initiatePayment(paymentRequest.getAmount(), paymentRequest.getPhoneNumber());
            return ResponseEntity.ok(response);  // Return the response from Telebirr
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Payment initiation failed: " + e.getMessage());
        }
    }

    // Endpoint to check payment status
    @GetMapping("/status/{transactionId}")
    public ResponseEntity<String> checkPaymentStatus(@PathVariable String transactionId) {
        try {
            // Call the Telebirr service to check the payment status
            String response = telebirrPaymentService.checkPaymentStatus(transactionId);
            return ResponseEntity.ok(response);  // Return the payment status
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to check payment status: " + e.getMessage());
        }
    }
}
