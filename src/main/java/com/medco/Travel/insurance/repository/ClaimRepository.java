package com.medco.Travel.insurance.repository;

import com.medco.Travel.insurance.entity.Claim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClaimRepository extends JpaRepository<Claim, Long> {
    List<Claim> findAllByPolicyPolicyIdAndSubscriptionDateBetween(Long policyId, LocalDate startDate, LocalDate endDate);

    List<Claim> findByPolicyPolicyNumber(String policyNumber);

    Optional<Object> findByTelephoneNumber(String phoneNumber);
}
