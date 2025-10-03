package com.medco.Travel.insurance.controller;

import com.medco.Travel.insurance.entity.Refund;
import com.medco.Travel.insurance.serviceImpl.RefundService;
import com.medco.Travel.insurance.shared.audit.enums.RefundStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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
    public ResponseEntity<Page<Refund>> searchRefund(
            @RequestParam(required = false) RefundStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Long policyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "refundId") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        Page<Refund> refunds = refundService.getRefunds(status, startDate, endDate, policyId, page, size, sortBy, direction);
        return ResponseEntity.ok(refunds);
    }
}

