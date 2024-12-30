package com.medco.Travel.insurance.entity;

import com.medco.Travel.insurance.shared.audit.Audit;
import com.medco.Travel.insurance.shared.audit.enums.UserStatus;
import com.medco.Travel.insurance.shared.audit.enums.UserType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "telephone")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User extends Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String fatherName;
    private String grandFatherName;
    private String gender;
    private String title;
    private LocalDate dateOfBirth;
    private String telephone;

    @Size(min = 36, max = 40)
    private String userUuid;

    @NotBlank
    @Size(min = 5, max = 50)
    @Email
    private String email;

    private String roleName;

    @NotBlank
    @Size(min = 5, max = 120)
    private String password;

    @NotNull
    private String roleUuid;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    private boolean isDeleted = false;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<EvidenceDocument> evidences = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Role> roles = new ArrayList<>();

    private String profilePicture;
    private String passwordResetCode;
    private String emailVerificationToken;
}


