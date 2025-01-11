package com.medco.Travel.insurance.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InsurancePremiumRequest {
    private String destination;
    private LocalDate startDate;
    private LocalDate endDate;
    private int numberOfTravelers;
    private List<Integer> travelerAges;
}

