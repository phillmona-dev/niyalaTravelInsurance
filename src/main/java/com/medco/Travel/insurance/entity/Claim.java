package com.medco.Travel.insurance.entity;

import com.medco.Travel.insurance.shared.audit.enums.ClaimStatus;
import com.medco.Travel.insurance.shared.audit.enums.ClaimType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Claim {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
   @ManyToOne
   @JoinColumn(name = "policy_id", nullable = false)
   private Policy policy;

    private String insuredName;
    private String address;
    private String countryOfResidence;
    private String passportNumber;
    private String telephoneNumber;
    private String email;
    private String destinationCountry;
    private LocalDate insuranceStartDate;
    private LocalDate insuranceEndDate;
    private LocalDate subscriptionDate;

    @Enumerated(EnumType.STRING)
    private ClaimType claimType;

    @Enumerated(EnumType.STRING)
    private ClaimStatus status = ClaimStatus.PENDING;

    private String rejectionReason;

    @OneToMany(mappedBy = "claim", cascade = CascadeType.ALL)
    private List<ClaimDocument> documents;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bank_account_id")
    private BankAccount bankAccount;
}
