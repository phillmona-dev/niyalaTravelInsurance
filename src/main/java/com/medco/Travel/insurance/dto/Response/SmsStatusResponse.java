package com.medco.Travel.insurance.dto.Response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SmsStatusResponse {

    private String status;  // The status of the SMS (e.g., "delivered", "failed")
    private String messageId;  // The message ID for tracking the SMS
    private String phoneNumber;  // The phone number to which the SMS was sent
    private String errorMessage;  // If there was an error, this field will contain the error message

    // Add other fields as per the AfroMessage API response
}
