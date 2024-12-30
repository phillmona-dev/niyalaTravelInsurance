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
    private Destination destination;
}
