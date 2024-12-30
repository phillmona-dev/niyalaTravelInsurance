package com.medco.Travel.insurance.serviceImpl;

import com.medco.Travel.insurance.entity.Passenger;
import com.medco.Travel.insurance.entity.Destination;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@Service
public class MapfreApiClient {

    @Value("${mapfre.api.key}")
    private String apiKey;

    @Value("${mapfre.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Calls the MAPFRE API to calculate the premium.
     *
     * @param passenger       the user details
     * @param destination the destination details
     * @param duration   the duration of the trip
     * @return the calculated premium
     */
    public double calculatePremium(Passenger passenger, Destination destination, int duration) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("userAge", passenger.getAge());
        requestBody.put("destination", destination.getCountryName());
        requestBody.put("duration", duration);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, entity, Map.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> responseBody = response.getBody();
            return (double) responseBody.get("premium");
        } else {
            throw new RuntimeException("Failed to calculate premium with MAPFRE API");
        }
    }
}
