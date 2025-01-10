package com.medco.Travel.insurance.repository;

import com.medco.Travel.insurance.entity.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {

    Optional<ExchangeRate> findTopByOrderByLastUpdatedDesc();

    void deleteAllByLastUpdatedBefore(LocalDateTime localDateTime);

}

