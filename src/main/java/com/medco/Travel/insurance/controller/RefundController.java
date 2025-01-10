package com.medco.Travel.insurance.controller;

import com.medco.Travel.insurance.entity.Refund;
import com.medco.Travel.insurance.serviceImpl.RefundService;
import com.medco.Travel.insurance.shared.audit.enums.RefundStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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

    @GetMapping("/search")
    public ResponseEntity<List<Refund>> searchRefund(
            @RequestParam(required = false) RefundStatus status,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) Long policyId) {

        List<Refund> refunds = refundService.getRefunds(status, startDate, endDate, policyId);
        return ResponseEntity.ok(refunds);
    }
}

