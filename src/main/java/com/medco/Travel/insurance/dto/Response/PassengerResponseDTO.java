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
public class PassengerResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private int age;
    private String passportNumber;
    private String telephone;
    private String email;
    private String city;
    private String woreda;
    private String houseNumber;
    private String citizenship;
    private String fixedPhoneNumber;
    private String postalCode;
}
