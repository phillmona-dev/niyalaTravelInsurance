package com.medco.Travel.insurance.controller;

import com.medco.Travel.insurance.dto.Request.ChapaPaymentRequest;
import com.medco.Travel.insurance.dto.Response.ChapaPaymentResponse;
import com.medco.Travel.insurance.entity.InsurancePremium;
import com.medco.Travel.insurance.repository.InsurancePremiumRepository;
import com.medco.Travel.insurance.serviceImpl.ChapaPaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/travel/payments")
public class PaymentController {

    private final ChapaPaymentService paymentService;
    private final InsurancePremiumRepository insurancePremiumRepository;

    public PaymentController(ChapaPaymentService paymentService, InsurancePremiumRepository insurancePremiumRepository) {

        this.paymentService = paymentService;
        this.insurancePremiumRepository = insurancePremiumRepository;
    }

    @PostMapping("/initiate/{referenceCode}")
    public ResponseEntity<ChapaPaymentResponse> initiatePayment(@PathVariable String referenceCode) throws IOException {
        // Fetch premium using referenceCode
        InsurancePremium premium = insurancePremiumRepository.findUnpaidPremiumByReferenceCode(referenceCode)
                .orElseThrow(() -> new RuntimeException("No unpaid premium found for reference code: " + referenceCode));

        ChapaPaymentRequest paymentRequest = paymentService.createPaymentRequest(premium);
        paymentRequest.setCallbackUrl("http://192.168.100.82:8900/api/payments/callback");
        paymentRequest.setReturnUrl("http://192.168.100.82:8900/api/payments/payment-success");
        paymentRequest.setTxRef(referenceCode);

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

    @PostMapping("/redirect")
    @ResponseStatus(HttpStatus.CREATED)
    public RedirectView redirectToCheckout(@RequestBody Map<String, Object> requestData) {
        String checkoutUrl = (String) requestData.get("checkout_url");
        if (checkoutUrl == null || checkoutUrl.isEmpty()) {
            throw new IllegalArgumentException("Checkout URL is required");
        }
        return new RedirectView(checkoutUrl);
    }
}


