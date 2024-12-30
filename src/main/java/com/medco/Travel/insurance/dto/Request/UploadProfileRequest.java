package com.medco.Travel.insurance.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UploadProfileRequest {

    private MultipartFile profilePicture;
    private String userUuid;

}

