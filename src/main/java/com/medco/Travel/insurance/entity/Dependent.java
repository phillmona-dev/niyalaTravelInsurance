package com.medco.Travel.insurance.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Dependent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dependentId;

    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;

    private String passportNumber;
    private String relationship;
    private String chronicIllness;

    @ManyToOne
    @JoinColumn(name = "passenger_id", nullable = false)
    private Passenger passenger;
}
