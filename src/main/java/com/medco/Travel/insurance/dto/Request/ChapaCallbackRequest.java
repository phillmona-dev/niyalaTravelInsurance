package com.medco.Travel.insurance.dto.Request;

import lombok.Data;

@Data
public class ChapaCallbackRequest {
    private String txRef; // Transaction reference
    private String status; // Payment status (e.g., "success", "failed")
    private String message; // Optional message
}

