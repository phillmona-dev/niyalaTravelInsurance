package com.medco.Travel.insurance.service;

import com.medco.Travel.insurance.dto.Request.*;
import com.medco.Travel.insurance.dto.Response.ProfileResponse;
import com.medco.Travel.insurance.dto.Response.UserMyResponse;
import com.medco.Travel.insurance.dto.Response.UserResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {

    public ResponseEntity<?> createUser(UserRequest userRequest);
    public ResponseEntity<?> authenticateUser(LoginRequest userRequest);
    public ResponseEntity<UserResponse> updateUser(String userUuid, UserUpdateRequest userRequest);
    public ResponseEntity<?> deleteUser(String userUuid);
    public UserResponse getUser(String userUuid);
    public ResponseEntity<?> approveUser(Long userId, UserAccountApprovalRequest request);

    ResponseEntity<Resource> getEvidenceDocument(Long id);

    ResponseEntity<?> updateDocument(Long id, MultipartFile file, String fileNames);

    ResponseEntity<?> deleteDocument(Long id);

    UserMyResponse getUsers(int page, int limit);

    UserResponse getUserByEmail(String email);

    ResponseEntity<?> verifyAccount(String emailVerificationToken);

    List<UserResponse> searchUsers(String searchKey, int page, int limit);

    ResponseEntity<?> resetPassword(ResetPasswordRequest resetPasswordDetail);

    ResponseEntity<?> changePassword(ResetPasswordRequest resetPasswordDetail, String userUuid);

    ResponseEntity<?> checkResetCode(ResetPasswordRequest resetPasswordDetail);

    ResponseEntity<?> uploadProfilePicture(UploadProfileRequest requestDetail) throws IOException;

    ProfileResponse getProfile(String userUuid);

    ResponseEntity<?> editProfile(String userUuid, String userName, byte[] profilePictureBytes) throws IOException;

    ResponseEntity<?> deleteProfile(String userUuid);

    ResponseEntity<?> createProfile(String userUuid, String userName, byte[] profilePictureBytes) throws IOException;
}
