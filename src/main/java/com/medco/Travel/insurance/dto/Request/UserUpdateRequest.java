package com.medco.Travel.insurance.dto.Request;

import com.medco.Travel.insurance.shared.audit.enums.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {


    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(min = 2, max = 25)
    private String title;

    @NotBlank
    @Size(min = 2, max = 25)
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 25)
    private String fatherName;

    @NotBlank
    @Size(min = 2, max = 25)
    private String grandFatherName;

    @NotBlank
    @Size(min = 1, max = 10)
    private String Gender;

    private Date dateOfBirth;

    @NotBlank
    @Size(min = 9, max = 13)
    private String telephone;

//    private UserStatus userStatus;

    @NotNull
    private String roleUuid;

    private UserType userType;

}
