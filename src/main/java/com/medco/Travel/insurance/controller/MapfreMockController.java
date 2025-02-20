package com.medco.Travel.insurance.controller;

import com.medco.Travel.insurance.dto.Response.MapfreResponse;
import com.medco.Travel.insurance.entity.Claim;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/mock/mapfre")
public class MapfreMockController {

    private final Random random = new Random();

    @PostMapping("/process-claim")
    public ResponseEntity<MapfreResponse> processClaim(@RequestBody Claim claim) {
        // Simulating different claim responses
        String[] statuses = {"APPROVED", "REJECTED", "PENDING"};
        String randomStatus = statuses[random.nextInt(statuses.length)];

        // Create a response
        MapfreResponse response = new MapfreResponse(randomStatus, "Claim processed successfully.");

        return ResponseEntity.ok(response);
    }
}


