package com.medco.Travel.insurance.controller;

import com.medco.Travel.insurance.dto.Request.ChapaPaymentRequest;
import com.medco.Travel.insurance.dto.Response.ChapaPaymentResponse;
import com.medco.Travel.insurance.entity.InsurancePremium;
import com.medco.Travel.insurance.repository.InsurancePremiumRepository;
import com.medco.Travel.insurance.serviceImpl.ChapaPaymentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/travel/payments")
public class PaymentController {

    private final ChapaPaymentService paymentService;
    private final InsurancePremiumRepository insurancePremiumRepository;

    @Value("${chapa.api.callback-url}")
    private String callbackUrl;

    @Value("${chapa.api.return-url}")
    private String returnUrl;

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
        paymentRequest.setCallbackUrl(callbackUrl);
//        paymentRequest.setReturnUrl(returnUrl);
        paymentRequest.setTxRef(referenceCode);

        ChapaPaymentResponse response = paymentService.initiatePayment(paymentRequest);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/callback")
    public ResponseEntity<String> handleCallback(@RequestBody Map<String, Object> callbackData) throws IOException {
        // Expecting tx_ref or txRef from gateway callback
        String txRef = Optional.ofNullable((String) callbackData.get("tx_ref"))
                .orElse((String) callbackData.get("txRef"));

        if (txRef == null || txRef.isEmpty()) {
            return ResponseEntity.badRequest().body("Missing tx_ref in callback payload");
        }

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


