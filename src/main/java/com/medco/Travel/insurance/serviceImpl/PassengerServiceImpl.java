package com.medco.Travel.insurance.serviceImpl;

import com.medco.Travel.insurance.dto.Request.DestinationRequest;
import com.medco.Travel.insurance.dto.Request.PassengerRequest;
import com.medco.Travel.insurance.dto.Response.DependentResponse;
import com.medco.Travel.insurance.dto.Response.PassengerMyResponse;
import com.medco.Travel.insurance.dto.Response.PassengerResponse;
import com.medco.Travel.insurance.entity.Dependent;
import com.medco.Travel.insurance.entity.Destination;
import com.medco.Travel.insurance.entity.InsurancePremium;
import com.medco.Travel.insurance.entity.Passenger;
import com.medco.Travel.insurance.exception.ResourceNotFoundException;
import com.medco.Travel.insurance.repository.DependentRepository;
import com.medco.Travel.insurance.repository.DestinationRepository;
import com.medco.Travel.insurance.repository.PassengerRepository;
import com.medco.Travel.insurance.repository.InsurancePremiumRepository;
import com.medco.Travel.insurance.service.PassengerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PassengerServiceImpl implements PassengerService {

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private DependentRepository dependentRepository;

    @Autowired
    private DestinationRepository destinationRepository;

    @Autowired
    private InsurancePremiumRepository insurancePremiumRepository;

    @Override
    public PassengerMyResponse addPassenger(PassengerRequest passengerRequest) {
        // Fetch the stored premium using referenceCode
        InsurancePremium premium = insurancePremiumRepository.findByReferenceCode(passengerRequest.getReferenceCode())
                .orElseThrow(() -> new IllegalArgumentException("Invalid reference code: Premium not found."));

        // Ensure the passenger is charged the exact stored premium
        if (premium.isPaid()) {
            throw new IllegalStateException("Premium already paid for this reference code.");
        }

        // Extract DestinationRequest from PassengerRequest
        DestinationRequest destinationRequest = passengerRequest.getDestination();

        // Check if a matching Destination exists, or create a new one
        Destination destination = destinationRepository.findByPhoneToDestination(destinationRequest.getPhoneToDestination())
                .orElseGet(() -> {
                    Destination newDestination = new Destination();
                    BeanUtils.copyProperties(destinationRequest, newDestination);
                    return destinationRepository.save(newDestination);
                });

        // Map PassengerRequest to Passenger entity
        Passenger passenger = new Passenger();
        BeanUtils.copyProperties(passengerRequest, passenger);
        passenger.setAge(Period.between(passengerRequest.getDateOfBirth(), LocalDate.now()).getYears());
        passenger.setDestination(destination);
        passenger.setInsurancePremium(premium); // Link premium to passenger

        // Map Dependents if present
        if (passengerRequest.getDependents() != null && !passengerRequest.getDependents().isEmpty()) {
            List<Dependent> dependents = passengerRequest.getDependents().stream()
                    .map(dependentRequest -> {
                        Dependent dependent = new Dependent();
                        BeanUtils.copyProperties(dependentRequest, dependent);
                        dependent.setPassenger(passenger);
                        return dependent;
                    })
                    .collect(Collectors.toList());
            passenger.setDependents(dependents);
        }

        // Save Passenger
        Passenger savedPassenger = passengerRepository.save(passenger);

        // Update the premium with the registered passenger
        // Since InsurancePremium has a one-to-many relationship with Passenger,
        // we need to add the passenger to the list
        if (premium.getPassengers() == null) {
            premium.setPassengers(new java.util.ArrayList<>());
        }
        premium.getPassengers().add(savedPassenger);
        insurancePremiumRepository.save(premium); // Ensure passenger is linked in premium

        // Return response with required fields
        PassengerMyResponse passengerResponse = new PassengerMyResponse();
        passengerResponse.setPassengerId(savedPassenger.getPassengerId());
        passengerResponse.setDestinationId(savedPassenger.getDestination().getDestinationId());
        passengerResponse.setStartDate(savedPassenger.getDestination().getStartDate());
        passengerResponse.setEndDate(savedPassenger.getDestination().getEndDate());
        passengerResponse.setPremiumAmount(premium.getPremiumAmount());
        passengerResponse.setReferenceCode(premium.getReferenceCode());

        return passengerResponse;
    }



    @Override
    public PassengerResponse getPassengerById(Long id) {
        Passenger passenger = passengerRepository.findByIdWithDependents(id)
                .orElseThrow(() -> new ResourceNotFoundException("Passenger not found"));

        PassengerResponse passengerResponse = new PassengerResponse();
        BeanUtils.copyProperties(passenger, passengerResponse);

        // Map dependents to the response
        if (passenger.getDependents() != null && !passenger.getDependents().isEmpty()) {
            List<DependentResponse> dependentResponses = passenger.getDependents().stream()
                    .map(dependent -> {
                        DependentResponse dependentResponse = new DependentResponse();
                        BeanUtils.copyProperties(dependent, dependentResponse);
                        return dependentResponse;
                    })
                    .collect(Collectors.toList());
            passengerResponse.setDependents(dependentResponses);
        }

        return passengerResponse;
    }

    @Override
    public List<PassengerResponse> getAllPassengers() {
        List<Passenger> passengers = passengerRepository.findAll();

        return passengers.stream().map(passenger -> {
            PassengerResponse passengerResponse = new PassengerResponse();
            BeanUtils.copyProperties(passenger, passengerResponse);

            // Set destination
            if (passenger.getDestination() != null) {
                Destination destination = new Destination();
                BeanUtils.copyProperties(passenger.getDestination(), destination);
                passengerResponse.setDestination(destination);
            }

            // Set dependents
            if (passenger.getDependents() != null && !passenger.getDependents().isEmpty()) {
                List<DependentResponse> dependentResponses = passenger.getDependents().stream()
                        .map(dependent -> {
                            DependentResponse dependentResponse = new DependentResponse();
                            BeanUtils.copyProperties(dependent, dependentResponse);
                            return dependentResponse;
                        })
                        .collect(Collectors.toList());
                passengerResponse.setDependents(dependentResponses);
            }

            return passengerResponse;
        }).collect(Collectors.toList());
    }


    @Override
    public PassengerResponse updatePassenger(Long id, Passenger passenger) {

        PassengerResponse existingPassenger = getPassengerById(id);

        Destination destination = destinationRepository.findById(passenger.getDestination().getDestinationId())
                .orElseThrow(() -> new RuntimeException("Destination not found with ID: " + passenger.getDestination().getDestinationId()));

        existingPassenger.setFirstName(passenger.getFirstName());
        existingPassenger.setLastName(passenger.getLastName());
        existingPassenger.setDateOfBirth(passenger.getDateOfBirth());
        existingPassenger.setAge(Period.between(passenger.getDateOfBirth(), LocalDate.now()).getYears());
        existingPassenger.setPassportNumber(passenger.getPassportNumber());
        existingPassenger.setTelephone(passenger.getTelephone());

        existingPassenger.setEmail(passenger.getEmail());
        existingPassenger.setCity(passenger.getCity());
        existingPassenger.setWoreda(passenger.getWoreda());
        existingPassenger.setCitizenship(passenger.getCitizenship());
        existingPassenger.setHouseNumber(passenger.getHouseNumber());
        existingPassenger.setPostalCode(passenger.getPostalCode());
        existingPassenger.setFixedPhoneNumber(passenger.getFixedPhoneNumber());

        existingPassenger.setDestination(destination);

        return passengerRepository.save(existingPassenger);

    }

    @Override
    public void deletePassenger(Long id) {

        if (!passengerRepository.existsById(id)) {
            throw new RuntimeException("Passenger not found with ID: " + id);
        }
        passengerRepository.deleteById(id);
    }

}
