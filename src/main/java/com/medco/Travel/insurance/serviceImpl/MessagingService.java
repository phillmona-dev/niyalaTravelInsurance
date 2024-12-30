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

    @Value("${afromessage.api.senderName}")
    private String senderName;

    @Value("${afromessage.api.callbackUrl}")
    private String callbackUrl;

    private final OkHttpClient client;
    private final OtpRepository otpRepository;

    private static final long OTP_EXPIRY_MINUTES = 5;  // OTP validity duration in minutes

    public MessagingService(OtpRepository otpRepository) {
        this.client = new OkHttpClient();
        this.otpRepository = otpRepository;
    }

    /**
     * Sends an OTP message to the specified phone number using AfroMessage API.
     *
     * @param phoneNumber the recipient's phone number
     * @param otp         the OTP to send
     */
    public void sendOtp(String phoneNumber, String otp) {
        // Check if there's an existing OTP and if it's expired
        Optional<Otp> existingOtp = otpRepository.findByPhoneNumber(phoneNumber);

        if (existingOtp.isPresent()) {
            Otp otpEntity = existingOtp.get();

            // Check if OTP has expired
            if (otpEntity.getExpiryDate().isAfter(LocalDateTime.now())) {
                System.out.println("OTP is still valid, not sending a new one.");
                return;  // Skip sending a new OTP if the existing one is still valid
            } else {
                // If OTP expired, delete the old OTP record
                otpRepository.delete(otpEntity);
                System.out.println("OTP expired, generating a new one.");
            }
        }

        // Generate a new OTP
        String newOtp = generateOtp();

        // Save the new OTP with expiry time in the database
        Otp otpEntity = new Otp();
        otpEntity.setPhoneNumber(phoneNumber);
        otpEntity.setOtp(newOtp);
        otpEntity.setExpiryDate(LocalDateTime.now().plus(OTP_EXPIRY_MINUTES, ChronoUnit.MINUTES));
        otpRepository.save(otpEntity);

        // Build the URL with required parameters
        HttpUrl.Builder urlBuilder = HttpUrl.parse(apiUrl).newBuilder();
        urlBuilder.addQueryParameter("to", phoneNumber);
        urlBuilder.addQueryParameter("message", "Your OTP is: " + newOtp);
        urlBuilder.addQueryParameter("callback", callbackUrl);
        urlBuilder.addQueryParameter("from", identifierId);
        urlBuilder.addQueryParameter("sender", senderName);

        String url = urlBuilder.build().toString();

        // Build the request
        Request request = new Request.Builder()
                .header("Authorization", "Bearer " + apiToken)
                .url(url)
                .build();

        // Dispatch the request asynchronously
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Handle failure (e.g., log the error or retry logic)
                System.err.println("Error sending OTP: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Handle the response from AfroMessage API
                if (!response.isSuccessful()) {
                    // Handle unsuccessful response (e.g., log the error or alert)
                    System.err.println("Failed to send OTP: " + response.body().string());
                } else {
                    // Process the successful response (e.g., check for acknowledgment or status)
                    System.out.println("OTP sent successfully: " + response.body().string());
                }
            }
        });
    }

    /**
     * Generates a random 6-digit OTP.
     *
     * @return OTP as a String
     */
    private String generateOtp() {
        return String.format("%06d", (int) (Math.random() * 1000000));
    }

}
