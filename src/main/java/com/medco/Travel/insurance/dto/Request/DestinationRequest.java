package com.medco.Travel.insurance.dto.Request;

import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor@Builder
public class DestinationRequest {

    private String countryName;
    private LocalDate startDate;
    private LocalDate endDate;
    private int numberOfTravelers;

}
