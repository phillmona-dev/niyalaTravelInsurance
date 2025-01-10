package com.medco.Travel.insurance.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PremiumResponseDTO {
    private Long id;
    private double premiumAmount;
    private double coverLimit;
//    private String premiumType;
    private PassengerResponseDTO passenger;
    private DestinationResponseDTO destination;
    private DependentResponseDTO dependent;

}
