package com.medco.Travel.insurance.controller;

import com.medco.Travel.insurance.dto.Request.ClaimSubmissionDTO;
import com.medco.Travel.insurance.entity.Claim;
import com.medco.Travel.insurance.serviceImpl.ClaimService;
import com.medco.Travel.insurance.shared.audit.enums.ClaimStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/travel/claims")
public class ClaimController {

    private final ClaimService claimService;

    @PostMapping("/submitClaim")
    public ResponseEntity<Claim> submitClaim(@RequestBody ClaimSubmissionDTO claimDTO) {
        return ResponseEntity.ok(claimService.submitClaim(claimDTO));
    }

    @Operation(summary = "Upload Claim Document", description = "Uploads a file for a claim")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document Uploaded Successfully"),
            @ApiResponse(responseCode = "404", description = "Claim Not Found")
    })
    @PostMapping(value = "/{claimId}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadClaimDocument(
            @PathVariable Long claimId,
            @RequestPart("file") MultipartFile file) {
        claimService.uploadDocument(claimId, file);
        return ResponseEntity.ok("Document Uploaded Successfully");
    }

    @GetMapping("/policy/{policyNumber}")
    public ResponseEntity<List<Claim>> getClaimsByPolicy(@PathVariable String policyNumber){
     return ResponseEntity.ok(claimService.getClaimsByPolicyNumber(policyNumber));
    }

    @PutMapping("/{id}/decision")
    public ResponseEntity<Claim> updateClaimStatus(
            @PathVariable Long id,
            @RequestParam ClaimStatus status,
            @RequestParam(required = false) String rejectionReason
            ){
        return ResponseEntity.ok(claimService.updateClaimStatus(id, status, rejectionReason));
    }

}
