package com.medco.Travel.insurance.repository;

import com.medco.Travel.insurance.entity.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {
    PaymentTransaction findByTxRef(String txRef);
}

