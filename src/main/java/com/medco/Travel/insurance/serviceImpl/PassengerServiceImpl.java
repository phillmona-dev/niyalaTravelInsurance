package com.medco.Travel.insurance.serviceImpl;

import com.medco.Travel.insurance.dto.Request.DestinationRequest;
import com.medco.Travel.insurance.dto.Request.PassengerRequest;
import com.medco.Travel.insurance.dto.Response.PassengerResponse;
import com.medco.Travel.insurance.entity.Destination;
import com.medco.Travel.insurance.entity.Passenger;
import com.medco.Travel.insurance.repository.DestinationRepository;
import com.medco.Travel.insurance.repository.PassengerRepository;
import com.medco.Travel.insurance.service.PassengerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PassengerServiceImpl implements PassengerService {

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private DestinationRepository destinationRepository;

    @Override
    public List<PassengerResponse> addPassengers(List<PassengerRequest> passengerRequests) {
        List<PassengerResponse> passengerResponses = new ArrayList<>();

        for (PassengerRequest passengerRequest : passengerRequests) {
            // Extract DestinationRequest from PassengerRequest
            DestinationRequest destinationRequest = passengerRequest.getDestination();

            // Check if a matching Destination exists (based on unique fields like countryName)
            Destination destination = destinationRepository.findByCountryName(destinationRequest.getCountryName())
                    .orElseGet(() -> {
                        // Create a new Destination if it doesn't exist
                        Destination newDestination = new Destination();
                        newDestination.setCountryName(destinationRequest.getCountryName());
                        newDestination.setStartDate(destinationRequest.getStartDate());
                        newDestination.setEndDate(destinationRequest.getEndDate());
                        newDestination.setNumberOfTravelers(destinationRequest.getNumberOfTravelers());

                        newDestination.setPhoneToDestination(destinationRequest.getPhoneToDestination());
                        newDestination.setDestinationAddress(destinationRequest.getDestinationAddress());
                        newDestination.setChronicIllness(destinationRequest.getChronicIllness());
                        newDestination.setCoverRequiredFor(destinationRequest.getCoverRequiredFor());
                        newDestination.setPurposeOfTravel(destinationRequest.getPurposeOfTravel());
                        newDestination.setTravelAirline(destinationRequest.getTravelAirline());

                        return destinationRepository.save(newDestination);

                    });

            // Create a new Passenger instance
            Passenger passenger = new Passenger();
            passenger.setFirstName(passengerRequest.getFirstName());
            passenger.setLastName(passengerRequest.getLastName());
            passenger.setDateOfBirth(passengerRequest.getDateOfBirth());
            passenger.setAge(Period.between(passengerRequest.getDateOfBirth(), LocalDate.now()).getYears());
            passenger.setPassportNumber(passengerRequest.getPassportNumber());
            passenger.setTelephone(passengerRequest.getTelephone());

            passenger.setEmail(passengerRequest.getEmail());
            passenger.setCitizenship(passengerRequest.getCitizenship());
            passenger.setFixedPhoneNumber(passengerRequest.getFixedPhoneNumber());
            passenger.setHouseNumber(passengerRequest.getHouseNumber());
            passenger.setCity(passengerRequest.getCity());
            passenger.setWoreda(passengerRequest.getWoreda());
            passenger.setPostalCode(passengerRequest.getPostalCode());

            passenger.setDestination(destination);

            Passenger savedPassenger = passengerRepository.save(passenger);

            // Add the response to the list
            passengerResponses.add(new PassengerResponse(

                    savedPassenger.getId(),
                    savedPassenger.getFirstName(),
                    savedPassenger.getLastName(),
                    savedPassenger.getDateOfBirth(),
                    savedPassenger.getAge(),
                    savedPassenger.getPassportNumber(),
                    savedPassenger.getTelephone(),

                    savedPassenger.getEmail(),
                    savedPassenger.getCity(),
                    savedPassenger.getWoreda(),
                    savedPassenger.getCitizenship(),
                    savedPassenger.getFixedPhoneNumber(),
                    savedPassenger.getHouseNumber(),
                    savedPassenger.getPostalCode(),

                    savedPassenger.getDestination()

            ));
        }

        return passengerResponses;
    }

    @Override
    public PassengerResponse getPassengerById(Long id) {

        Optional<Passenger> passenger = passengerRepository.findById(id);

        PassengerResponse passengerResponse = new PassengerResponse();
        BeanUtils.copyProperties(passenger, passengerResponse);

        return passengerResponse;

    }

    @Override
    public List<Passenger> getAllPassengers() {

        return passengerRepository.findAll();

    }

    @Override
    public PassengerResponse updatePassenger(Long id, Passenger passenger) {

        PassengerResponse existingPassenger = getPassengerById(id);

        Destination destination = destinationRepository.findById(passenger.getDestination().getId())
                .orElseThrow(() -> new RuntimeException("Destination not found with ID: " + passenger.getDestination().getId()));

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
