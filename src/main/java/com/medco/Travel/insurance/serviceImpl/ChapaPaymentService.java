package com.medco.Travel.insurance.serviceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medco.Travel.insurance.dto.Request.ChapaPaymentRequest;
import com.medco.Travel.insurance.dto.Response.ChapaPaymentResponse;
import com.medco.Travel.insurance.entity.PaymentTransaction;
import com.medco.Travel.insurance.repository.PaymentTransactionRepository;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ChapaPaymentService {

    private static final Logger logger = LoggerFactory.getLogger(ChapaPaymentService.class);
    @Value("${chapa.api.base-url}")
    private String baseUrl;

    @Value("${chapa.api.key}")
    private String apiKey;

    private final OkHttpClient client;

    private final PaymentTransactionRepository paymentTransactionRepository;

    public ChapaPaymentService(PaymentTransactionRepository paymentTransactionRepository) {
        this.paymentTransactionRepository = paymentTransactionRepository;
        this.client = new OkHttpClient();
    }

    public ChapaPaymentResponse initiatePayment(ChapaPaymentRequest request) throws IOException {
        MediaType mediaType = MediaType.parse("application/json");
        String jsonBody = "{"
                + "\"amount\":\"" + request.getAmount() + "\","
                + "\"currency\":\"" + request.getCurrency() + "\","
                + "\"email\":\"" + request.getEmail() + "\","
                + "\"first_name\":\"" + request.getFirstName() + "\","
                + "\"last_name\":\"" + request.getLastName() + "\","
                + "\"phone_number\":\"" + request.getPhoneNumber() + "\","
                + "\"tx_ref\":\"" + request.getTxRef() + "\","
                + "\"callback_url\":\"" + request.getCallbackUrl() + "\","
                + "\"return_url\":\"" + request.getReturnUrl() + "\","
                + "\"customization[title]\":\"" + request.getTitle() + "\","
                + "\"customization[description]\":\"" + request.getDescription() + "\","
                + "\"meta[hide_receipt]\":\"" + request.isHideReceipt() + "\""
                + "}";

        RequestBody body = RequestBody.create(mediaType, jsonBody);

        Request requestObj = new Request.Builder()
                .url(baseUrl)
                .post(body)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(requestObj).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                System.out.println("response from chapa" + " " + responseBody);
                return parseResponse(responseBody);
            } else {
                logger.error("Failed to initiate payment: HTTP Status Code: {}, Response Message: {}, Response Body: {}",
                        response.code(), response.message(), response.body().string());
                throw new RuntimeException("Failed to initiate payment: " + response.message());
            }
        } catch (IOException e) {
            logger.error("Error occurred while initiating payment: ", e);
            throw e;
        }
    }

    private void savePaymentTransaction(ChapaPaymentResponse paymentResponse) {

        PaymentTransaction transaction = new PaymentTransaction();

        try {

            BeanUtils.copyProperties(transaction, paymentResponse.getData());

            transaction.setTxRef(paymentResponse.getTxRef());
            transaction.setStatus(paymentResponse.getStatus());
            transaction.setCheckoutUrl(paymentResponse.getData().getCheckoutUrl());
            transaction.setPaymentGatewayResponse(paymentResponse.getMessage());

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
}


