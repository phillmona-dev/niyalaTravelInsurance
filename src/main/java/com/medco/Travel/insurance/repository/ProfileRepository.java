package com.medco.Travel.insurance.repository;

import com.medco.Travel.insurance.entity.ProfilePicture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<ProfilePicture,Long> {

    ProfilePicture findByUserUuid(String userUuid);

    boolean existsByUserUuid(String userUuid);
}