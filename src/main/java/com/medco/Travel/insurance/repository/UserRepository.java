package com.medco.Travel.insurance.repository;

import com.medco.Travel.insurance.entity.User;
import com.medco.Travel.insurance.shared.audit.enums.UserStatus;
import com.medco.Travel.insurance.shared.audit.enums.UserType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByEmail(String email);
    User findByEmail(String email);

    //    Optional<User> findByEmail(String email);
    User findByUserUuid(String userUuid);
    void deleteByUserUuid(String userUuid);
    boolean existsByTelephone(String telephone);

    User findByEmailVerificationToken(String emailVerificationToken);

    Page<User> findByFirstNameContainingAndFatherNameContaining(String firstName, String fatherName, Pageable pageableRequest);

    Page<User> findByFirstNameContainingAndFatherNameContainingAndGrandFatherNameContaining(String firstName, String fatherName, String grandFatherName, Pageable pageableRequest);

    User findByEmailAndPasswordResetCode(String email, String passwordResetCode);

    Optional<User> findByEmailOrTelephone(String email, String telephone);

    Page<User> findByFirstNameContainingOrFatherNameContainingOrGrandFatherNameContainingOrTelephoneContainingOrEmailContaining(String searchKey, String searchKey1, String searchKey2, String searchKey3, String searchKey4, Pageable pageableRequest);

    Page<User> findByUserStatus(UserStatus status, Pageable pageableRequest);

    Page<User> findByUserType(UserType userType, Pageable pageableRequest);


    User findByRoleUuid(String roleUuid);

}
