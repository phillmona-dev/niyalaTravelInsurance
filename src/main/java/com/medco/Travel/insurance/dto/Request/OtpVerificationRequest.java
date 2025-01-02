package com.medco.Travel.insurance.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OtpVerificationRequest {
    private String phoneNumber;
    private String otp;
}
