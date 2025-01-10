package com.medco.Travel.insurance.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChapaPaymentRequest {

    private String email;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("phone_number")
    private String phoneNumber;

    private Double amount;

    private String currency = "ETB";

    @JsonProperty("tx_ref")
    private String txRef;

    @JsonProperty("callback_url")
    private String callbackUrl;

    @JsonProperty("return_url")
    private String returnUrl;

    private String title;

    private String description;

    @JsonProperty("meta[hide_receipt]")
    private boolean hideReceipt;
}


