package com.medco.Travel.insurance.dto.Request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ClaimSubmissionDTO {
    private String insuredName;
    private String address;
    private String countryOfResidence;
    private String passportNumber;
    private String telephoneNumber;
    private String email;
    private String policyNumber;
    private String destinationCountry;
    private LocalDate insuranceStartDate;
    private LocalDate insuranceEndDate;
    private LocalDate subscriptionDate;
}

