package com.medco.Travel.insurance.serviceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medco.Travel.insurance.dto.Request.ChapaPaymentRequest;
import com.medco.Travel.insurance.dto.Response.ChapaPaymentResponse;
import com.medco.Travel.insurance.entity.InsurancePremium;
import com.medco.Travel.insurance.entity.Passenger;
import com.medco.Travel.insurance.entity.PaymentTransaction;
import com.medco.Travel.insurance.repository.InsurancePremiumRepository;
import com.medco.Travel.insurance.repository.PaymentTransactionRepository;
import com.medco.Travel.insurance.repository.PremiumRepository;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ChapaPaymentService {

    private static final Logger logger = LoggerFactory.getLogger(ChapaPaymentService.class);
    @Value("${chapa.api.base-url}")
    private String baseUrl;

    @Value("${chapa.api.key}")
    private String apiKey;

    private final OkHttpClient client;

    private final PaymentTransactionRepository paymentTransactionRepository;
    private final PremiumRepository premiumRepository;
    private final InsurancePremiumRepository insurancePremiumRepository;

    public ChapaPaymentService(PaymentTransactionRepository paymentTransactionRepository, PremiumRepository premiumRepository, InsurancePremiumRepository insurancePremiumRepository) {
        this.paymentTransactionRepository = paymentTransactionRepository;
        this.premiumRepository = premiumRepository;
        this.insurancePremiumRepository = insurancePremiumRepository;
        this.client = new OkHttpClient();
    }

    public ChapaPaymentResponse initiatePayment(ChapaPaymentRequest request) throws IOException {
        MediaType mediaType = MediaType.parse("application/json");

        // Convert request to JSON format
        String jsonBody = new ObjectMapper().writeValueAsString(request);

        RequestBody body = RequestBody.create(mediaType, jsonBody);

        Request requestObj = new Request.Builder()
                .url(baseUrl)
                .post(body)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(requestObj).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                System.out.println("response body: ooooooooooooooooooooo: " + responseBody);
                logger.info("Payment initiated successfully: {}", responseBody);

              ChapaPaymentResponse paymentResponse = parseResponse(responseBody);
              paymentResponse.setStatus("PENDING");
              savePaymentTransaction(paymentResponse, request);
              return paymentResponse;
            } else {
                String errorMessage = response.body() != null ? response.body().string() : "No response body";
                logger.error("Failed to initiate payment: HTTP Status Code: {}, Response Message: {}, Response Body: {}",
                        response.code(), response.message(), errorMessage);
                throw new RuntimeException("Failed to initiate payment: " + response.message());
            }
        } catch (IOException e) {
            logger.error("Error occurred while initiating payment: ", e);
            throw e;
        }
    }


    private void savePaymentTransaction(ChapaPaymentResponse paymentResponse, ChapaPaymentRequest request) {

        PaymentTransaction transaction = new PaymentTransaction();
        try {

            if (paymentResponse.getData()==null){
                throw new RuntimeException("Payment data is null. cannot save transactions.");
            }

//            Optional<InsurancePremium> insurancePremiumOpt = insurancePremiumRepository.findByReferenceCode(paymentResponse.getTxRef());
//            System.out.println("insurancePremium mmmmmmmmm" + insurancePremiumOpt);
//            if (insurancePremiumOpt.isEmpty()){
//                throw new RuntimeException("No insurance premium found for txRef" + " " + paymentResponse.getTxRef());
//            }
//            InsurancePremium insurancePremium = insurancePremiumOpt.get();
//            transaction.setInsurancePremium(insurancePremium);
          BeanUtils.copyProperties(transaction, paymentResponse.getData());

            transaction.setTxRef(request.getTxRef());
            transaction.setStatus(paymentResponse.getStatus() != null ? paymentResponse.getStatus() : "PENDING");
//            transaction.setCheckoutUrl(paymentResponse.getData().getCheckoutUrl());
            transaction.setPaymentGatewayResponse(paymentResponse.getMessage());
            transaction.setAmount(String.valueOf(request.getAmount()));
            transaction.setCurrency(request.getCurrency());
            transaction.setCreatedAt(LocalDateTime.now());
            transaction.setEmail(request.getEmail());
            transaction.setFirstName(request.getFirstName());
            transaction.setLastName(request.getLastName());

            paymentTransactionRepository.save(transaction);

            logger.info("Payment transaction saved successfully: {}", transaction);
        } catch (Exception e) {
            logger.error("Unexpected error occurred while saving the payment transaction: {}", e.getMessage());
            throw new RuntimeException("Unexpected error occurred while saving the payment transaction", e);
        }
    }

    public boolean verifyTransaction(String txRef) throws IOException {
        Request request = new Request.Builder()
                .url(baseUrl + "/v1/transaction/verify/" + txRef)
                .get()
                .addHeader("Authorization", "Bearer " + apiKey)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();

                return responseBody.contains("\"status\":\"success\"");
            } else {
                throw new RuntimeException("Failed to verify transaction: " + response.message());
            }
        }
    }

    private ObjectMapper objectMapper = new ObjectMapper();

    private ChapaPaymentResponse parseResponse(String responseBody) {
        try {

            ChapaPaymentResponse response = objectMapper.readValue(responseBody, ChapaPaymentResponse.class);

            if (response == null || response.getStatus() == null || response.getMessage() == null || response.getData() == null || response.getData().getCheckoutUrl() == null) {
                logger.error("Invalid response data: " + responseBody);
                throw new RuntimeException("Invalid response from payment service: Missing required fields.");
            }

            return response;
        } catch (IOException e) {
            logger.error("Error occurred while parsing the response: " + responseBody, e);
            throw new RuntimeException("Error occurred while parsing the payment response", e);
        }
    }

    public ChapaPaymentRequest createPaymentRequest(InsurancePremium premium) {
        // Retrieve the passenger associated with the premium
        Passenger passenger = premium.getPassenger();

        return ChapaPaymentRequest.builder()
                .amount(premium.getPremiumAmount())
                .currency("ETB")
                .email(passenger.getEmail())
                .firstName(passenger.getFirstName())
                .lastName(passenger.getLastName())
                .phoneNumber(passenger.getTelephone())
                .txRef("tx-" + premium.getReferenceCode() + "-" + System.currentTimeMillis())  // Unique transaction reference
                .callbackUrl("http://192.168.100.82:8900/api/payments/callback")
                .returnUrl("http://192.168.100.82:8900/api/payments/payment-success")
                .title("Insurance Payment")
                .description("Payment for Travel Insurance Premium")
                .hideReceipt(false)
                .build();
    }


}


