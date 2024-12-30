package com.medco.Travel.insurance.serviceImpl;

import com.medco.Travel.insurance.dto.Request.AddRolePrivilegesRequest;
import com.medco.Travel.insurance.dto.Request.RoleRequest;
import com.medco.Travel.insurance.dto.Response.MessageResponse;
import com.medco.Travel.insurance.dto.Response.PrivilegeResponse;
import com.medco.Travel.insurance.dto.Response.RoleMyResponse;
import com.medco.Travel.insurance.dto.Response.RoleResponse;
import com.medco.Travel.insurance.entity.Privilege;
import com.medco.Travel.insurance.entity.Role;
import com.medco.Travel.insurance.entity.RolePrivilege;
import com.medco.Travel.insurance.entity.User;
import com.medco.Travel.insurance.exception.BadRequestException;
import com.medco.Travel.insurance.repository.PrivilegeRepository;
import com.medco.Travel.insurance.repository.RolePrivilegeRepository;
import com.medco.Travel.insurance.repository.RoleRepository;
import com.medco.Travel.insurance.repository.UserRepository;
import com.medco.Travel.insurance.service.RoleService;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PrivilegeRepository privilegeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    RolePrivilegeRepository rolePrivilegeRepository;

    @Override
    public RoleResponse getRoleByEmail(String email) {

        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found with email: " + email);
        }

        Role role = roleRepository.findByRoleUuidWithPrivileges(user.getRoleUuid());
        if (role == null) {
            throw new RuntimeException("Role not found for the provided email.");
        }

        RoleResponse roleResponse = new RoleResponse();
        roleResponse.setRoleUuid(role.getRoleUuid());
        roleResponse.setRoleName(role.getRoleName());
        roleResponse.setRoleDescription(role.getRoleDescription());


        List<PrivilegeResponse> privilegeResponses = role.getPrivileges().stream()
                .map(privilege -> new PrivilegeResponse(
                        privilege.getPrivilegeName(),
                        privilege.getPrivilegeDescription(),
                        privilege.getPrivilegeCategory(),
                        privilege.getPrivilegeUuid(),
                        0))
                .collect(Collectors.toList());

        roleResponse.setPrivileges(privilegeResponses);

        return roleResponse;

    }

    @Override
    @Transactional
    public RoleResponse createRole(RoleRequest roleRequest) {

        if (roleRepository.existsByRoleName(roleRequest.getRoleName())) {
            throw new BadRequestException("Error: Role Name is already registered!");
        }

        if (roleRepository.existsByRoleDescription(roleRequest.getRoleDescription())) {
            throw new BadRequestException("Error: Role description is already registered!");
        }

        Role role = new Role(roleRequest.getRoleName(), roleRequest.getRoleDescription());
        role = roleRepository.save(role);

        Set<RolePrivilege> rolePrivileges = new HashSet<>();
        List<PrivilegeResponse> privilegeResponses = new ArrayList<>();

        if (roleRequest.getPrivileges() != null && roleRequest.getPrivileges().length > 0) {
            for (String privilegeUuid : roleRequest.getPrivileges()) {

                Privilege privilege = privilegeRepository.findByPrivilegeUuid(privilegeUuid);

                if (privilege == null) {
                    throw new BadRequestException("Error: Privilege with UUID " + privilegeUuid + " does not exist!");
                }

                PrivilegeResponse privilegeResponse = new PrivilegeResponse();
                privilegeResponse.setPrivilegeUuid(privilege.getPrivilegeUuid());
                privilegeResponse.setPrivilegeName(privilege.getPrivilegeName());
                privilegeResponse.setPrivilegeDescription(privilege.getPrivilegeDescription());
                privilegeResponse.setPrivilegeCategory(privilege.getPrivilegeCategory());

                privilegeResponses.add(privilegeResponse);

                RolePrivilege rolePrivilege = new RolePrivilege();
                rolePrivilege.setRole(role);
                rolePrivilege.setPrivilege(privilege);
                rolePrivileges.add(rolePrivilege);

            }

            rolePrivilegeRepository.saveAll(rolePrivileges);
        }

        RoleResponse roleResponse = new RoleResponse();
        BeanUtils.copyProperties(role, roleResponse);

        roleResponse.setPrivileges(privilegeResponses);

        return roleResponse;
    }

    @Override
    public RoleResponse getRole(String roleUuid) {

        User user = userRepository.findByRoleUuid(roleUuid);
        if (user == null) {
            throw new RuntimeException("User not found with email: " + roleUuid);
        }

        Role role = roleRepository.findByRoleUuidWithPrivileges(user.getRoleUuid());
        if (role == null) {
            throw new RuntimeException("Role not found for the provided role Uuid.");
        }

        RoleResponse roleResponse = new RoleResponse();
        roleResponse.setRoleUuid(role.getRoleUuid());
        roleResponse.setRoleName(role.getRoleName());
        roleResponse.setRoleDescription(role.getRoleDescription());


        List<PrivilegeResponse> privilegeResponses = role.getPrivileges().stream()
                .map(privilege -> new PrivilegeResponse(
                        privilege.getPrivilegeName(),
                        privilege.getPrivilegeDescription(),
                        privilege.getPrivilegeCategory(),
                        privilege.getPrivilegeUuid(),
                        0))
                .collect(Collectors.toList());

        roleResponse.setPrivileges(privilegeResponses);

        return roleResponse;

    }

    @Override
    public RoleMyResponse getRoles(int page, int limit) {
        if (page > 0) page = page - 1;

        Pageable pageRequest = PageRequest.of(page, limit, Sort.by("id").descending());
        Page<Role> rolePage = roleRepository.findAll(pageRequest);
        long totalPages = rolePage.getTotalPages();
        List<Role> roleList = rolePage.getContent();

        RoleMyResponse response = new RoleMyResponse();
        response.setTotalPages(totalPages);

        List<RoleResponse> roleResponseList = new ArrayList<>();
        for (Role role : roleList){
            RoleResponse roleResponse = new RoleResponse();
            BeanUtils.copyProperties(role, roleResponse);

            List<PrivilegeResponse> privilegeResponses = role.getPrivileges().stream()
                    .map(privilege -> {
                        PrivilegeResponse privilegeResponse = new PrivilegeResponse();
                        privilegeResponse.setPrivilegeName(privilege.getPrivilegeName());
                        privilegeResponse.setPrivilegeUuid(privilege.getPrivilegeUuid());
                        privilegeResponse.setPrivilegeCategory(privilege.getPrivilegeCategory());
                        privilegeResponse.setPrivilegeDescription(privilege.getPrivilegeDescription());
                        return privilegeResponse;
                    })
                    .collect(Collectors.toList());
            roleResponse.setPrivileges(privilegeResponses);
            roleResponseList.add(roleResponse);
        }

        response.setResponse(roleResponseList);
        return response;
    }

    @Override
    public List<RoleResponse> searchRoles(String searchKey, int page, int limit) {
        if (page > 0 ) page = page - 1;

        Pageable pageRequest = PageRequest.of(page, limit, Sort.by("id").descending());
        Page<Role> rolesPage = roleRepository.findByRoleNameContainingOrRoleDescriptionContaining(searchKey, searchKey, pageRequest);

        int totalPages = rolesPage.getTotalPages();

        List<Role> roleList = rolesPage.getContent();
        List<RoleResponse> roleResponseList = new ArrayList<>();
        for (Role r : roleList){
            RoleResponse rr = new RoleResponse();
            BeanUtils.copyProperties(r, rr);

            roleResponseList.add(rr);
        }

        return roleResponseList;
    }

    @Override
    public ResponseEntity<?> addRolePrivileges(String roleUuid, AddRolePrivilegesRequest rolePrivilegesRequest) {
        Role role = roleRepository.findByRoleUuid(roleUuid);
        if (role == null)
            throw new RuntimeException("Role not found.");
        String[] privilegeNames = rolePrivilegesRequest.getPrivileges();
        Set<Privilege> privileges = role.getPrivileges();

        for (String privilegeName : privilegeNames) {

            Privilege rolePrivilege = privilegeRepository.findByPrivilegeName(privilegeName);
            if (rolePrivilege == null) {
                throw new RuntimeException("Error: Privilege is not found in database.");
            }
            privileges.add(rolePrivilege);

        }

        role.setPrivileges(privileges);
        roleRepository.save(role);
        return ResponseEntity.ok(new MessageResponse("Privileges added to Role successfully"));
    }

    @Override
    public ResponseEntity<?> deleteRolePrivileges(String roleUuid, AddRolePrivilegesRequest rolePrivilegesRequest) {
        Role role = roleRepository.findByRoleUuid(roleUuid);
        if (role == null)
            throw new RuntimeException("Role not found.");

        String[] privilegeNames = rolePrivilegesRequest.getPrivileges();
        Set<Privilege> privileges = role.getPrivileges();
        Set<Privilege> deletedPrivileges = new HashSet<>();
        Set<Privilege> filteredPrivileges = new HashSet<>(privileges);

        for (String privilegeName : privilegeNames) {
            Privilege rolePrivilege = privilegeRepository.findByPrivilegeName(privilegeName);
            if (rolePrivilege == null) {
                throw new RuntimeException("Error: Privilege is not found in database.");
            }
            deletedPrivileges.add(rolePrivilege);
        }

        filteredPrivileges.removeAll(deletedPrivileges);
        role.setPrivileges(filteredPrivileges);
        roleRepository.save(role);
        return ResponseEntity.ok(new MessageResponse("Privileges deleted from a Role successfully!"));
    }

    @Transactional
    @Override
    public ResponseEntity<?> updateRole(String roleUuid, RoleRequest roleUpdateRequest) {
        Role role = roleRepository.findByRoleUuid(roleUuid);
        if (role == null)
            throw new RuntimeException("role not found!");

        if (roleRepository.existsByRoleNameAndRoleUuidNot(roleUpdateRequest.getRoleName(), role.getRoleUuid())){
            throw new RuntimeException("Role name already exists. Please choose another name");
        }

        if (roleRepository.existsByRoleDescriptionAndRoleUuidNot(roleUpdateRequest.getRoleDescription(), role.getRoleUuid())){
            throw new RuntimeException("Role description already exists . please choose another role description.");
        }

        role.setRoleName(roleUpdateRequest.getRoleName());
        role.setRoleDescription(roleUpdateRequest.getRoleDescription());

        if (roleUpdateRequest.getPrivileges() != null && roleUpdateRequest.getPrivileges().length>0){
            rolePrivilegeRepository.deleteByRole(role);

            Set<RolePrivilege> newRolePrivileges = new HashSet<>();
            for (String privilegeUuid : roleUpdateRequest.getPrivileges()){
                Privilege privilege = privilegeRepository.findByPrivilegeUuid(privilegeUuid);

                if (privilege == null)
                    throw new RuntimeException("privilege with Uuid" + privilegeUuid + "does not exist");

                RolePrivilege rolePrivilege = new RolePrivilege();
                rolePrivilege.setRole(role);
                rolePrivilege.setPrivilege(privilege);
                newRolePrivileges.add(rolePrivilege);

            }

            rolePrivilegeRepository.saveAll(newRolePrivileges);
        }

        roleRepository.save(role);
        return ResponseEntity.ok(new MessageResponse("Role updated successfully"));
    }

    @Transactional
    @Override
    public ResponseEntity<?> deleteRole(String roleUuid) {
        Role role = roleRepository.findByRoleUuidWithPrivileges(roleUuid);
        if (role == null) {
            throw new RuntimeException("Role not found");
        }

        rolePrivilegeRepository.deleteByRole(role);

        rolePrivilegeRepository.flush();

        roleRepository.delete(role);

        return ResponseEntity.ok(new MessageResponse("Role deleted successfully"));

    }


}
