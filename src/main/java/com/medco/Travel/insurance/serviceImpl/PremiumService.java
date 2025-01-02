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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PremiumService {

    private final PassengerRepository passengerRepository;
    private final DestinationRepository destinationRepository;
    private final PremiumRepository premiumRepository;
    private final MapfreNotifier mapfreNotifier;

    @Autowired
    private ExchangeRateService exchangeRateService;

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

    public PremiumResponse calculateAndSavePremium(Long passengerId, Long destinationId, LocalDate startDate, LocalDate endDate) {
        Passenger passenger = passengerRepository.findById(passengerId)
                .orElseThrow(() -> new RuntimeException("Passenger not found"));
        Destination destination = destinationRepository.findById(destinationId)
                .orElseThrow(() -> new RuntimeException("Destination not found"));

        int numberOfTravelers = destination.getNumberOfTravelers();

        int duration = Period.between(startDate, endDate).getDays() + 1; // Include both start and end dates
        if (duration <= 0) throw new RuntimeException("Invalid duration");

        double euroPremium = calculatePremiumInEuro(destination.getCoverRequiredFor(), duration);
        double exchangeRate = exchangeRateService.getEuroToBirrRate();
        double birrPremium = euroPremium * exchangeRate;

        // Adjust the premium based on the number of travelers
        double totalPremium = birrPremium * numberOfTravelers;

        // Apply age-based adjustment
        int passengerAge = calculateAge(passenger.getDateOfBirth());
        if (passengerAge >= 65 && passengerAge <= 80) {
            totalPremium += totalPremium * 0.2; // Increase by 20%
        } else if (passengerAge > 80) {
            throw new RuntimeException("Passengers above the age of 80 are not allowed");
        }

        // Get the cover limit for the destination
        double coverLimit = getCoverLimit(destination.getCoverRequiredFor());

        Premium premiumEntity = new Premium();
        premiumEntity.setPassenger(passenger);
        premiumEntity.setDestination(destination);
        premiumEntity.setStartDate(startDate);
        premiumEntity.setEndDate(endDate);
        premiumEntity.setPremiumAmount(totalPremium); // Save the adjusted premium
        premiumEntity.setCoverLimit(coverLimit); // Save the cover limit
        premiumRepository.save(premiumEntity);

        return new PremiumResponse(totalPremium, "Premium calculated and saved successfully, with a cover limit of " + coverLimit + " euros.");
    }

    private int calculateAge(LocalDate dateOfBirth) {
        if (dateOfBirth == null) throw new RuntimeException("Passenger's date of birth is not set");
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    private double calculatePremiumInEuro(String coverRequiredFor, int duration) {
        Map<String, Map<String, Double>> premiumRates = Map.ofEntries(
                Map.entry("AfricaAsia", Map.ofEntries(
                        Map.entry("1-4", 6.92), Map.entry("5-7", 7.39), Map.entry("8-10", 7.77),
                        Map.entry("11-15", 8.39), Map.entry("16-21", 9.49), Map.entry("22-30", 16.72),
                        Map.entry("31-60", 31.05), Map.entry("61-90", 43.00), Map.entry("91-180", 65.10),
                        Map.entry("181-365", 93.76)
                )),
                Map.entry("Israel", Map.ofEntries(
                        Map.entry("1-4", 8.90), Map.entry("5-7", 11.86), Map.entry("8-10", 12.47),
                        Map.entry("11-15", 13.47), Map.entry("16-21", 15.26), Map.entry("22-30", 26.86),
                        Map.entry("31-60", 49.87), Map.entry("61-90", 69.07), Map.entry("91-180", 104.56),
                        Map.entry("181-365", 150.60)
                )),
                Map.entry("Schengen", Map.ofEntries(
                        Map.entry("1-4", 10.07), Map.entry("5-7", 13.42), Map.entry("8-10", 14.09),
                        Map.entry("11-15", 15.22), Map.entry("16-21", 17.25), Map.entry("22-30", 30.37),
                        Map.entry("31-60", 56.39), Map.entry("61-90", 78.07), Map.entry("91-180", 118.20),
                        Map.entry("181-365", 170.25)
                )),
                Map.entry("WorldWideBasic", Map.ofEntries(
                        Map.entry("1-4", 21.17), Map.entry("5-7", 28.24), Map.entry("8-10", 39.83),
                        Map.entry("11-15", 42.90), Map.entry("16-21", 45.07), Map.entry("22-30", 70.37),
                        Map.entry("31-60", 107.67), Map.entry("61-90", 139.28), Map.entry("91-180", 150.19),
                        Map.entry("181-365", 193.10)
                )),
                Map.entry("WorldWidePlus", Map.ofEntries(
                        Map.entry("1-4", 24.86), Map.entry("5-7", 33.15), Map.entry("8-10", 46.76),
                        Map.entry("11-15", 50.36), Map.entry("16-21", 52.88), Map.entry("22-30", 82.61),
                        Map.entry("31-60", 126.37), Map.entry("61-90", 163.48), Map.entry("91-180", 176.30),
                        Map.entry("181-365", 226.66)
                )),
                Map.entry("WorldWideExtra", Map.ofEntries(
                        Map.entry("1-4", 28.53), Map.entry("5-7", 38.03), Map.entry("8-10", 53.65),
                        Map.entry("11-15", 57.79), Map.entry("16-21", 60.69), Map.entry("22-30", 94.78),
                        Map.entry("31-60", 145.01), Map.entry("61-90", 187.59), Map.entry("91-180", 202.28),
                        Map.entry("181-365", 260.07)
                )),
                Map.entry("PilgrimageBasic", Map.ofEntries(
                        Map.entry("1-15", 21.87), Map.entry("16-25", 28.03), Map.entry("26-45", 35.72)
                )),
                Map.entry("PilgrimagePlus", Map.ofEntries(
                        Map.entry("1-15", 24.46), Map.entry("16-25", 34.53), Map.entry("26-45", 42.22)
                )),
                Map.entry("PilgrimageExtra", Map.ofEntries(
                        Map.entry("1-15", 31.96), Map.entry("16-25", 40.86), Map.entry("26-45", 57.60)
                )),
                Map.entry("StudentEurope", Map.ofEntries(
                        Map.entry("1-182", 156.21), Map.entry("183-365", 212.52)
                )),
                Map.entry("StudentWorldWide", Map.ofEntries(
                        Map.entry("1-182", 179.55), Map.entry("183-365", 244.29)
                ))
        );

        Map<String, Double> rateMap = premiumRates.getOrDefault(coverRequiredFor, Map.of());

        return rateMap.entrySet().stream()
                .filter(entry -> isDurationInRange(duration, entry.getKey()))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElseThrow(() -> new RuntimeException("No rate found for the given duration and coverage"));
    }

    private boolean isDurationInRange(int duration, String range) {
        String[] parts = range.split("-");
        int min = Integer.parseInt(parts[0]);
        int max = Integer.parseInt(parts[1]);
        return duration >= min && duration <= max;
    }

    private double getCoverLimit(String coverRequiredFor) {
        Map<String, Double> coverLimits = Map.ofEntries(
                Map.entry("AfricaAsia", 15000.0),
                Map.entry("Israel", 30000.0),
                Map.entry("Schengen", 30000.0),
                Map.entry("WorldWideBasic", 40000.0),
                Map.entry("WorldWidePlus", 75000.0),
                Map.entry("WorldWideExtra", 150000.0),
                Map.entry("PilgrimageBasic", 10000.0),
                Map.entry("PilgrimagePlus", 15000.0),
                Map.entry("PilgrimageExtra", 25000.0),
                Map.entry("StudentEurope", 30000.0),
                Map.entry("StudentWorldWide", 100000.0)
        );

        return coverLimits.getOrDefault(coverRequiredFor, 0.0);
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


