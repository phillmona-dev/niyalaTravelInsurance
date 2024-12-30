package com.medco.Travel.insurance.dto.Response;

import com.medco.Travel.insurance.shared.audit.enums.UserStatus;
import com.medco.Travel.insurance.shared.audit.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private Long id;

    private String firstName;
    private String fatherName;
    private String grandFatherName;
    private String gender;
    private String telePhone;
    private UserStatus userStatus;
    private UserType userType;
    //    private String profilePicture;
    private String roleUuid;
    private String roleName;
    private LocalDate dateOfBirth;

    private long totalPages;
    private String userUuid;
    private String email;
    private String title;

}
