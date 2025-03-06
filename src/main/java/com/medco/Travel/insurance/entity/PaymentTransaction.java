package com.medco.Travel.insurance.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String txRef;

    @Column(nullable = false)
    private String amount;

    private String currency;
    private String email;

    private String firstName;
    private String lastName;
    private String status; // e.g., PENDING, SUCCESS, FAILED

//    @Column(nullable = true)
//    private String checkoutUrl;

    private String paymentGatewayResponse; // Store response from Chapa for reference

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

//    @ManyToOne
//    @JoinColumn(name = "insurance_premium_id", nullable = false)
//    private InsurancePremium insurancePremium;
//
//    public String getAmount() {
//        return insurancePremium != null ? String.valueOf(insurancePremium.getPremiumAmount()) : "0.0";
//    }
}

