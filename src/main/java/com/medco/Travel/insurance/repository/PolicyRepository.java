package com.medco.Travel.insurance.repository;

import com.medco.Travel.insurance.entity.Passenger;
import com.medco.Travel.insurance.entity.Policy;
import com.medco.Travel.insurance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PolicyRepository extends JpaRepository<Policy, Long> {
//    List<Policy> findByPassengersUser(User user);

    List<Policy> findByPassengers(List<Passenger> passengers);

    List<Policy> findAllByStartDateBetween(LocalDate startDate, LocalDate endDate);
}
