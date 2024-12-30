package com.medco.Travel.insurance.repository;

import com.medco.Travel.insurance.entity.Claim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ClaimRepository extends JpaRepository<Claim, Long> {
    List<Claim> findAllByPolicyIdAndDateFiledBetween(Long policyId, LocalDate startDate, LocalDate endDate);

}
