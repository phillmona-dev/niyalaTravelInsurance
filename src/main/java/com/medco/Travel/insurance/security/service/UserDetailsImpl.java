package com.medco.Travel.insurance.security.service;

import java.io.Serial;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.medco.Travel.insurance.entity.User;
import com.medco.Travel.insurance.shared.audit.enums.UserStatus;
import com.medco.Travel.insurance.shared.audit.enums.UserType;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;


public class UserDetailsImpl implements UserDetails {

    @Serial
    private static final long serialVersionUID = -3169085431065072156L;

    private String userUuid;
    private String email;
    @JsonIgnore
    private String password;
    private String roleUuid;
    //    private String roleName;

    private String firstName;
    private String fatherName;
    private String grandFatherName;
    private String gender;
    private String title;
    private String telePhone;
    private UserStatus userStatus;
    private UserType userType;
    //    private String insuranceUuid;
//    private String agencyUuid;
//    private String profilePicture;
    private int branchId;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(String userUuid, String email, String password, String roleUuid,
                           String firstName, String fatherName,String grandFatherName, String gender,String title, String telePhone,
                           UserStatus userStatus, UserType userType,
                           Collection<? extends GrantedAuthority> authorities) {
        this.userUuid = userUuid;
        this.email = email;
        this.password = password;
        this.roleUuid = roleUuid;
        this.firstName = firstName;
        this.fatherName = fatherName;
        this.grandFatherName = grandFatherName;
        this.gender = gender;
        this.title = title;
        this.telePhone = telePhone;
        this.userStatus = userStatus;
        this.userType = userType;
//        this.insuranceUuid = insuranceUuid;
//        this.agencyUuid = agencyUuid;
//        this.profilePicture = profilePicture;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(User user, List<String> privilegesForRole) {

        List<GrantedAuthority> authorities = privilegesForRole.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new UserDetailsImpl(user.getUserUuid(), user.getEmail(), user.getPassword(), user.getRoleUuid(),
                user.getFirstName(), user.getFatherName(), user.getGrandFatherName(), user.getGender(), user.getTitle(), user.getTelephone(),
                user.getUserStatus(), user.getUserType(),
                authorities
        );
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getUserUuid() {
        return userUuid;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(userUuid, user.userUuid);
    }

    @Override
    public String getUsername() {
        return email;
    }

    public List<String> getPrivileges() {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoleUuid() {
        return roleUuid;
    }

    public void setRoleUuid(String roleUuid) {
        this.roleUuid = roleUuid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getGrandFatherName() {
        return grandFatherName;
    }

    public void setGrandFatherName(String grandFatherName) {
        this.grandFatherName = grandFatherName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTelePhone() {
        return telePhone;
    }

    public void setTelePhone(String telePhone) {
        this.telePhone = telePhone;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }
}
