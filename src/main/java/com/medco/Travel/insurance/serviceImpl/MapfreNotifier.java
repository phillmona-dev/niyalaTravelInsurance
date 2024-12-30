package com.medco.Travel.insurance.serviceImpl;

import com.medco.Travel.insurance.entity.Destination;
import com.medco.Travel.insurance.entity.Passenger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class MapfreNotifier {

    @Value("${mapfre.api.key}")
    private String apiKey;

    @Value("${mapfre.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Notifies Mapfre about the calculated premium.
     *
     * @param passenger   the passenger details
     * @param destination the destination details
     * @param premium     the calculated premium
     */
    public void notifyMapfre(Passenger passenger, Destination destination, double premium) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("userAge", passenger.getAge());
        requestBody.put("destination", destination.getCountryName());
        requestBody.put("premium", premium);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, entity, Map.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to notify Mapfre");
        }
    }
}

