package com.medco.Travel.insurance.serviceImpl;

import com.medco.Travel.insurance.entity.Claim;
import com.medco.Travel.insurance.entity.Policy;
import com.medco.Travel.insurance.repository.ClaimRepository;
import com.medco.Travel.insurance.repository.PolicyRepository;
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

    public List<Claim> generateClaimReport(Long policyId, LocalDate startDate, LocalDate endDate) {
        return claimRepository.findAllByPolicyIdAndDateFiledBetween(policyId, startDate, endDate);
    }
}
