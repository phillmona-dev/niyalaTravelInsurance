package com.medco.Travel.insurance.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PrivilegeResponse {

    private String privilegeName;
    private String privilegeDescription;
    private String privilegeCategory;
    private String privilegeUuid;
    private long totalPages;

}
