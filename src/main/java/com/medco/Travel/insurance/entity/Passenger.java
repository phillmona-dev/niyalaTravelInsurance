package com.medco.Travel.insurance.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Passenger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long passengerId;

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
    private String chronicIllness;
    @ManyToOne
    @JoinColumn(name = "policy_policyId")
    private Policy policy;

    @ManyToOne
    @JoinColumn(name = "destination_destinationId", nullable = true)
    private Destination destination;

    @OneToMany(mappedBy = "passenger", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Dependent> dependents;


    @OneToOne
    @JoinColumn(name = "premium_id", nullable = false)
    private InsurancePremium insurancePremium;
//    @ManyToOne
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;
}

