package com.medco.Travel.insurance.serviceImpl;

import com.medco.Travel.insurance.entity.Otp;
import com.medco.Travel.insurance.repository.OtpRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {

    private final MessagingService messagingService;
    private final OtpRepository otpRepository;
    private final Map<String, String> otpStore = new ConcurrentHashMap<>();

    public OtpService(MessagingService messagingService, OtpRepository otpRepository) {
        this.messagingService = messagingService;
        this.otpRepository = otpRepository;
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
        Optional<Otp> otpEntity = otpRepository.findByPhoneNumber(phoneNumber);

        if (otpEntity.isPresent()) {
            Otp existingOtp = otpEntity.get();

            // Check if OTP has expired
            if (existingOtp.getExpiryDate().isBefore(LocalDateTime.now())) {
                otpRepository.delete(existingOtp); // Clean up expired OTP
                System.out.println("OTP expired for phone number: " + phoneNumber);
                return false; // OTP is invalid due to expiry
            }

            // Check if the OTP matches
            if (existingOtp.getOtp().equals(otp)) {
                otpRepository.delete(existingOtp); // Clean up after successful verification
                System.out.println("OTP verified successfully for phone number: " + phoneNumber);
                return true;
            }
        }

        System.out.println("Invalid OTP for phone number: " + phoneNumber);
        return false; // OTP is invalid
    }

    private String generateOtp() {

        return String.format("%04d", (int) (Math.random() * 10000));
    }
}


