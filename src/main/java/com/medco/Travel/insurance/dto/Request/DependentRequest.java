package com.medco.Travel.insurance.dto.Request;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DependentRequest {
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String relationship;
    private String passportNumber;
    private String chronicIllness;
}

