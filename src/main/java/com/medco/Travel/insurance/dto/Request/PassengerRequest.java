package com.medco.Travel.insurance.dto.Request;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PassengerRequest {

    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
//    private int age;
    private String passportNumber;
    private String telephone;

    private String email;
    private String city;
    private String woreda;
    private String houseNumber;
    private String citizenship;
    private String fixedPhoneNumber;
    private String postalCode;

    private DestinationRequest destination;
}
