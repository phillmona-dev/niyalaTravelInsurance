package com.medco.Travel.insurance.serviceImpl;

import com.medco.Travel.insurance.dto.Request.InsurancePremiumRequest;
import com.medco.Travel.insurance.dto.Response.InsurancePremiumResponse;

import com.medco.Travel.insurance.entity.InsurancePremium;
import com.medco.Travel.insurance.repository.InsurancePremiumRepository;
import com.medco.Travel.insurance.repository.PremiumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;

@Service
public class InsurancePremiumService {

    @Autowired
    private ExchangeRateService exchangeRateService;
    @Autowired
    private PremiumService premiumService;
    @Autowired
    private PremiumRepository premiumRepository;
    @Autowired
    private InsurancePremiumRepository insurancePremiumRepository;

    public InsurancePremiumResponse calculatePremium(InsurancePremiumRequest request) {

        int tripDuration = (int) (ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate()) + 1);
        System.out.println("Trip duration: " + tripDuration);

        if (tripDuration <= 0) {
            throw new IllegalArgumentException("End date must be after start date.");
        }

        double totalPremiumInEuro = 0.0;
        double exchangeRate = exchangeRateService.getEuroToBirrRate();
        System.out.println("Exchange Rate (Euro to Birr): " + exchangeRate);

        // Loop through all travelers to calculate the premium for each
        for (int i = 0; i < request.getNumberOfTravelers(); i++) {
            int age = request.getTravelerAges().get(i);
            double euroPremium = calculatePremiumInEuro(request.getCoverRequiredFor(), tripDuration);
            System.out.println("Traveler " + (i + 1) + ": Euro Premium = " + euroPremium);

            double adjustedEuroPremium = applyAgeAdjustment(euroPremium, age);
            System.out.println("Traveler " + (i + 1) + ": Adjusted Euro Premium = " + adjustedEuroPremium);

            totalPremiumInEuro += adjustedEuroPremium;
        }

        System.out.println("Total Premium in Euro (before conversion): " + totalPremiumInEuro);

        // Convert the total premium to Birr
        double totalPremiumInBirr = totalPremiumInEuro * exchangeRate;
        totalPremiumInBirr = Math.round(totalPremiumInBirr * 100.0) / 100.0;
        System.out.println("Total Premium in Birr (after conversion): " + totalPremiumInBirr);

        // Determine the coverage limit based on the type of coverage
        int coverLimit = (int) getCoverLimit(request.getCoverRequiredFor());

        // Generate a unique reference code for the premium
        String referenceCode = UUID.randomUUID().toString();

        // Save the premium details in the database
        InsurancePremium premium = new InsurancePremium();

        premium.setCoverRequiredFor(request.getCoverRequiredFor());
        premium.setStartDate(request.getStartDate());
        premium.setEndDate(request.getEndDate());
        premium.setNumberOfTravelers(request.getNumberOfTravelers());
        premium.setPremiumAmount(totalPremiumInBirr);
        premium.setCoverLimit(coverLimit);
        premium.setReferenceCode(referenceCode);
        premium.setTripDuration(tripDuration);

        insurancePremiumRepository.save(premium);

        // Return the InsurancePremiumResponse with all necessary details
        return new InsurancePremiumResponse(
                premium.getId(),
                premium.getCoverRequiredFor(),
                premium.getStartDate(),
                premium.getEndDate(),
                premium.getNumberOfTravelers(),
                request.getTravelerAges(),
                premium.getPremiumAmount(),
                premium.getCoverLimit(),
                premium.getReferenceCode(),
                premium.isPaid(),
                premium.getTripDuration()
        );
    }


    private double applyAgeAdjustment(double premium, int age) {

        if (age >= 65 && age <= 80) {
            System.out.println("age adjusted premium"+ " " + premium * 1.2);

            return premium * 1.2;
        }
        else if (age > 80) {
            throw new IllegalArgumentException("Travelers over 80 years old are not eligible for insurance.");
        }
        return premium;
    }

    private double calculatePremiumInEuro(String coverRequiredFor, int duration) {
        Map<String, Map<String, Double>> premiumRates = Map.ofEntries(
                Map.entry("Africa_Asia", Map.ofEntries(
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
                .filter(entry -> isDurationWithinRange(duration, entry.getKey()))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElseThrow(() -> new RuntimeException("No rate found for the given duration and coverage"));
    }

    private double getCoverLimit(String coverRequiredFor) {
        Map<String, Double> coverLimits = Map.ofEntries(
                Map.entry("Africa_Asia", 15000.0),
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

    private boolean isDurationWithinRange(int duration, String range) {
        String[] bounds = range.split("-");
        int min = Integer.parseInt(bounds[0]);
        int max = Integer.parseInt(bounds[1]);
        return duration >= min && duration <= max;

    }
}
