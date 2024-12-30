package com.medco.Travel.insurance.dto.Request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PrivilegeRequest {

    @NotBlank
    @Size(min = 3, max = 50)
    private String privilegeName;

    @NotBlank
    @Size(max = 100)
    private String privilegeDescription;

    @NotBlank
    @Column(length = 50)
    private String privilegeCategory;

}