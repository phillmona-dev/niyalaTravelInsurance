package com.medco.Travel.insurance.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponse {

    private byte[] imageData;
    private String profilePicture;

    private  String userName;

}
