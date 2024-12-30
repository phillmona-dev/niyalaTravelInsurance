package com.medco.Travel.insurance.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoleResponse {

    private String roleName;
    private String roleDescription;
    private String roleUuid;
    private List<PrivilegeResponse> privileges;


}
