package com.medco.Travel.insurance.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
public class InsurancePremium {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String coverRequiredFor;

    private LocalDate startDate;

    private LocalDate endDate;

    private int numberOfTravelers;

    private double premiumAmount;

    private int coverLimit;

    @Column(unique = true, nullable = false)
    private String referenceCode;

    private boolean isPaid = false;
    private int tripDuration;

    @OneToOne(mappedBy = "insurancePremium", cascade = CascadeType.ALL)
    private Passenger passenger;

    public InsurancePremium() {
        this.referenceCode = UUID.randomUUID().toString(); // Generate a unique reference code at creation
    }
}


