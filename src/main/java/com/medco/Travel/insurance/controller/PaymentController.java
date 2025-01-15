package com.medco.Travel.insurance.controller;

import com.medco.Travel.insurance.dto.Request.ChapaPaymentRequest;
import com.medco.Travel.insurance.dto.Response.ChapaPaymentResponse;
import com.medco.Travel.insurance.serviceImpl.ChapaPaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/travel/payments")
public class PaymentController {

    private final ChapaPaymentService paymentService;

    public PaymentController(ChapaPaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/initiate")
    public ResponseEntity<ChapaPaymentResponse> initiatePayment(@RequestBody ChapaPaymentRequest paymentRequest) throws IOException {
        paymentRequest.setCallbackUrl("http://192.168.100.82:8900/api/payments/callback");
        paymentRequest.setReturnUrl("http://192.168.100.82:8900/api/payments/payment-success");
        paymentRequest.setTxRef("unique-tx-ref-" + System.currentTimeMillis());
        ChapaPaymentResponse response = paymentService.initiatePayment(paymentRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/callback")
    public ResponseEntity<String> handleCallback(@RequestBody String callbackData) throws IOException {
        // Parse the callbackData if needed and verify the transaction
        // Extract txRef from the callbackData
        String txRef = "extracted-tx-ref"; // Replace with actual logic to extract txRef
        boolean isVerified = paymentService.verifyTransaction(txRef);
        return ResponseEntity.ok(isVerified ? "Payment verified" : "Payment verification failed");
    }

    // Step 2: Handle Return URL after Payment Success
    @GetMapping("/payment-success")
    public String handlePaymentSuccess(@RequestParam String tx_ref, @RequestParam String status) {
        return "Payment Status: " + status;
    }
}


