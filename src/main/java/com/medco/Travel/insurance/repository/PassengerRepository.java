package com.medco.Travel.insurance.repository;

import com.medco.Travel.insurance.dto.Request.PassengerRequest;
import com.medco.Travel.insurance.dto.Response.PassengerResponse;
import com.medco.Travel.insurance.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {
    PassengerResponse save(PassengerRequest passenger);

    Optional<Passenger> findById(Long id);

    List<Passenger> findAll();

    void deleteById(Long id);

    PassengerResponse save(PassengerResponse existingPassenger);

    @Query("SELECT p FROM Passenger p LEFT JOIN FETCH p.dependents WHERE p.id = :id")
    Optional<Passenger> findByIdWithDependents(@Param("id") Long id);

}
