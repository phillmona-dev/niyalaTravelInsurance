package com.medco.Travel.insurance.repository;

import com.medco.Travel.insurance.entity.Refund;
import com.medco.Travel.insurance.shared.audit.enums.RefundStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RefundRepository extends JpaRepository<Refund, Long> {
    // Find refunds by status
    List<Refund> findByStatus(RefundStatus status);

    // Find refunds by status and request date range
    List<Refund> findByStatusAndRequestDateBetween(RefundStatus status, LocalDate startDate, LocalDate endDate);

    // Find refunds by status and request date after a certain date
    List<Refund> findByStatusAndRequestDateAfter(RefundStatus status, LocalDate startDate);

    // Find refunds by status and request date before a certain date
    List<Refund> findByStatusAndRequestDateBefore(RefundStatus status, LocalDate endDate);

    // Find refunds by request date range
    List<Refund> findByRequestDateBetween(LocalDate startDate, LocalDate endDate);

    // Find refunds by request date after a certain date
    List<Refund> findByRequestDateAfter(LocalDate startDate);

    // Find refunds by request date before a certain date
    List<Refund> findByRequestDateBefore(LocalDate endDate);

    // Find refunds by policy ID
    List<Refund> findByPolicyId(Long policyId);
}

