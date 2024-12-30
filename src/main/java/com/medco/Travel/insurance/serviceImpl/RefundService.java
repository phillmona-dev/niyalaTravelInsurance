package com.medco.Travel.insurance.serviceImpl;

import com.medco.Travel.insurance.entity.Policy;
import com.medco.Travel.insurance.entity.Refund;
import com.medco.Travel.insurance.repository.PolicyRepository;
import com.medco.Travel.insurance.repository.RefundRepository;
import com.medco.Travel.insurance.shared.audit.enums.RefundStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class RefundService {

    private final RefundRepository refundRepository;
    private final PolicyRepository policyRepository;

    @Autowired
    public RefundService(RefundRepository refundRepository, PolicyRepository policyRepository) {
        this.refundRepository = refundRepository;
        this.policyRepository = policyRepository;
    }

    public Refund requestRefund(Long policyId) {
        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new RuntimeException("Policy not found"));

        double refundAmount = policy.getPremiumAmount();

        Refund refund = new Refund();
        refund.setPolicyId(policyId);
        refund.setRefundAmount(refundAmount);
        refund.setRequestDate(LocalDate.now());
        refund.setStatus(RefundStatus.PENDING);

        return refundRepository.save(refund);
    }

    public Refund approveRefund(Long refundId) {
        Refund refund = refundRepository.findById(refundId)
                .orElseThrow(() -> new RuntimeException("Refund not found"));

        refund.setStatus(RefundStatus.APPROVED);
        return refundRepository.save(refund);
    }

    public Refund rejectRefund(Long refundId) {
        Refund refund = refundRepository.findById(refundId)
                .orElseThrow(() -> new RuntimeException("Refund not found"));

        refund.setStatus(RefundStatus.REJECTED);
        return refundRepository.save(refund);
    }
}

