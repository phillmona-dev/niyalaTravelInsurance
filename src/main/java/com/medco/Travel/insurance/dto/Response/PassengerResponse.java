package com.medco.Travel.insurance.dto.Response;

import com.medco.Travel.insurance.entity.Destination;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PassengerResponse {

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
    private String citizenship;

    private String fixedPhoneNumber;
    private String houseNumber;

    private String postalCode;

    private Destination destination;
}
