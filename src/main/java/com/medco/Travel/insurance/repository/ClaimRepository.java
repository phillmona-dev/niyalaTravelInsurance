package com.medco.Travel.insurance.repository;

import com.medco.Travel.insurance.entity.Claim;
import com.medco.Travel.insurance.shared.audit.enums.ClaimStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClaimRepository extends JpaRepository<Claim, Long> {
    // Original method for specific policy filtering
    List<Claim> findAllByPolicyPolicyIdAndSubscriptionDateBetween(Long policyId, LocalDate startDate, LocalDate endDate);

    // New method for date range filtering across all policies
    List<Claim> findAllBySubscriptionDateBetween(LocalDate startDate, LocalDate endDate);

    // Filter by date range and status
    List<Claim> findAllBySubscriptionDateBetweenAndStatus(LocalDate startDate, LocalDate endDate, ClaimStatus status);

    // Filter by date range and policy number (more user-friendly than policy ID)
    List<Claim> findAllByPolicyPolicyNumberAndSubscriptionDateBetween(String policyNumber, LocalDate startDate, LocalDate endDate);

    // Existing methods
    List<Claim> findByPolicyPolicyNumber(String policyNumber);

    Optional<Object> findByTelephoneNumber(String phoneNumber);

    // Additional useful query for comprehensive reporting
    @Query("SELECT c FROM Claim c WHERE c.subscriptionDate BETWEEN :startDate AND :endDate " +
           "AND (:policyId IS NULL OR c.policy.policyId = :policyId) " +
           "AND (:status IS NULL OR c.status = :status) " +
           "ORDER BY c.subscriptionDate DESC")
    List<Claim> findClaimsWithFilters(@Param("startDate") LocalDate startDate,
                                     @Param("endDate") LocalDate endDate,
                                     @Param("policyId") Long policyId,
                                     @Param("status") ClaimStatus status);
}
