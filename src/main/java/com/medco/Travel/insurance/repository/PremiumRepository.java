package com.medco.Travel.insurance.repository;

import com.medco.Travel.insurance.entity.Premium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PremiumRepository extends JpaRepository<Premium, Long> {
    Optional<Object> findByPassengerIdAndDestinationId(Long passengerId, Long destinationId);
}


