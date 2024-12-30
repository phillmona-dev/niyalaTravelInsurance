package com.medco.Travel.insurance.repository;

import com.medco.Travel.insurance.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {


    Boolean existsByRoleName(String roleName);

    Boolean existsByRoleDescription(String roleDescription);

    Role findByRoleUuid(String roleUuid);

    Page<Role> findByRoleNameContainingOrRoleDescriptionContaining(String searchTerm1, String searchTerm2,
                                                                   Pageable pageRequest);
//    List<Role> findAllByRoleDescription(String roleDescription);
//
//    List<Role> findAllByRoleName(String roleName);
//
//    String findRoleNameById(Integer roleId);

    boolean existsByRoleNameAndRoleUuidNot(String roleName, String roleUuid);

    boolean existsByRoleDescriptionAndRoleUuidNot(String roleDescription, String roleUuid);

    @Query("SELECT r FROM Role r LEFT JOIN FETCH r.privileges WHERE r.roleUuid = :roleUuid")
    Role findByRoleUuidWithPrivileges(@Param("roleUuid") String roleUuid);

    Role findByRoleName(String admin);

}
