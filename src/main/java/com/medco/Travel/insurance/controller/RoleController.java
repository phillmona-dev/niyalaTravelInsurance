package com.medco.Travel.insurance.controller;

import com.medco.Travel.insurance.dto.Request.AddRolePrivilegesRequest;
import com.medco.Travel.insurance.dto.Request.RoleRequest;
import com.medco.Travel.insurance.dto.Response.RoleMyResponse;
import com.medco.Travel.insurance.dto.Response.RoleResponse;
import com.medco.Travel.insurance.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/travel/roles")
public class RoleController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    RoleService roleService;

    @PostMapping("/create")
    public RoleResponse createRole(@Valid @RequestBody RoleRequest roleRequest) {
        return roleService.createRole(roleRequest);
    }

    @GetMapping("/getRoleByEmail/{email}")
    public RoleResponse getRoleByEmail(@PathVariable("email") String email) {
        return roleService.getRoleByEmail(email);
    }

    @GetMapping("/getRole/{roleUuid}")
    public RoleResponse getRole(@PathVariable String roleUuid){

        return roleService.getRole(roleUuid);
    }

    @GetMapping("/all")
//    @PreAuthorize("hasAuthority('ROLE_READ_PATIENT')")
    public RoleMyResponse getRoles(@RequestParam(value = "page", defaultValue = "1") int page,
                                   @RequestParam(value = "limit", defaultValue = "25") int limit){
        return roleService.getRoles(page, limit);
    }

    @GetMapping("/roleSearch/search")
    public List<RoleResponse> searchRoles(@RequestParam("search") String searchKey,
                                          @RequestParam(value = "page", defaultValue = "1") int page,
                                          @RequestParam(value = "limit", defaultValue = "500") int limit){
        return roleService.searchRoles(searchKey, page, limit);
    }

    @PutMapping(path = "/update/{roleUuid}")
    public ResponseEntity<?> updateRole(@PathVariable String roleUuid,
                                        @Valid @RequestBody RoleRequest roleUpdateRequest){
        return roleService.updateRole(roleUuid, roleUpdateRequest);
    }

    @PutMapping("/privileges/{roleUuid}")
    public ResponseEntity<?> addRolePrivileges(@PathVariable String roleUuid,
                                               @Valid @RequestBody AddRolePrivilegesRequest rolePrivilegesRequest){
        return roleService.addRolePrivileges(roleUuid, rolePrivilegesRequest);
    }

    @PutMapping("/delete/privileges/{roleUuid}")
    public ResponseEntity<?> deleteRolePrivileges(@PathVariable String roleUuid,
                                                  @Valid @RequestBody AddRolePrivilegesRequest rolePrivilegesRequest){
        return roleService.deleteRolePrivileges(roleUuid, rolePrivilegesRequest);
    }

    @DeleteMapping("/{roleUuid}")
    public ResponseEntity<?> deleteRole(@PathVariable String roleUuid) {

        return roleService.deleteRole(roleUuid);

    }

}