package com.medco.Travel.insurance.repository;

import com.medco.Travel.insurance.entity.Dependent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DependentRepository extends JpaRepository<Dependent, Long> {
    List<Dependent> findByPassengerId(Long passengerId);
}
