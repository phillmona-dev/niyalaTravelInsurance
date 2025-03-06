package com.medco.Travel.insurance.controller;

import com.medco.Travel.insurance.dto.Request.PassengerRequest;
import com.medco.Travel.insurance.dto.Response.PassengerMyResponse;
import com.medco.Travel.insurance.dto.Response.PassengerResponse;
import com.medco.Travel.insurance.entity.Passenger;
import com.medco.Travel.insurance.service.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/travel/passengers")
public class PassengerController {

    @Autowired
    private PassengerService passengerService;

    @PostMapping
    public ResponseEntity<PassengerMyResponse> addPassenger(@RequestBody PassengerRequest passengerRequest) {
        PassengerMyResponse passengerResponse = passengerService.addPassenger(passengerRequest);
        return new ResponseEntity<>(passengerResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public PassengerResponse getPassengerById(@PathVariable Long id) {

        return passengerService.getPassengerById(id);
    }

    @GetMapping("/all")
    public ResponseEntity<List<PassengerResponse>> getAllPassengers() {
        List<PassengerResponse> passengers = passengerService.getAllPassengers();
        return ResponseEntity.ok(passengers);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PassengerResponse> updatePassenger(@PathVariable Long id, @RequestBody Passenger passenger) {
        PassengerResponse updatedPassenger = passengerService.updatePassenger(id, passenger);
        return ResponseEntity.ok(updatedPassenger);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePassenger(@PathVariable Long id) {
        passengerService.deletePassenger(id);
        return ResponseEntity.noContent().build();
    }

}
