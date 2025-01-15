package com.medco.Travel.insurance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Destination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long destinationId;

    private String countryName;
    private LocalDate startDate;
    private LocalDate endDate;
    private int numberOfTravelers;

    private String purposeOfTravel;
    private String travelAirline;
    private String destinationAddress;
    private String phoneToDestination;
    private String coverRequiredFor;
//    private String chronicIllness;

    @OneToMany(mappedBy = "destination", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Policy> policies = new ArrayList<>();

}
