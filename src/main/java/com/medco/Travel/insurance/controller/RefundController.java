package com.medco.Travel.insurance.controller;

import com.medco.Travel.insurance.entity.Refund;
import com.medco.Travel.insurance.serviceImpl.RefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/travel/refunds")
public class RefundController {

    private final RefundService refundService;

    @Autowired
    public RefundController(RefundService refundService) {
        this.refundService = refundService;
    }

    @PostMapping("/request")
    public ResponseEntity<Refund> requestRefund(@RequestParam Long policyId) {
        Refund refund = refundService.requestRefund(policyId);
        return ResponseEntity.ok(refund);
    }

    @PostMapping("/approve")
    public ResponseEntity<Refund> approveRefund(@RequestParam Long refundId) {
        Refund refund = refundService.approveRefund(refundId);
        return ResponseEntity.ok(refund);
    }

    @PostMapping("/reject")
    public ResponseEntity<Refund> rejectRefund(@RequestParam Long refundId) {
        Refund refund = refundService.rejectRefund(refundId);
        return ResponseEntity.ok(refund);
    }
}

