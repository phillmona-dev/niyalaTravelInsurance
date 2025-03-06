package com.medco.Travel.insurance.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InsurancePremiumResponse {

    private Long id;
    private String coverRequiredFor;
    private LocalDate startDate;
    private LocalDate endDate;
    private int numberOfTravelers;
    private List<Integer> travelerAges;
    private double premiumAmount;
    private int coverLimit;
    private String referenceCode;
    private boolean isPaid;
    private int duration;

}

