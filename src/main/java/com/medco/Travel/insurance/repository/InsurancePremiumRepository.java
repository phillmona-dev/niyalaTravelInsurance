package com.medco.Travel.insurance.repository;

import com.medco.Travel.insurance.entity.InsurancePremium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InsurancePremiumRepository extends JpaRepository<InsurancePremium, Long> {

    Optional<InsurancePremium> findByReferenceCode(String referenceCode);

    @Query("SELECT ip FROM InsurancePremium ip WHERE ip.referenceCode = :referenceCode AND ip.isPaid = false")
    Optional<InsurancePremium> findUnpaidPremiumByReferenceCode(@Param("referenceCode") String referenceCode);

}

