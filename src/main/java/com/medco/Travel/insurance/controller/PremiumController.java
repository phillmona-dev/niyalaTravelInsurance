package com.medco.Travel.insurance.controller;

import com.medco.Travel.insurance.dto.Response.PremiumResponse;
import com.medco.Travel.insurance.entity.Premium;
import com.medco.Travel.insurance.serviceImpl.PremiumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/travel/premium")
public class PremiumController {

    private final PremiumService premiumService;

    @Autowired
    public PremiumController(PremiumService premiumService) {
        this.premiumService = premiumService;
    }

    /**
     * Endpoint to calculate the premium for a passenger and destination.
     *
     * @param passengerId the ID of the passenger
     * @param destinationId the ID of the destination
     * @param startDate the start date of the trip
     * @param endDate the end date of the trip
     * @return the premium response
     */
    @PostMapping("/calculate")
    public ResponseEntity<PremiumResponse> calculatePremium(
            @RequestParam Long passengerId,
            @RequestParam Long destinationId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        PremiumResponse response = premiumService.calculateAndSavePremium(passengerId, destinationId, startDate, endDate);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Premium>> getAllPremiums() {
        List<Premium> premiums = premiumService.getAllPremiums();
        return ResponseEntity.ok(premiums);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Premium> getPremiumById(@PathVariable Long id) {
        Premium premium = premiumService.getPremiumById(id);
        return ResponseEntity.ok(premium);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Premium> updatePremium(
            @PathVariable Long id,
            @RequestBody Premium updatedPremium) {
        Premium premium = premiumService.updatePremium(id, updatedPremium);
        return ResponseEntity.ok(premium);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePremium(@PathVariable Long id) {
        premiumService.deletePremium(id);
        return ResponseEntity.ok("Premium deleted successfully");
    }

}



