package com.medco.Travel.insurance.serviceImpl;

import com.medco.Travel.insurance.dto.Response.PremiumResponse;
import com.medco.Travel.insurance.entity.Destination;
import com.medco.Travel.insurance.entity.Passenger;
import com.medco.Travel.insurance.entity.Premium;
import com.medco.Travel.insurance.repository.DestinationRepository;
import com.medco.Travel.insurance.repository.PassengerRepository;
import com.medco.Travel.insurance.repository.PremiumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class PremiumService {

    private final PassengerRepository passengerRepository;
    private final DestinationRepository destinationRepository;
    private final PremiumRepository premiumRepository;
    private final MapfreNotifier mapfreNotifier;

    @Autowired
    public PremiumService(
            PassengerRepository passengerRepository,
            DestinationRepository destinationRepository,
            PremiumRepository premiumRepository,
            MapfreNotifier mapfreNotifier) {
        this.passengerRepository = passengerRepository;
        this.destinationRepository = destinationRepository;
        this.premiumRepository = premiumRepository;
        this.mapfreNotifier = mapfreNotifier;
    }

    /**
     * Calculates the premium, saves it to the database, and returns a response with the total premium.
     *
     * @param passengerId   the ID of the passenger
     * @param destinationId the ID of the destination
     * @param startDate     the start date of the trip
     * @param endDate       the end date of the trip
     * @return the premium response
     */
    public PremiumResponse calculateAndSavePremium(Long passengerId, Long destinationId, LocalDate startDate, LocalDate endDate) {
        // Fetch the passenger and destination
        Passenger passenger = passengerRepository.findById(passengerId)
                .orElseThrow(() -> new RuntimeException("Passenger not found"));
        Destination destination = destinationRepository.findById(destinationId)
                .orElseThrow(() -> new RuntimeException("Destination not found"));

        int numberOfTravelers = destination.getNumberOfTravelers();

        // Calculate the duration of the trip
        int duration = Period.between(startDate, endDate).getDays();

        // Calculate the premium locally
        double baseRate = 50.0; // Example base rate
        double premiumPerTraveler = baseRate * passenger.getAge() * duration;
        double premium = premiumPerTraveler * numberOfTravelers;

        // Save the premium details to the database
        Premium premiumEntity = new Premium();
        premiumEntity.setPassenger(passenger);
        premiumEntity.setDestination(destination);
        premiumEntity.setStartDate(startDate);
        premiumEntity.setEndDate(endDate);
        premiumEntity.setPremiumAmount(premium);
        premiumRepository.save(premiumEntity);

        // Create the response object
        PremiumResponse response = new PremiumResponse(premium, "Premium calculated and saved successfully");

        // Notify Mapfre (this can be called after returning the response)
//        mapfreNotifier.notifyMapfre(passenger, destination, premium);

        return response;

    }

    public List<Premium> getAllPremiums() {
        return premiumRepository.findAll();
    }

    public Premium getPremiumById(Long id) {
        return premiumRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Premium not found"));
    }

    public Premium updatePremium(Long id, Premium updatedPremium) {
        Premium existingPremium = premiumRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Premium not found"));

        existingPremium.setPremiumAmount(updatedPremium.getPremiumAmount());
        existingPremium.setStartDate(updatedPremium.getStartDate());
        existingPremium.setEndDate(updatedPremium.getEndDate());

        return premiumRepository.save(existingPremium);
    }

    public void deletePremium(Long id) {
        Premium premium = premiumRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Premium not found"));
        premiumRepository.delete(premium);
    }

}


