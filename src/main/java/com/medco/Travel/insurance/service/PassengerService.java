package com.medco.Travel.insurance.service;

import com.medco.Travel.insurance.dto.Request.PassengerRequest;
import com.medco.Travel.insurance.dto.Response.PassengerResponse;
import com.medco.Travel.insurance.entity.Passenger;

import java.util.List;

public interface PassengerService {

    PassengerResponse getPassengerById(Long id);

    List<Passenger> getAllPassengers();

    PassengerResponse updatePassenger(Long id, Passenger passenger);

    void deletePassenger(Long id);

    List<PassengerResponse> addPassengers(List<PassengerRequest> passengerRequests);
}
