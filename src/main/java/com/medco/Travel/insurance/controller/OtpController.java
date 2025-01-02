package com.medco.Travel.insurance.controller;

import com.medco.Travel.insurance.dto.Request.OtpRequest;
import com.medco.Travel.insurance.dto.Request.OtpVerificationRequest;
import com.medco.Travel.insurance.serviceImpl.MessagingService;
import com.medco.Travel.insurance.serviceImpl.OtpService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/travel/otp")
public class OtpController {

    private static final Logger logger = LoggerFactory.getLogger(OtpController.class);

    private final OtpService otpService;

    @Autowired
    public OtpController(OtpService otpService, MessagingService messagingService) {
        this.otpService = otpService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendOtp(@RequestBody OtpRequest otpRequest, HttpServletRequest request) {
        try {
            // Log incoming request details
            logger.info("Incoming request to /send endpoint.");
            logger.info("Request Method: {}", request.getMethod());
            logger.info("Request URI: {}", request.getRequestURI());
            logger.info("Headers: {}", Collections.list(request.getHeaderNames())
                    .stream()
                    .map(headerName -> headerName + ": " + request.getHeader(headerName))
                    .toList());
            logger.info("Payload (phoneNumber): {}", otpRequest.getPhoneNumber());

            // Generate and send OTP
            otpService.generateAndSendOtp(otpRequest.getPhoneNumber());

            logger.info("OTP sent successfully to {}", otpRequest.getPhoneNumber());

            return ResponseEntity.ok("OTP sent successfully to " + otpRequest.getPhoneNumber());
        } catch (Exception e) {
            logger.error("Error sending OTP to {}: {}", otpRequest.getPhoneNumber(), e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send OTP to " + otpRequest.getPhoneNumber() + ". Error: " + e.getMessage());
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyOtp(@RequestBody OtpVerificationRequest otpVerificationRequest) {
        try {
            // Extract phone number and OTP from the request body
            String phoneNumber = otpVerificationRequest.getPhoneNumber();
            String otp = otpVerificationRequest.getOtp();

            // Verify the OTP
            boolean isValid = otpService.verifyOtp(phoneNumber, otp);

            if (isValid) {
                return ResponseEntity.ok("OTP verified successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired OTP.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while verifying OTP: " + e.getMessage());
        }
    }

}

