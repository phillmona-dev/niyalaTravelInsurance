package com.medco.Travel.insurance.serviceImpl;

import com.medco.Travel.insurance.entity.Policy;
import com.medco.Travel.insurance.entity.Refund;
import com.medco.Travel.insurance.repository.PolicyRepository;
import com.medco.Travel.insurance.repository.RefundRepository;
import com.medco.Travel.insurance.shared.audit.enums.RefundStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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

        LocalDate startDate = policy.getStartDate();
        LocalDate requestDate = LocalDate.now();

        // Check if the request date is on or before 2 days before the start date
        if (!requestDate.isAfter(startDate.minusDays(2))) {
            double totalPremiumPaid = policy.getPremiumAmount();
            double refundAmount = totalPremiumPaid - 200;

            if (refundAmount < 0) {
                throw new RuntimeException("Refund amount cannot be negative.");
            }

            Refund refund = new Refund();
            refund.setPolicyId(policyId);
            refund.setRefundAmount(refundAmount);
            refund.setRequestDate(requestDate);
            refund.setStatus(RefundStatus.PENDING);

            return refundRepository.save(refund);

        } else {
            throw new RuntimeException("Refund request is not allowed. You can only request a refund up to 2 days before the start date.");
        }
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

    public Page<Refund> getRefunds(RefundStatus status, LocalDate startDate, LocalDate endDate, Long policyId,
                                   int page, int size, String sortBy, String direction) {

        Pageable pageable = createPageable(page, size, sortBy, direction);

        if (status != null) {
            return getRefundsByStatus(status, startDate, endDate, pageable);
        }

        if (startDate != null || endDate != null) {
            return getRefundsByDateRange(startDate, endDate, pageable);
        }

        if (policyId != null) {
            return refundRepository.findByPolicyId(policyId, pageable);
        }

        return refundRepository.findAll(pageable);
    }

    private Pageable createPageable(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        return PageRequest.of(page, size, sort);
    }

    private Page<Refund> getRefundsByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        if (startDate != null && endDate != null) {
            return refundRepository.findByRequestDateBetween(startDate, endDate, pageable);
        } else if (startDate != null) {
            return refundRepository.findByRequestDateAfter(startDate, pageable);
        } else if (endDate != null) {
            return refundRepository.findByRequestDateBefore(endDate, pageable);
        } else {
            // Fallback - return all with pagination if no dates provided
            return refundRepository.findAll(pageable);
        }
    }

    private Page<Refund> getRefundsByStatus(RefundStatus status, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        if (startDate != null && endDate != null) {
            return refundRepository.findByStatusAndRequestDateBetween(status, startDate, endDate, pageable);
        } else if (startDate != null) {
            return refundRepository.findByStatusAndRequestDateAfter(status, startDate, pageable);
        } else if (endDate != null) {
            return refundRepository.findByStatusAndRequestDateBefore(status, endDate, pageable);
        } else {
            return refundRepository.findByStatus(status, pageable);
        }
    }

}

