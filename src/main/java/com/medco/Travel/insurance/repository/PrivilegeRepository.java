package com.medco.Travel.insurance.repository;

import com.medco.Travel.insurance.entity.Privilege;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {

    Privilege findByPrivilegeName(String privilegeName);

//    @Query("SELECT p.privilegeName FROM Privilege p INNER JOIN RolePrivilege rp ON p.privilegeUuid = rp.privilegeUuid WHERE rp.roleUuid = :roleUuid ORDER BY p.privilegeCategory ASC")
//    List<String> findPrivilegeNamesByRoleUuid(@Param("roleUuid") String roleUuid);

    @Query("SELECT p.privilegeName, p.privilegeCategory " +
            "FROM Privilege p " +
            "INNER JOIN RolePrivilege rp ON p.privilegeUuid = rp.privilege.privilegeUuid WHERE rp.role.roleUuid = :roleUuid ORDER BY p.privilegeCategory ASC")
    List<Object[]> findPrivilegeNamesAndCategoryByRoleUuid(@Param("roleUuid") String roleUuid);

    boolean existsByPrivilegeName(String privilegeName);

    boolean existsByPrivilegeDescription(String privilegeDescription);

    Privilege findByPrivilegeUuid(String privilegeString);

    List<Privilege> findAllByPrivilegeName(String privilegeName);
    List<Privilege> findAllByPrivilegeDescription(String privilegeDescription);

    Page<Privilege> findAllByPrivilegeNameContainingOrPrivilegeDescriptionContainingOrPrivilegeCategoryContaining(
            String search, String search2, String search3, Pageable pageRequest);

    @Query("SELECT p.privilegeName FROM Privilege p " +
            "INNER JOIN RolePrivilege rp ON p.privilegeUuid = rp.privilege.privilegeUuid " +
            "WHERE rp.role.roleUuid = :roleUuid ORDER BY p.privilegeCategory ASC")
    List<String> findPrivilegeNamesByRoleUuid(@Param("roleUuid") String roleUuid);

    List<Privilege> findAllByPrivilegeNameIn(List<String> privilegeNames);
}
