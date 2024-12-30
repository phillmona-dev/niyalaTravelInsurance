package com.medco.Travel.insurance.serviceImpl;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {

    private final MessagingService messagingService;
    private final Map<String, String> otpStore = new ConcurrentHashMap<>();

    public OtpService(MessagingService messagingService) {
        this.messagingService = messagingService;
    }

    /**
     * Generates and sends an OTP to the specified phone number.
     *
     * @param phoneNumber the recipient's phone number
     */
    public void generateAndSendOtp(String phoneNumber) {

        String otp = generateOtp();
        otpStore.put(phoneNumber, otp);
        messagingService.sendOtp(phoneNumber, otp);

    }

    /**
     * Verifies the OTP for a given phone number.
     *
     * @param phoneNumber the phone number
     * @param otp         the OTP to verify
     * @return true if the OTP is valid, false otherwise
     */
    public boolean verifyOtp(String phoneNumber, String otp) {
        return otp.equals(otpStore.get(phoneNumber));
    }

    private String generateOtp() {
        return String.format("%06d", (int) (Math.random() * 1000000));
    }
}


