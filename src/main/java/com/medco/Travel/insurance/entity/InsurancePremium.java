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
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    // Relationships from Premium entity
    @OneToMany(mappedBy = "insurancePremium", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Passenger> passengers = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "destination_id")
    private Destination destination;

    @ManyToOne
    @JoinColumn(name = "dependent_id")
    @JsonIgnore
    private Dependent dependent;

    // One-to-many relationship with policies (one premium can be associated with multiple policies)
    @OneToMany(mappedBy = "insurancePremium", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Policy> policies = new ArrayList<>();

    @PrePersist
    public void generateReferenceCode() {
        if (this.referenceCode == null || this.referenceCode.isEmpty()) {
            this.referenceCode = UUID.randomUUID().toString();
        }
    }
}


