package com.medco.Travel.insurance.serviceImpl;

import com.medco.Travel.insurance.entity.Otp;
import com.medco.Travel.insurance.repository.OtpRepository;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class MessagingService {

    @Value("${afromessage.api.url}")
    private String apiUrl;

    @Value("${afromessage.api.token}")
    private String apiToken;

    @Value("${afromessage.api.identifierId}")
    private String identifierId;

    private final OkHttpClient client;
    private final OtpRepository otpRepository;

    private static final long OTP_EXPIRY_MINUTES = 5; // OTP validity duration in minutes

    public MessagingService(OtpRepository otpRepository) {
        this.client = new OkHttpClient();
        this.otpRepository = otpRepository;
    }

    /**
     * Sends an OTP message to the specified phone number using AfroMessage API.
     *
     * @param phoneNumber the recipient's phone number
     * @throws RuntimeException if the OTP sending fails
     */
    public void sendOtp(String phoneNumber, String otp) {
        // Check if there's an existing OTP and if it's expired
        Optional<Otp> existingOtp = otpRepository.findByPhoneNumber(phoneNumber);

        if (existingOtp.isPresent()) {
            Otp otpEntity = existingOtp.get();

            // Check if OTP has expired
            if (otpEntity.getExpiryDate().isAfter(LocalDateTime.now())) {
                System.out.println("OTP is still valid, not sending a new one.");
                return; // Skip sending a new OTP if the existing one is still valid
            } else {
                // If OTP expired, delete the old OTP record
                otpRepository.delete(otpEntity);
                System.out.println("OTP expired, generating a new one.");
            }
        }

        // Generate a new OTP
        String newOtp = generateOtp();



        // Prepare the JSON payload
        String messageContent = "Your OTP is: " + newOtp;
        String jsonPayload = "{"
                + "\"from\":\"" + identifierId + "\","
                + "\"to\":\"" + phoneNumber + "\","
                + "\"message\":\"" + messageContent + "\""
                + "}";

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonPayload);

        // Build the request
        Request request = new Request.Builder()
                .header("Authorization", "Bearer " + apiToken)
                .url(apiUrl)
                .post(body)
                .build();

        // Dispatch the request synchronously to handle errors properly
        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();

            if (!response.isSuccessful() || responseBody.contains("\"acknowledge\":\"error\"")) {
                throw new RuntimeException("Error from AfroMessage API: " + responseBody);
            }

            // Save the new OTP with expiry time in the database only after successful response
            Otp otpEntity = new Otp();
            otpEntity.setPhoneNumber(phoneNumber);
            otpEntity.setOtp(newOtp);
            otpEntity.setExpiryDate(LocalDateTime.now().plus(OTP_EXPIRY_MINUTES, ChronoUnit.MINUTES));
            otpRepository.save(otpEntity);

            System.out.println("OTP sent successfully to " + phoneNumber);
        } catch (IOException e) {
            throw new RuntimeException("Error sending OTP: " + e.getMessage(), e);
        }
    }

    /**
     * Generates a random 6-digit OTP.
     *
     * @return OTP as a String
     */
    private String generateOtp() {
        return String.format("%04d", (int) (Math.random() * 10000));
    }
}


