package com.medco.Travel.insurance.controller;

import com.medco.Travel.insurance.dto.Request.*;
import com.medco.Travel.insurance.dto.Response.ProfileResponse;
import com.medco.Travel.insurance.dto.Response.UserMyResponse;
import com.medco.Travel.insurance.dto.Response.UserResponse;
import com.medco.Travel.insurance.exception.*;
import com.medco.Travel.insurance.service.UserService;
import com.medco.Travel.insurance.serviceImpl.AdminService;
import io.jsonwebtoken.io.IOException;
import org.springframework.core.io.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/travel/users")
public class UserController {

    private  final  AuthenticationManager authenticationManager;
    private final UserService userService;
    private final AdminService adminService;

    public UserController(UserService userService, AuthenticationManager authenticationManager, AdminService adminService) {
        this.userService = userService;
        this.authenticationManager=authenticationManager;
        this.adminService = adminService;
    }

    @PatchMapping("/approveUser/{userUuid}")
    public ResponseEntity<?> approvePharmacist(@PathVariable String userUuid, @RequestParam boolean approved) {
        try {
            String message = adminService.approveUser(userUuid, approved);
            return ResponseEntity.ok(message);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/signIn")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest)
            throws AccountNotFoundException {
        return userService.authenticateUser(loginRequest);

    }

    @PostMapping("/user-signup")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserRequest signUpRequest) {
        try {
            return userService.createUser(signUpRequest);
        } catch (EmailAlreadyExists | InvalidPhoneException | BadRequestException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<UserMyResponse> getUsers(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        UserMyResponse users = userService.getUsers(page, limit);
        return ResponseEntity.ok(users);
    }

    @GetMapping(path = "/{userUuid}")
//    @PreAuthorize("hasRole('Read-User')")
    public UserResponse getUser(@PathVariable String userUuid) {
        return userService.getUser(userUuid);

    }

    @GetMapping("/getUser/{email}")
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email) {
        UserResponse userResponse = userService.getUserByEmail(email);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping(path = "/email/verification/{emailVerificationToken}")
//    @PreAuthorize("hasRole('Email-Verification')")
    public ResponseEntity<?> verifyAccount(@PathVariable String emailVerificationToken) {
        return userService.verifyAccount(emailVerificationToken);

    }

    @PutMapping(path = "/{userUuid}")
// @PreAuthorize("hasRole('Update-User')")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable String userUuid,
            @RequestBody UserUpdateRequest userRequest) {

        return userService.updateUser(userUuid, userRequest);
    }

    @GetMapping(path = "/search")
//    @PreAuthorize("hasRole('Read-Users')")
    public List<UserResponse> searchUsers(@RequestParam("search") String searchKey, @RequestParam(value="page", defaultValue = "1") int page,
                                          @RequestParam(value="limit", defaultValue = "25") int limit){
        return userService.searchUsers(searchKey,page,limit);

    }

    @PostMapping(path = "/uploadprofile")
//    @PreAuthorize("hasRole('Change-User-Profile')")
    public ResponseEntity<?> uploadProfilePicture(@ModelAttribute UploadProfileRequest requestDetail)
            throws java.io.IOException {
        return userService.uploadProfilePicture(requestDetail);
    }

    @PutMapping(path="/resetpassword")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest resetPasswordDetail){
        return userService.resetPassword(resetPasswordDetail);

    }

    @PutMapping(path = "/changepassword/{userUuid}")
//    @PreAuthorize("hasRole('Change-Password')")
    public ResponseEntity<?> changePassword(@RequestBody ResetPasswordRequest resetPasswordDetail,
                                            @PathVariable String userUuid) {
        return userService.changePassword(resetPasswordDetail, userUuid);

    }

    @PostMapping(path = "/password/checkresetcode")
    public ResponseEntity<?> checkResetCode(@RequestBody ResetPasswordRequest resetPasswordDetail) {
        return userService.checkResetCode(resetPasswordDetail);

    }

    @GetMapping("/open")
    public ResponseEntity<Resource> getAttachment(@RequestParam("id")  Long id) throws IOException {
        return userService.getEvidenceDocument(id);
    }

    @PutMapping(path="update-doc/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateClaimAttachment(@PathVariable Long id, @RequestParam("file") MultipartFile file, @RequestParam("fileNames") String fileNames) throws IOException {

        return userService.updateDocument(id, file, fileNames);

    }

    @DeleteMapping(path="/delete-doc{id}")
    public ResponseEntity<?> deleteDocument(@PathVariable Long id) {

        return userService.deleteDocument(id);
    }


    @PutMapping("/approve/{userId}")
    public ResponseEntity<?> approveUser(@PathVariable Long userId,@RequestBody UserAccountApprovalRequest request) {
        return userService.approveUser(userId,request);
    }

    @DeleteMapping(path = "/{userUuid}")
//    @PreAuthorize("hasRole('Delete-User')")
    @Operation(summary = "Delete System User", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> deleteUser(@PathVariable String userUuid) {
        return userService.deleteUser(userUuid);

    }

    @PostMapping(value = "/createProfile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProfile(
            @RequestParam String userUuid,
            @RequestParam String userName,
            @Valid @RequestBody MultipartFile profilePicture) throws IOException, java.io.IOException {
        byte[] profilePictureBytes = profilePicture.getBytes();
        return userService.createProfile(userUuid, userName, profilePictureBytes);
    }

    @GetMapping("/getProfile/{userUuid}")
    //@PreAuthorize("hasRole('ROLE_read_user')")
    public ProfileResponse getProfile(@PathVariable String userUuid)
    {

        return userService.getProfile(userUuid);
    }

    @PutMapping(value = "/editProfile/{userUuid}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?>editProfile(@PathVariable String userUuid,
                                        @RequestParam String userName,
                                        @Valid @RequestBody MultipartFile profilePicture) throws java.io.IOException {
        byte[] profilePictureBytes = profilePicture.getBytes();

        return userService.editProfile(userUuid,userName,profilePictureBytes);
    }

    @DeleteMapping("/deleteProfile/{userUuid}")
    public ResponseEntity<?> deleteProfile(@PathVariable String userUuid) {
        return userService.deleteProfile(userUuid);
    }

}


