package com.medco.Travel.insurance.service;

import com.medco.Travel.insurance.dto.Request.AddRolePrivilegesRequest;
import com.medco.Travel.insurance.dto.Request.RoleRequest;
import com.medco.Travel.insurance.dto.Response.RoleMyResponse;
import com.medco.Travel.insurance.dto.Response.RoleResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RoleService {
    public RoleResponse createRole(RoleRequest roleRequest);
    public ResponseEntity<?> updateRole(String roleUuid, @Valid RoleRequest roleUpdateRequest);
    public ResponseEntity<?> deleteRole(String roleUuid);
    public RoleResponse getRole(String roleUuid);
    public RoleMyResponse getRoles(int page, int limit);
    public List<RoleResponse> searchRoles(String searchKey, int page, int limit);

    ResponseEntity<?> addRolePrivileges(String roleUuid, AddRolePrivilegesRequest rolePrivilegesRequest);

    ResponseEntity<?> deleteRolePrivileges(String roleUuid, AddRolePrivilegesRequest rolePrivilegesRequest);

    RoleResponse getRoleByEmail(String email);
}
