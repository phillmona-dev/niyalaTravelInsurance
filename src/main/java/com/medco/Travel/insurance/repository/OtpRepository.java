package com.medco.Travel.insurance.repository;

import com.medco.Travel.insurance.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<Otp, Long> {

    Optional<Otp> findByPhoneNumber(String phoneNumber);
}
