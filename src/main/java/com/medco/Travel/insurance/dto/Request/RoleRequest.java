package com.medco.Travel.insurance.dto.Request;

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
public class RoleRequest {

    @NotBlank
    @Size(min = 3, max = 50)
    private String roleName;

    @NotBlank
    @Size(max = 100)
    private String roleDescription;


    private String [] privileges;

}
