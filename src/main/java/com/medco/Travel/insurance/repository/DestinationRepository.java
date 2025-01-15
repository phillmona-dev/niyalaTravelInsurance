package com.medco.Travel.insurance.repository;

import com.medco.Travel.insurance.entity.Destination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DestinationRepository extends JpaRepository<Destination, Long> {

    Optional<Destination> findByPhoneToDestination(String phoneToDestination);
}
