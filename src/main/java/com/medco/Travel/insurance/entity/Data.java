package com.medco.Travel.insurance.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Data {
    @JsonProperty("checkout_url")
    private String checkoutUrl;
}