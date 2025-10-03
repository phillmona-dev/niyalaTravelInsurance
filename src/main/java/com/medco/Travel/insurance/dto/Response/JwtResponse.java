package com.medco.Travel.insurance.dto.Response;

import com.medco.Travel.insurance.shared.audit.enums.UserStatus;
import com.medco.Travel.insurance.shared.audit.enums.UserType;

import java.util.List;


public class JwtResponse {
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public List<String> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<String> privileges) {
        this.privileges = privileges;
    }

    private String token;
    private String type = "Bearer";
    private String userUuid;
    private String email;
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
    private List<String> privileges;

    public JwtResponse(String accessToken, String userUuid, String email, String roleUuid,
                       String firstName, String fatherName, String grandFatherName, String gender, String title,String telePhone,
                       UserStatus userStatus, UserType userType,
                       List<String> privileges) {
        this.token = accessToken;
        this.userUuid = userUuid;
        this.email = email;
        this.roleUuid = roleUuid;
//        this.roleName = roleName;
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
        this.privileges = privileges;
    }
}
