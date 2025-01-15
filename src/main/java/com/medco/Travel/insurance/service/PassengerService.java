package com.medco.Travel.insurance.service;

import com.medco.Travel.insurance.dto.Request.PassengerRequest;
import com.medco.Travel.insurance.dto.Response.PassengerMyResponse;
import com.medco.Travel.insurance.dto.Response.PassengerResponse;
import com.medco.Travel.insurance.entity.Passenger;

import java.util.List;

public interface PassengerService {

    PassengerResponse getPassengerById(Long id);

    List<PassengerResponse> getAllPassengers();

    PassengerResponse updatePassenger(Long id, Passenger passenger);

    void deletePassenger(Long id);

    PassengerMyResponse addPassenger(PassengerRequest passengerRequest);

}
