package com.medco.Travel.insurance.repository;

import com.medco.Travel.insurance.entity.RequestedRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRequestedRepository  extends JpaRepository<RequestedRole,Long> {

    Optional<RequestedRole> findFirstByUserId(Long userId);


}
