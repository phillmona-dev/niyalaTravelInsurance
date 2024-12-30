package com.medco.Travel.insurance.controller;

import com.medco.Travel.insurance.entity.Claim;
import com.medco.Travel.insurance.entity.Policy;
import com.medco.Travel.insurance.serviceImpl.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/travel/reports")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/policy-report")
    public ResponseEntity<List<Policy>> generatePolicyReport(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        List<Policy> policies = reportService.generatePolicyReport(startDate, endDate);
        return ResponseEntity.ok(policies);
    }

    @GetMapping("/claim-report")
    public ResponseEntity<List<Claim>> generateClaimReport(
            @RequestParam Long policyId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        List<Claim> claims = reportService.generateClaimReport(policyId, startDate, endDate);
        return ResponseEntity.ok(claims);
    }
}
