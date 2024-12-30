package com.medco.Travel.insurance.entity;

import com.medco.Travel.insurance.shared.audit.enums.RefundStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Refund {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long policyId;
    private double refundAmount;
    private LocalDate requestDate;

    @Enumerated(EnumType.STRING)
    private RefundStatus status;

}

