package com.medco.Travel.insurance.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Premium {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Passenger passenger;

    @ManyToOne
    private Destination destination;

    private LocalDate startDate;
    private LocalDate endDate;

    private double premiumAmount;

    private double coverLimit;

    @ManyToOne
    @JoinColumn(name = "dependent_id")
    private Dependent dependent;

//    @Enumerated(EnumType.STRING)
//    private PremiumType premiumType;

}

