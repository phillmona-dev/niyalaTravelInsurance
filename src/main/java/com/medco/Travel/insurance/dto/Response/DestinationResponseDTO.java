package com.medco.Travel.insurance.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DestinationResponseDTO {
    private Long id;

    private String countryName;
    private LocalDate startDate;
    private LocalDate endDate;
    private int numberOfTravelers;

    private String purposeOfTravel;
    private String travelAirline;
    private String destinationAddress;
    private String phoneToDestination;
    private String coverRequiredFor;
    private String chronicIllness;
}

