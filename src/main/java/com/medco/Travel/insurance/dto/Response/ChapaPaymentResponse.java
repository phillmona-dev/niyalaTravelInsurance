package com.medco.Travel.insurance.dto.Response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.medco.Travel.insurance.entity.Data;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChapaPaymentResponse {
    private String message;
    private String status;
    private String txRef;
    private Data data;
}


