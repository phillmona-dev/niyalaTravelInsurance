package com.medco.Travel.insurance.controller;

import com.medco.Travel.insurance.dto.Request.InsurancePremiumRequest;
import com.medco.Travel.insurance.dto.Response.InsurancePremiumResponse;
import com.medco.Travel.insurance.serviceImpl.InsurancePremiumService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/insurance")
public class InsurancePremiumController {

    private final InsurancePremiumService insurancePremiumService;

    public InsurancePremiumController(InsurancePremiumService insurancePremiumService) {
        this.insurancePremiumService = insurancePremiumService;
    }

    @PostMapping("/calculate")
    public ResponseEntity<InsurancePremiumResponse> calculateInsurancePremium(
            @RequestBody InsurancePremiumRequest request) {
        InsurancePremiumResponse response = insurancePremiumService.calculatePremium(request);
        return ResponseEntity.ok(response);
    }
}

