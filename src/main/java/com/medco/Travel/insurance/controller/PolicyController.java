package com.medco.Travel.insurance.controller;

import com.medco.Travel.insurance.entity.Policy;
import com.medco.Travel.insurance.serviceImpl.PolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/travel/policy")
public class PolicyController {

    @Autowired
    private PolicyService policyService;

    @PostMapping("/createPolicy")
    public Policy createPolicy(@RequestParam Long passengerId, @RequestParam Long destinationId, @RequestParam String startDate, @RequestParam String endDate) {
        return policyService.createPolicy(passengerId, destinationId, LocalDate.parse(startDate), LocalDate.parse(endDate));
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
