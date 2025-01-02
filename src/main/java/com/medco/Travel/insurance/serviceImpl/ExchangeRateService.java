package com.medco.Travel.insurance.serviceImpl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class ExchangeRateService {

    private static final String EXCHANGE_RATE_API_URL = "https://api.exchangerate-api.com/v4/latest/EUR";

    public double getEuroToBirrRate() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> response = restTemplate.getForEntity(EXCHANGE_RATE_API_URL, Map.class);
            Map<String, Object> rates = (Map<String, Object>) response.getBody().get("rates");
            return (double) rates.get("ETB");
        } catch (Exception e) {
            // Fallback rate if API fails
            return 60.0; // Example rate, adjust as needed
        }
    }
}

