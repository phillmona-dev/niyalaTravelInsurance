package com.medco.Travel.insurance.controller;

import com.medco.Travel.insurance.dto.Request.PolicyRequest;
import com.medco.Travel.insurance.entity.Policy;
import com.medco.Travel.insurance.serviceImpl.PolicyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/travel/policy")
public class PolicyController {

    @Autowired
    private PolicyService policyService;

    @PostMapping("/createPolicy")
    public Policy createPolicy(@Valid @RequestBody PolicyRequest policyRequest) {
        return policyService.createPolicy(
                policyRequest.getPassengerId(),
                policyRequest.getDestinationId(),
                policyRequest.getStartDate(),
                policyRequest.getEndDate()
        );
    }

    @GetMapping("/getAll")
    public List<Policy> getAllPolicies() {

        return policyService.getPolicies();

    }

    @GetMapping("/getByUser/{passengerId}")
    public List<Policy> getPoliciesByUser(@PathVariable Long passengerId) {
        return policyService.getPoliciesByPassenger(passengerId);
    }
}
