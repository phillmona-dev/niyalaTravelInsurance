package com.medco.Travel.insurance.serviceImpl;

import com.medco.Travel.insurance.dto.Request.ClaimSubmissionDTO;
import com.medco.Travel.insurance.dto.Response.MapfreResponse;
import com.medco.Travel.insurance.entity.Claim;
import com.medco.Travel.insurance.entity.ClaimDocument;
import com.medco.Travel.insurance.entity.Policy;
import com.medco.Travel.insurance.repository.ClaimDocumentRepository;
import com.medco.Travel.insurance.repository.ClaimRepository;
import com.medco.Travel.insurance.repository.PolicyRepository;
import com.medco.Travel.insurance.shared.audit.enums.ClaimStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClaimService {
    private final ClaimRepository claimRepository;
    private final ClaimDocumentRepository claimDocumentRepository;
    private final PolicyRepository policyRepository;
    private final FileStorageService fileStorageService;
    private final RestTemplate restTemplate;
    private final SmsService smsService;

    @Value("${mapfre.api.url:http://localhost:8900/mock/mapfre}")
    private String mapfreApiUrl;

    public Claim submitClaim(ClaimSubmissionDTO dto) {
        // Validate policy number
        Policy policy = policyRepository.findByPolicyNumber(dto.getPolicyNumber())
                .orElseThrow(() -> new RuntimeException("Policy number does not exist."));

        // Create claim entity
        Claim claim = Claim.builder()
                .insuredName(dto.getInsuredName())
                .address(dto.getAddress())
                .countryOfResidence(dto.getCountryOfResidence())
                .passportNumber(dto.getPassportNumber())
                .telephoneNumber(dto.getTelephoneNumber())
                .email(dto.getEmail())
                .policy(policy)
                .destinationCountry(dto.getDestinationCountry())
                .insuranceStartDate(dto.getInsuranceStartDate())
                .insuranceEndDate(dto.getInsuranceEndDate())
                .subscriptionDate(dto.getSubscriptionDate())
                .status(ClaimStatus.PENDING)
                .build();

        Claim savedClaim = claimRepository.save(claim);

        // Send claim details to the mock Mapfre API
        ResponseEntity<MapfreResponse> response = restTemplate.postForEntity(
                mapfreApiUrl + "/process-claim", savedClaim, MapfreResponse.class
        );

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            // Update claim status based on Mapfre API response
            savedClaim.setStatus(ClaimStatus.valueOf(response.getBody().getStatus()));
            claimRepository.save(savedClaim);

            // Send SMS notification via AfroMessage
            String message = "Dear " + dto.getInsuredName() + ", your claim status is: " + savedClaim.getStatus();
            smsService.sendSms(dto.getTelephoneNumber(), message);
        } else {
            throw new RuntimeException("Failed to process claim with Mapfre.");
        }

        return savedClaim;
    }

    public void uploadDocument(Long claimId, MultipartFile file) {
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new RuntimeException("Claim not found"));

        if (file.isEmpty()) {
            throw new RuntimeException("Uploaded file is empty.");
        }

        String filePath = fileStorageService.storeFile(file);

        ClaimDocument document = ClaimDocument.builder()
                .claim(claim)
                .documentName(file.getOriginalFilename())
                .documentUrl(filePath)
                .build();

        claimDocumentRepository.save(document);
    }

    public List<Claim> getClaimsByPolicyNumber(String policyNumber){
        return claimRepository.findByPolicyPolicyNumber(policyNumber);
    }

    public Claim updateClaimStatus(Long id, ClaimStatus status, String rejectionReason){
        Claim claim = claimRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Claim not found"));
        claim.setStatus(status);
        if (status == ClaimStatus.REJECTED){
            claim.setRejectionReason(rejectionReason);
        }
        return claimRepository.save(claim);
    }
}
