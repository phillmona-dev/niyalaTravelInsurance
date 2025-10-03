package com.medco.Travel.insurance.serviceImpl;

import com.medco.Travel.insurance.entity.Claim;
import com.medco.Travel.insurance.entity.Policy;
import com.medco.Travel.insurance.repository.ClaimRepository;
import com.medco.Travel.insurance.repository.PolicyRepository;
import com.medco.Travel.insurance.shared.audit.enums.ClaimStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReportService {

    private final PolicyRepository policyRepository;
    private final ClaimRepository claimRepository;

    @Autowired
    public ReportService(PolicyRepository policyRepository, ClaimRepository claimRepository) {
        this.policyRepository = policyRepository;
        this.claimRepository = claimRepository;
    }

    public List<Policy> generatePolicyReport(LocalDate startDate, LocalDate endDate) {
        return policyRepository.findAllByStartDateBetween(startDate, endDate);
    }

    // Original method - kept for backward compatibility
    public List<Claim> generateClaimReport(Long policyId, LocalDate startDate, LocalDate endDate) {
        return claimRepository.findAllByPolicyPolicyIdAndSubscriptionDateBetween(policyId, startDate, endDate);
    }

    // New improved method - generates claims report by date range (most common use case)
    public List<Claim> generateClaimReportByDateRange(LocalDate startDate, LocalDate endDate) {
        return claimRepository.findAllBySubscriptionDateBetween(startDate, endDate);
    }

    // Enhanced method with optional filters for comprehensive reporting
    public List<Claim> generateClaimReportWithFilters(LocalDate startDate, LocalDate endDate,
                                                     Long policyId, ClaimStatus status) {
        return claimRepository.findClaimsWithFilters(startDate, endDate, policyId, status);
    }

    // Generate claims report by policy number (more user-friendly)
    public List<Claim> generateClaimReportByPolicyNumber(String policyNumber, LocalDate startDate, LocalDate endDate) {
        return claimRepository.findAllByPolicyPolicyNumberAndSubscriptionDateBetween(policyNumber, startDate, endDate);
    }

    // Generate claims report by status within date range
    public List<Claim> generateClaimReportByStatus(LocalDate startDate, LocalDate endDate, ClaimStatus status) {
        return claimRepository.findAllBySubscriptionDateBetweenAndStatus(startDate, endDate, status);
    }
}
