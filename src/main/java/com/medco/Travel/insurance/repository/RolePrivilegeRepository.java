package com.medco.Travel.insurance.repository;

import com.medco.Travel.insurance.entity.Role;
import com.medco.Travel.insurance.entity.RolePrivilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import jakarta.transaction.Transactional;

@Repository
public interface RolePrivilegeRepository extends JpaRepository<RolePrivilege, Integer> {
//    void deleteByRole(Role role);

    @Transactional
    @Modifying
    @Query("DELETE FROM RolePrivilege rp WHERE rp.role = :role")
    void deleteByRole(@Param("role") Role role);
}
