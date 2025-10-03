package com.medco.Travel.insurance.controller;

import com.medco.Travel.insurance.entity.Claim;
import com.medco.Travel.insurance.entity.Policy;
import com.medco.Travel.insurance.serviceImpl.ReportService;
import com.medco.Travel.insurance.shared.audit.enums.ClaimStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Generate Policy Report", description = "Generate a report of all policies created within a date range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Policy report generated successfully")
    })
    @GetMapping("/policy-report")
    public ResponseEntity<List<Policy>> generatePolicyReport(
            @Parameter(description = "Start date for the report (YYYY-MM-DD)", required = true)
            @RequestParam LocalDate startDate,
            @Parameter(description = "End date for the report (YYYY-MM-DD)", required = true)
            @RequestParam LocalDate endDate) {
        List<Policy> policies = reportService.generatePolicyReport(startDate, endDate);
        return ResponseEntity.ok(policies);
    }

    @Operation(summary = "Generate Claims Report by Date Range",
               description = "Generate a comprehensive report of all claims submitted within a date range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Claims report generated successfully")
    })
    @GetMapping("/claim-report")
    public ResponseEntity<List<Claim>> generateClaimReport(
            @Parameter(description = "Start date for the report (YYYY-MM-DD)", required = true)
            @RequestParam LocalDate startDate,
            @Parameter(description = "End date for the report (YYYY-MM-DD)", required = true)
            @RequestParam LocalDate endDate) {
        List<Claim> claims = reportService.generateClaimReportByDateRange(startDate, endDate);
        return ResponseEntity.ok(claims);
    }

    @Operation(summary = "Generate Claims Report with Filters",
               description = "Generate a detailed claims report with optional filters for policy ID and claim status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Filtered claims report generated successfully")
    })
    @GetMapping("/claim-report-filtered")
    public ResponseEntity<List<Claim>> generateClaimReportWithFilters(
            @Parameter(description = "Start date for the report (YYYY-MM-DD)", required = true)
            @RequestParam LocalDate startDate,
            @Parameter(description = "End date for the report (YYYY-MM-DD)", required = true)
            @RequestParam LocalDate endDate,
            @Parameter(description = "Optional policy ID to filter by specific policy")
            @RequestParam(required = false) Long policyId,
            @Parameter(description = "Optional claim status to filter by (PENDING, APPROVED, REJECTED, etc.)")
            @RequestParam(required = false) ClaimStatus status) {
        List<Claim> claims = reportService.generateClaimReportWithFilters(startDate, endDate, policyId, status);
        return ResponseEntity.ok(claims);
    }

    @Operation(summary = "Generate Claims Report by Policy Number",
               description = "Generate a claims report for a specific policy using policy number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Policy-specific claims report generated successfully")
    })
    @GetMapping("/claim-report-by-policy")
    public ResponseEntity<List<Claim>> generateClaimReportByPolicyNumber(
            @Parameter(description = "Policy number to filter claims", required = true)
            @RequestParam String policyNumber,
            @Parameter(description = "Start date for the report (YYYY-MM-DD)", required = true)
            @RequestParam LocalDate startDate,
            @Parameter(description = "End date for the report (YYYY-MM-DD)", required = true)
            @RequestParam LocalDate endDate) {
        List<Claim> claims = reportService.generateClaimReportByPolicyNumber(policyNumber, startDate, endDate);
        return ResponseEntity.ok(claims);
    }

    @Operation(summary = "Generate Claims Report by Status",
               description = "Generate a claims report filtered by claim status within a date range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status-filtered claims report generated successfully")
    })
    @GetMapping("/claim-report-by-status")
    public ResponseEntity<List<Claim>> generateClaimReportByStatus(
            @Parameter(description = "Start date for the report (YYYY-MM-DD)", required = true)
            @RequestParam LocalDate startDate,
            @Parameter(description = "End date for the report (YYYY-MM-DD)", required = true)
            @RequestParam LocalDate endDate,
            @Parameter(description = "Claim status to filter by", required = true)
            @RequestParam ClaimStatus status) {
        List<Claim> claims = reportService.generateClaimReportByStatus(startDate, endDate, status);
        return ResponseEntity.ok(claims);
    }

    // Legacy endpoint - kept for backward compatibility
    @Operation(summary = "Generate Claims Report by Policy ID (Legacy)",
               description = "Legacy endpoint: Generate claims report for a specific policy ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Legacy claims report generated successfully")
    })
    @GetMapping("/claim-report-legacy")
    public ResponseEntity<List<Claim>> generateClaimReportLegacy(
            @Parameter(description = "Policy ID to filter claims", required = true)
            @RequestParam Long policyId,
            @Parameter(description = "Start date for the report (YYYY-MM-DD)", required = true)
            @RequestParam LocalDate startDate,
            @Parameter(description = "End date for the report (YYYY-MM-DD)", required = true)
            @RequestParam LocalDate endDate) {
        List<Claim> claims = reportService.generateClaimReport(policyId, startDate, endDate);
        return ResponseEntity.ok(claims);
    }
}
