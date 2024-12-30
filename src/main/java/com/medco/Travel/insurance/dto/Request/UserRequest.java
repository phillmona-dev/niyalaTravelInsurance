package com.medco.Travel.insurance.dto.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    private String firstName;
    private String fatherName;
    private LocalDate dateOfBirth;
    private String grandFatherName;
    private String gender;
    private String title;
    private String telephone;

    @NotBlank
    @Size(min = 5, max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(min = 5, max = 120)
    private String password;

    private String roleUuid;
}

