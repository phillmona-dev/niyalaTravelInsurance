package com.medco.Travel.insurance.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class Policy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long policyId;

    private String policyNumber;
    private LocalDate startDate;
    private LocalDate endDate;
    private String numberOfTravelers;
    private double premiumAmount;
    private String paymentStatus; // PAID or UNPAID

    @OneToMany(mappedBy = "policy", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Passenger> passengers = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "destination_destinationId", nullable = false)
    private Destination destination;

    @ManyToOne
    @JoinColumn(name = "insurance_premium_id")
    @JsonIgnore
    private InsurancePremium insurancePremium;

}
