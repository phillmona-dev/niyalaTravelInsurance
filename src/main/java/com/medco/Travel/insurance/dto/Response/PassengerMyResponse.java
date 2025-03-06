package com.medco.Travel.insurance.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PassengerMyResponse {
    private Long passengerId;
    private Long destinationId;
    private LocalDate startDate;
    private LocalDate endDate;
    private double premiumAmount;

    private String referenceCode;

}
