package com.medco.Travel.insurance.controller;

import com.medco.Travel.insurance.dto.Response.SmsStatusResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/travel/notifications")
public class NotificationController {

    /**
     * This endpoint receives the SMS status callback from AfroMessage API.
     *
     * @param response the SMS status response from AfroMessage
     * @return a response indicating the status was received
     */
    @PostMapping("/sms-status")
    public ResponseEntity<String> handleSmsStatus(@RequestBody SmsStatusResponse response) {
        // Log the received SMS status
        System.out.println("Received SMS Status:");
        System.out.println("Phone Number: " + response.getPhoneNumber());
        System.out.println("Message ID: " + response.getMessageId());
        System.out.println("Status: " + response.getStatus());
        System.out.println("Error Message: " + response.getErrorMessage());

        // You can implement additional logic here, such as:
        // - Save the status in the database
        // - Notify the user about the delivery status
        // - Handle failed messages and retry logic

        // For now, we simply acknowledge the receipt of the status
        return ResponseEntity.ok("Status received");
    }
}
