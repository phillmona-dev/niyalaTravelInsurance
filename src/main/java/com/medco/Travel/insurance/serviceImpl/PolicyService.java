package com.medco.Travel.insurance.serviceImpl;

import com.medco.Travel.insurance.entity.*;
import com.medco.Travel.insurance.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Service
public class PolicyService {

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DestinationRepository destinationRepository;

    @Autowired
    private PassengerRepository passengerRepository;
    @Autowired
    private PremiumRepository premiumRepository;

    public Policy createPolicy(Long passengerId, Long destinationId, LocalDate startDate, LocalDate endDate) {

        Passenger passenger = passengerRepository.findById(passengerId)
                .orElseThrow(() -> new RuntimeException("Passenger not found"));

        Destination destination = destinationRepository.findById(destinationId)
                .orElseThrow(() -> new RuntimeException("Destination not found"));

        // Fetch the first available premium and unwrap the Optional
        Premium premium = (Premium) premiumRepository.findFirstByPassenger_passengerIdAndDestination_destinationId(passengerId, destinationId)
                .orElseThrow(() -> new RuntimeException("Premium not found for the given passenger and destination"));

        int duration = Period.between(startDate, endDate).getDays();
        if (duration <= 0) {
            throw new IllegalArgumentException("End date must be after start date.");
        }

        // Create a new policy
        Policy policy = new Policy();
        policy.setPolicyNumber(generatePolicyNumber());
        policy.setStartDate(startDate);
        policy.setEndDate(endDate);
        policy.setPremiumAmount(premium.getPremiumAmount());
        policy.setDestination(destination);

        // Associate the passenger with the policy
        policy.setPassengers(Collections.singletonList(passenger));

        return policyRepository.save(policy);
    }

    private String generatePolicyNumber() {

        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        // Generate a random 4-digit number
        int randomPart = (int) (Math.random() * 9000) + 1000;

        return datePart + randomPart;
    }

    public List<Policy> getPolicies() {

        return policyRepository.findAll();

    }

    // Method to get policies for a specific user
    public List<Policy> getPoliciesByPassenger(Long passengerId) {
        Passenger passenger = passengerRepository.findById(passengerId)
                .orElseThrow(() -> new RuntimeException("Passenger not found"));
        return policyRepository.findByPassengers((List<Passenger>) passenger);
    }

}
